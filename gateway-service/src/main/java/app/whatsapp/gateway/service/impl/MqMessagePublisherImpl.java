package app.whatsapp.gateway.service.impl;

import app.whatsapp.commonweb.models.gateway.Message;
import app.whatsapp.gateway.service.MqMessagePublisher;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MqMessagePublisherImpl implements MqMessagePublisher {

    private AmqpTemplate amqpTemplate;
    private Binding binding;

    public MqMessagePublisherImpl(AmqpTemplate amqpTemplate,@Qualifier("incomingQueueBinding") Binding binding) {
        this.amqpTemplate = amqpTemplate;
        this.binding = binding;
    }

    @Override
    public void send(Message message) {
        amqpTemplate.convertAndSend(binding.getExchange(), binding.getRoutingKey(), message);
    }

    @Override
    public void send(String message) {
        amqpTemplate.convertAndSend(binding.getExchange(), binding.getRoutingKey(), message);
    }
}
