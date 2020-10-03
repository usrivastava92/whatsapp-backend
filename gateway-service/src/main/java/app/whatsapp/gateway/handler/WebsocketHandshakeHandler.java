package app.whatsapp.gateway.handler;

import app.whatsapp.commonweb.models.profile.UserProfile;
import app.whatsapp.gateway.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Component
public class WebsocketHandshakeHandler extends DefaultHandshakeHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketHandshakeHandler.class);

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        return new User((UserProfile) attributes.get("principal"));
    }
}
