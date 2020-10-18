package app.whatsapp.commonweb.config;

import app.whatsapp.commonweb.properties.RedisConfigProperties;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.time.Duration;

@Configuration
@ConditionalOnProperty(value = "application.redis.enable", havingValue = "true")
public class RedisConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisConfig.class);

    private RedisConfigProperties redisConfigProperties;

    public RedisConfig(RedisConfigProperties redisConfigProperties) {
        this.redisConfigProperties = redisConfigProperties;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LOGGER.info("Initializing RedisConnectionFactory");
        RedisStandaloneConfiguration redisStandaloneConfiguration;
        if (StringUtils.isNotBlank(redisConfigProperties.getHostname())) {
            redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisConfigProperties.getHostname(), redisConfigProperties.getPort());
        } else {
            redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        }
        if (StringUtils.isNotBlank(redisConfigProperties.getPassword())) {
            redisStandaloneConfiguration.setPassword(RedisPassword.of(redisConfigProperties.getPassword()));
        }
        if (redisConfigProperties.getDb() != null) {
            redisStandaloneConfiguration.setDatabase(redisConfigProperties.getDb());
        }
        if (redisConfigProperties.getConnectTimeoutMs() != null || redisConfigProperties.getCommandTimeoutMs() != null) {
            LettuceClientConfiguration.LettuceClientConfigurationBuilder lettuceClientConfigurationBuilder = LettuceClientConfiguration.builder();
            if (redisConfigProperties.getConnectTimeoutMs() != null) {
                final ClientOptions clientOptions = ClientOptions.builder().socketOptions(SocketOptions.builder().connectTimeout(Duration.ofMillis(redisConfigProperties.getConnectTimeoutMs())).build()).build();
                lettuceClientConfigurationBuilder.clientOptions(clientOptions);
            }
            if (redisConfigProperties.getCommandTimeoutMs() != null) {
                lettuceClientConfigurationBuilder.commandTimeout(Duration.ofMillis(redisConfigProperties.getCommandTimeoutMs()));
            }
            return new LettuceConnectionFactory(redisStandaloneConfiguration, lettuceClientConfigurationBuilder.build());
        }
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String, Serializable> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        LOGGER.info("Initializing RedisTemplate");
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

}
