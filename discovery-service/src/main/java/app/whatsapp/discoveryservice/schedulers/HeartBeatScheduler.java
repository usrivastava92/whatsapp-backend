package app.whatsapp.discoveryservice.schedulers;

import com.netflix.discovery.shared.Applications;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/*
Hack scheduler to keep heroku servers alive
 */
@Slf4j
@Component
@Profile("prod")
public class HeartBeatScheduler {

    @Value("${application.web.protocol:http}://${HOSTNAME}${eureka.dashboard.path}")
    private String selfUrl;
    @Value("${application.ui.app.uri}")
    private String uiAppUrl;

    private final RestTemplate restTemplate;

    public HeartBeatScheduler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Scheduled(fixedDelayString = "${applications.heartbeat.millis}")
    public void heartbeat() {
        PeerAwareInstanceRegistry registry = EurekaServerContextHolder.getInstance().getServerContext().getRegistry();
        Applications applications = registry.getApplications();
        if (applications.getRegisteredApplications().isEmpty()) {
            log.info("No applications registered..!!");
        }
        applications.getRegisteredApplications().forEach((registeredApplication) -> {
            registeredApplication.getInstances().forEach((instance) -> {
                try {
                    ResponseEntity<String> responseEntity = restTemplate.getForEntity(instance.getStatusPageUrl(), String.class);
                    log.info("Checking heartbeat for : {} : {} : {}", instance.getAppName(), instance.getStatusPageUrl(), responseEntity.getStatusCode());
                } catch (Exception e) {
                    log.error("Error in checking heartbeat for : {} : {}", instance.getAppName(), instance.getStatusPageUrl());
                }
            });
        });
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(selfUrl, String.class);
            log.info("Checking heartbeat for : SELF : {} : {}", selfUrl, responseEntity.getStatusCode());
        } catch (Exception e) {
            log.error("Error in checking heartbeat for : SELF , {}", selfUrl);
        }
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uiAppUrl, String.class);
            log.info("Checking heartbeat for : UI-App : {} : {}", uiAppUrl, responseEntity.getStatusCode());
        } catch (Exception e) {
            log.error("Error in checking heartbeat for : UI-App , {}", uiAppUrl);
        }
    }

}
