package app.whatsapp.commonweb.properties;

import app.whatsapp.common.constants.CommonConstants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "application.redis")
@ConditionalOnProperty(value = "application.redis.enable", havingValue = "true")
public class RedisConfigProperties {

    private static final String SCHEMA_SEPARATOR = "://";

    private boolean enable;
    private String hostname;
    private int port;
    private Integer db;
    private String password;
    private Long defaultTtlSeconds;
    private Long connectTimeoutMs;
    private Long commandTimeoutMs;
    private String uri;

    public RedisConfigProperties() {
        log.info("Initializing redis configuration");
        populateDetailsUsingUri();
    }

    private void populateDetailsUsingUri() {
        if (StringUtils.isNotBlank(uri)) {
            String[] tokens = uri.split(SCHEMA_SEPARATOR);
            if (tokens.length < 2) {
                throw new IllegalArgumentException("redis uri is invalid");
            }
            String[] creds = tokens[1].split(CommonConstants.SpecialChars.COLON);
            String user = creds[0];
            if (creds.length < 3) {
                throw new IllegalArgumentException("redis uri is invalid");
            }
            this.port = Integer.parseInt(creds[2]);
            String[] passHost = creds[1].split(CommonConstants.SpecialChars.AT_THE_RATE);
            if (passHost.length < 2) {
                throw new IllegalArgumentException("redis uri is invalid");
            }
            this.password = passHost[0];
            this.hostname = passHost[1];
        }
    }

}
