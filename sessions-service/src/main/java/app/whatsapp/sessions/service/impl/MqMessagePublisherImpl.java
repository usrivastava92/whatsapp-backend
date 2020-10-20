package app.whatsapp.sessions.service.impl;

import app.whatsapp.commonweb.models.gateway.Message;
import app.whatsapp.sessions.service.MqMessagePublisher;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
public class MqMessagePublisherImpl implements MqMessagePublisher {

    private AmqpTemplate amqpTemplate;

    public MqMessagePublisherImpl(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    @Override
    public void send(String exchangeName, String routingKey, Message message) {
        amqpTemplate.convertAndSend(exchangeName, routingKey, message);
    }

    @Override
    public void send(String exchangeName, String routingKey, String message) {
        amqpTemplate.convertAndSend(exchangeName, routingKey, message);
    }
}
