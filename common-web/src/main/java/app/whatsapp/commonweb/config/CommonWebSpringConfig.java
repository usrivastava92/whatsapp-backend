package app.whatsapp.commonweb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class CommonWebSpringConfig {

    @Value("${application.http.connect.timeout.ms:3000}")
    private long connectionTimeout;
    @Value("${application.http.read.timeout.ms:5000}")
    private long readTimeout;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){
        return restTemplateBuilder.setConnectTimeout(Duration.ofMillis(connectionTimeout)).setReadTimeout(Duration.ofMillis(readTimeout)).build();
    }

    @Bean
    @LoadBalanced
    public RestTemplate loadBalancedRestTemplate(RestTemplateBuilder restTemplateBuilder){
        return restTemplateBuilder.setConnectTimeout(Duration.ofMillis(connectionTimeout)).setReadTimeout(Duration.ofMillis(readTimeout)).build();
    }

}
