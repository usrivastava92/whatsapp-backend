package app.whatsapp.discoveryservice.schedulers;

import app.whatsapp.commonweb.config.CommonWebSpringConfig;
import com.netflix.discovery.shared.Applications;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/*
Hack scheduler to keep heroku servers alive
 */
@Component
@Import(CommonWebSpringConfig.class)
public class HeartBeatScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeartBeatScheduler.class);

    @Value("${application.web.protocol}://${HOSTNAME}${eureka.dashboard.path}")
    private String selfUrl;

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    @Scheduled(fixedDelayString = "${applications.heartbeat.millis}")
    public void heartbeat() {
        PeerAwareInstanceRegistry registry = EurekaServerContextHolder.getInstance().getServerContext().getRegistry();
        Applications applications = registry.getApplications();
        if (applications.getRegisteredApplications().isEmpty()) {
            LOGGER.info("No applications registered..!!");
        }
        applications.getRegisteredApplications().forEach((registeredApplication) -> {
            registeredApplication.getInstances().forEach((instance) -> {
                try {
                    ResponseEntity<String> responseEntity = restTemplate.getForEntity(instance.getStatusPageUrl(), String.class);
                    LOGGER.info("Checking heartbeat for : {}, URI : {}, HttpStatusCode : {}", instance.getAppName(), instance.getStatusPageUrl(), responseEntity.getStatusCode());
                } catch (Exception e) {
                    LOGGER.error("Error in checking heartbeat for : {}", instance.getAppName());
                }
            });
        });
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(selfUrl, String.class);
            LOGGER.info("Checking heartbeat for : SELF, URI : {}, HttpStatusCode : {}", selfUrl.replace("/eureka", ""), responseEntity.getStatusCode());
        } catch (Exception e) {
            LOGGER.error("Error in checking heartbeat for : SELF , {}", e);
        }
    }

}
