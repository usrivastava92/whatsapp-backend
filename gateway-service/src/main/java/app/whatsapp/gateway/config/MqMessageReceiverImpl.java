package app.whatsapp.gateway.config;

import app.whatsapp.common.utils.JsonUtils;
import app.whatsapp.commonweb.hooks.MqMessageReceiver;
import app.whatsapp.commonweb.models.gateway.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Component
public class MqMessageReceiverImpl implements MqMessageReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqMessageReceiverImpl.class);

    private WebsocketSessionStore sessionStore;

    public MqMessageReceiverImpl(WebsocketSessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    @Override
    public void handleMessage(byte[] bytes) throws IOException {
        Message message = JsonUtils.map(new String(bytes), Message.class);
        if (message.getToUserId() != null) {
            WebSocketSession webSocketSession = sessionStore.getSession(message.getToUserId());
            if (webSocketSession != null) {
                webSocketSession.sendMessage(new TextMessage(JsonUtils.toJson(message)));
            }
        }
        LOGGER.info("Message received from queue : {}", message);
    }
}
