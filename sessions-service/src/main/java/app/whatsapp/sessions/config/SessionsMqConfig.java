package app.whatsapp.sessions.config;

import app.whatsapp.sessions.constants.SessionServiceConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnProperty(value = "application.mq.enable", havingValue = "true")
public class SessionsMqConfig {

    @Bean
    public Queue incomingMessagesQueue() {
        log.info("Initialized queue : {}", SessionServiceConstants.MqConstants.INCOMING_MESSAGES_QUEUE);
        return new Queue(SessionServiceConstants.MqConstants.INCOMING_MESSAGES_QUEUE, false);
    }

    @Bean
    public Exchange outgoingQueueExchange() {
        return new DirectExchange(SessionServiceConstants.MqConstants.OUTGOING_EXCHANGE);
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter messageListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(incomingMessagesQueue());
        container.setMessageListener(messageListenerAdapter);
        return container;
    }

}
