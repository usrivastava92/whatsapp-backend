package app.whatsapp.gateway.config;

import app.whatsapp.common.utils.JsonUtils;
import app.whatsapp.commonweb.models.gateway.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class WebsocketSessionStore {

    private Map<Long, WebSocketSession> sessionMap;
    private static final Long SYSTEM_USER_ID = 1000000L;
    private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketSessionStore.class);

    public WebsocketSessionStore() {
        this.sessionMap = new HashMap<>();
    }

    public void addSession(Long userId, WebSocketSession webSocketSession) {
        WebSocketSession currentSession = sessionMap.get(userId);
        if (currentSession != null) {
            Message message = new Message();
            message.setFromUserId(SYSTEM_USER_ID);
            message.setToUserId(userId);
            message.setMessage("Someone logged in from other location.. disconnecting session..!!");
            try {
                webSocketSession.sendMessage(new TextMessage(JsonUtils.toJson(message)));
                webSocketSession.close();
            } catch (IOException e) {
                LOGGER.error("Error while sending message ", e);
            }
        }
        sessionMap.put(userId, webSocketSession);
    }

    public WebSocketSession getSession(Long userId) {
        return sessionMap.get(userId);
    }

    public void deleteSession(Long userId) {
        sessionMap.remove(userId);
    }

}
