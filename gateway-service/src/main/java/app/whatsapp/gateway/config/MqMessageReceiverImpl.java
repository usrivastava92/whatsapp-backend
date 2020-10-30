package app.whatsapp.gateway.config;

import app.whatsapp.common.utils.JsonUtils;
import app.whatsapp.commonweb.annotations.log.Log;
import app.whatsapp.commonweb.hooks.MqMessageReceiver;
import app.whatsapp.commonweb.models.gateway.Message;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Slf4j
@Component
public class MqMessageReceiverImpl implements MqMessageReceiver {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public MqMessageReceiverImpl(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void handleMessage(byte[] bytes) throws IOException {
        Message message = JsonUtils.map(new String(bytes), Message.class);
        log.info("Message received from queue : {}", message);
        simpMessagingTemplate.convertAndSendToUser(String.valueOf(message.getToUserId()), "/reply", message);
    }

}
