package app.whatsapp.discoveryservice.controller;

import com.netflix.discovery.shared.Applications;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/*
Hack scheduler to keep heroku servers alive
 */
@Slf4j
@Controller
@Profile("prod")
public class ActivateAllServicesController {

    @Value("${application.web.protocol:http}://${HOSTNAME}${eureka.dashboard.path}")
    private String selfUrl;
    @Value("${application.ui.app.uri}")
    private String uiAppUrl;

    private final RestTemplate restTemplate;
    private static final String ERROR = "ERROR";
    private static final String SELF = "SELF";
    private static final String UI_APP = "UI-App";

    public ActivateAllServicesController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/activate")
    public ResponseEntity<ActivateServerResponse> heartbeat() {
        PeerAwareInstanceRegistry registry = EurekaServerContextHolder.getInstance().getServerContext().getRegistry();
        Applications applications = registry.getApplications();
        ActivateServerResponse activateServerResponse = new ActivateServerResponse();
        if (applications.getRegisteredApplications().isEmpty()) {
            log.info("No applications registered..!!");
        }
        applications.getRegisteredApplications().forEach((registeredApplication) -> {
            registeredApplication.getInstances().forEach((instance) -> {
                try {
                    ResponseEntity<String> responseEntity = restTemplate.getForEntity(instance.getStatusPageUrl(), String.class);
                    activateServerResponse.addHeartbeat(new Heartbeat(instance.getAppName(), instance.getStatusPageUrl(), responseEntity.getStatusCode().toString()));
                    log.info("Checking heartbeat for : {} : {} : {}", instance.getAppName(), instance.getStatusPageUrl(), responseEntity.getStatusCode());
                } catch (Exception e) {
                    log.error("Error in checking heartbeat for : {} : {}", instance.getAppName(), instance.getStatusPageUrl());
                    activateServerResponse.addHeartbeat(new Heartbeat(instance.getAppName(), instance.getStatusPageUrl(), ERROR));
                }
            });
        });
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(selfUrl, String.class);
            activateServerResponse.addHeartbeat(new Heartbeat(SELF, selfUrl, responseEntity.getStatusCode().toString()));
            log.info("Checking heartbeat for : SELF : {} : {}", selfUrl, responseEntity.getStatusCode());
        } catch (Exception e) {
            activateServerResponse.addHeartbeat(new Heartbeat(SELF, selfUrl, ERROR));
            log.error("Error in checking heartbeat for : SELF , {}", selfUrl);
        }
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uiAppUrl, String.class);
            activateServerResponse.addHeartbeat(new Heartbeat(UI_APP, uiAppUrl, responseEntity.getStatusCode().toString()));
            log.info("Checking heartbeat for : UI-App : {} : {}", uiAppUrl, responseEntity.getStatusCode());
        } catch (Exception e) {
            activateServerResponse.addHeartbeat(new Heartbeat(UI_APP, uiAppUrl, ERROR));
            log.error("Error in checking heartbeat for : UI-App , {}", uiAppUrl);
        }
        return ResponseEntity.ok(activateServerResponse);
    }


    private static class ActivateServerResponse {
        private final List<Heartbeat> heartbeats;

        public ActivateServerResponse() {
            heartbeats = new ArrayList<>();
        }

        public void addHeartbeat(Heartbeat heartbeat) {
            heartbeats.add(heartbeat);
        }

    }

    @Data
    @AllArgsConstructor
    private static class Heartbeat {
        private String name;
        private String url;
        private String status;
    }
}
