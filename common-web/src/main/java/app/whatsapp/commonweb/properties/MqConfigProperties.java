package app.whatsapp.commonweb.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "application.mq")
@ConditionalOnProperty(value = "application.mq.enable", havingValue = "true")
public class MqConfigProperties {

    private String hostname;
    private int port;
    private String username;
    private String password;
    private String routingKey;
    private String queueName;
    private String topicExchangeName;
    private String fanoutExchangeName;
    private String directExchangeName;
    private String headersExchangeName;

}
