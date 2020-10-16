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

    private boolean enable;
    private String hostname;
    private Integer port;
    private String username;
    private String password;
    private int connectionLimit;
    private String vHost;
    private String uri;
}
