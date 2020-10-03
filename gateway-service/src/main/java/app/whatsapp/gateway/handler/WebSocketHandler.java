package app.whatsapp.gateway.handler;

import app.whatsapp.common.enums.ECommonResponseCodes;
import app.whatsapp.commonweb.models.profile.UserProfile;
import app.whatsapp.commonweb.models.sessions.request.AddSessionRequest;
import app.whatsapp.commonweb.models.sessions.response.AddSessionResponse;
import app.whatsapp.gateway.models.User;
import app.whatsapp.gateway.utils.MdcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.InetSocketAddress;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketHandler.class);

    @Autowired
    @Qualifier("loadBalancedRestTemplate")
    private RestTemplate restTemplate;

    @Value("${application.sessions.add.session.url}")
    private String addSessionUrl;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOGGER.info("CONNECTED!");
        User user = (User) session.getPrincipal();
        AddSessionRequest addSessionRequest = new AddSessionRequest();
        InetSocketAddress hostAddress = session.getLocalAddress();
        addSessionRequest.setHost("http://" + hostAddress.getHostName() + ":" + hostAddress.getPort());
        addSessionRequest.setUserId(user.getId());
        ResponseEntity<AddSessionResponse> addSessionResponse = restTemplate.postForEntity(addSessionUrl, addSessionRequest, AddSessionResponse.class);
        if (!addSessionResponse.getBody().getResponseStatus().getStatus().equals(ECommonResponseCodes.SUCCESS.getStatus())) {
            session.close();
        }
        MdcUtils.clear();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        MdcUtils.setUserDetailsInMdc((UserProfile) session.getPrincipal());
        LOGGER.info("Payload : " + message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        MdcUtils.setUserDetailsInMdc((UserProfile) session.getPrincipal());
        LOGGER.info("DISCONNECTED!");
    }
}
