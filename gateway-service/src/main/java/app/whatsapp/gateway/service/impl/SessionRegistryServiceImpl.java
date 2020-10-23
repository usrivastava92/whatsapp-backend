package app.whatsapp.gateway.service.impl;

import app.whatsapp.commonweb.models.sessions.request.AddSessionRequest;
import app.whatsapp.commonweb.models.sessions.request.EvictSessionRequest;
import app.whatsapp.commonweb.models.sessions.response.AddSessionResponse;
import app.whatsapp.commonweb.models.sessions.response.EvictSessionResponse;
import app.whatsapp.gateway.models.StompPrincipal;
import app.whatsapp.gateway.service.SessionRegistryService;
import org.springframework.amqp.core.Binding;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SessionRegistryServiceImpl implements SessionRegistryService {

    private RestTemplate restTemplate;
    private Binding outgoingQueueBinding;

    @Value("${application.sessions.add.session.url}")
    private String addSessionUrl;

    @Value("${application.sessions.evict.session.url}")
    private String evictSessionUrl;

    public SessionRegistryServiceImpl(@Qualifier("loadBalancedRestTemplate") RestTemplate restTemplate, @Qualifier("outgoingQueueBinding") Binding outgoingQueueBinding) {
        this.restTemplate = restTemplate;
        this.outgoingQueueBinding = outgoingQueueBinding;
    }

    @Override
    public AddSessionResponse registerSession(StompPrincipal stompPrincipal) {
        try {
            AddSessionRequest addSessionRequest = new AddSessionRequest(stompPrincipal.getName(), outgoingQueueBinding.getRoutingKey());
            return restTemplate.postForObject(addSessionUrl, addSessionRequest, AddSessionResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("failed to register session");
        }
    }

    @Override
    public EvictSessionResponse evictSession(StompPrincipal stompPrincipal) {
        EvictSessionRequest evictSessionRequest = new EvictSessionRequest(stompPrincipal.getName());
        return restTemplate.postForObject(evictSessionUrl, evictSessionRequest, EvictSessionResponse.class);
    }
}
