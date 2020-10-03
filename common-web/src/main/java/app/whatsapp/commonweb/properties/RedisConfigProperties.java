package app.whatsapp.commonweb.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "application.redis")
@ConditionalOnProperty(value = "application.redis.enable", havingValue = "true")
public class RedisConfigProperties {

    private String hostname;
    private int port;
    private Integer db;
    private String username;
    private String password;
    private Long defaultTtl;


}
