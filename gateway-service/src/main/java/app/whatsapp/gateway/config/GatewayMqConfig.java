package app.whatsapp.gateway.config;

import app.whatsapp.common.constants.CommonConstants;
import app.whatsapp.gateway.constants.GatewayServiceConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;

@Slf4j
@Configuration
@ConditionalOnProperty(value = "application.mq.enable", havingValue = "true")
public class GatewayMqConfig {

    @Value("${eureka.instance.instance-id}")
    private long serverId;

    @Bean
    public Queue incomingMessageQueue() {
        log.info("Initialized queue : {}", GatewayServiceConstants.MqConstants.INCOMING_MESSAGES_QUEUE);
        return new Queue(GatewayServiceConstants.MqConstants.INCOMING_MESSAGES_QUEUE, false);
    }

    @Bean
    public Queue outgoingMessageQueue() {
        String queueName = GatewayServiceConstants.MqConstants.OUTGOING_MESSAGES_QUEUE.concat(CommonConstants.SpecialChars.UNDERSCORE + serverId);
        log.info("Initialized queue : {}", queueName);
        return new Queue(queueName, false);
    }

    @Bean
    public DirectExchange incomingQueueExchange() {
        return new DirectExchange(GatewayServiceConstants.MqConstants.INCOMING_QUEUE_EXCHANGE);
    }

    @Bean
    public DirectExchange outgoingQueueExchange() {
        return new DirectExchange(GatewayServiceConstants.MqConstants.OUTGOING_QUEUE_EXCHANGE);
    }

    @Bean
    public Binding incomingQueueBinding(@Qualifier("incomingMessageQueue") Queue queue, @Qualifier("incomingQueueExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(queue.getName());
    }

    @Bean
    public Binding outgoingQueueBinding(@Qualifier("outgoingMessageQueue") Queue queue, @Qualifier("outgoingQueueExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(queue.getName());
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter messageListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(outgoingMessageQueue());
        container.setMessageListener(messageListenerAdapter);
        return container;
    }

}
