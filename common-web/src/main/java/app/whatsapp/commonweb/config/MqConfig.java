package app.whatsapp.commonweb.config;

import app.whatsapp.commonweb.properties.MqConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "application.mq.enable", havingValue = "true")
public class MqConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqConfig.class);

    @Autowired
    private MqConfigProperties mqConfigProperties;


    @Bean
    public ConnectionFactory connectionFactory() {
        LOGGER.info("Initializing MqConnectionFactory");
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(mqConfigProperties.getHostname(), mqConfigProperties.getPort());
        cachingConnectionFactory.setUsername(mqConfigProperties.getUsername());
        cachingConnectionFactory.setUsername(mqConfigProperties.getPassword());
        return cachingConnectionFactory;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

}
