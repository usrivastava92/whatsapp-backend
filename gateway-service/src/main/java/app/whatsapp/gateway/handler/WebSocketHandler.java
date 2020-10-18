package app.whatsapp.gateway.handler;

import app.whatsapp.common.enums.ECommonResponseCodes;
import app.whatsapp.common.utils.JsonUtils;
import app.whatsapp.commonweb.models.sessions.request.AddSessionRequest;
import app.whatsapp.commonweb.models.sessions.request.EvictSessionRequest;
import app.whatsapp.commonweb.models.sessions.response.AddSessionResponse;
import app.whatsapp.commonweb.models.sessions.response.EvictSessionResponse;
import app.whatsapp.gateway.config.WebsocketSessionStore;
import app.whatsapp.commonweb.models.gateway.Message;
import app.whatsapp.gateway.models.User;
import app.whatsapp.gateway.service.MqMessagePublisher;
import app.whatsapp.gateway.utils.MdcUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketHandler.class);
    private final Binding binding;
    private final RestTemplate restTemplate;
    private final WebsocketSessionStore websocketSessionStore;
    private final MqMessagePublisher mqMessagePublisher;

    @Value("${application.sessions.add.session.url}")
    private String addSessionUrl;
    @Value("${application.sessions.evict.session.url}")
    private String evictSessionUrl;

    public WebSocketHandler(@Qualifier("loadBalancedRestTemplate") RestTemplate restTemplate,
                            WebsocketSessionStore websocketSessionStore, MqMessagePublisher mqMessagePublisher, @Qualifier("outgoingQueueBinding") Binding binding) {
        this.restTemplate = restTemplate;
        this.websocketSessionStore = websocketSessionStore;
        this.mqMessagePublisher = mqMessagePublisher;
        this.binding = binding;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOGGER.info("CONNECTED!");
        User user = (User) session.getPrincipal();
        websocketSessionStore.addSession(user.getId(), session);
        AddSessionRequest addSessionRequest = new AddSessionRequest(user.getId(), binding.getRoutingKey());
        AddSessionResponse addSessionResponse = restTemplate.postForObject(addSessionUrl, addSessionRequest, AddSessionResponse.class);
        if (!addSessionResponse.getResponseStatus().getStatus().equals(ECommonResponseCodes.SUCCESS.getStatus())) {
            session.close();
        }
        MdcUtils.clear();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        User user = (User) session.getPrincipal();
        MdcUtils.setUserDetailsInMdc(user);
        Message message = JsonUtils.map(textMessage.getPayload(), Message.class);
        message.setFromUserId(user.getId());
        if (StringUtils.isBlank(message.getMessage()) || message.getToUserId() == null) {
            throw new Exception("to user can't be null");
        }
        LOGGER.info("Payload : {}", message);
        mqMessagePublisher.send(message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        if (session != null && session.getPrincipal() != null) {
            User user = (User) session.getPrincipal();
            websocketSessionStore.deleteSession(user.getId());
            restTemplate.postForEntity(evictSessionUrl, new EvictSessionRequest(user.getId()), EvictSessionResponse.class);
        }
        LOGGER.info("DISCONNECTED!");
        MdcUtils.clear();
    }
}
