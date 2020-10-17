package app.whatsapp.gateway.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

@Component
public class WebsocketSessionStore {

    private Map<Long, WebSocketSession> sessionMap;

    public WebsocketSessionStore() {
        this.sessionMap = new HashMap<>();
    }

    public void addSession(Long userId, WebSocketSession webSocketSession) {
        sessionMap.put(userId, webSocketSession);
    }

    public WebSocketSession getSession(Long userId) {
        return sessionMap.get(userId);
    }

    public void deleteSession(Long userId) {
        sessionMap.remove(userId);
    }

}
