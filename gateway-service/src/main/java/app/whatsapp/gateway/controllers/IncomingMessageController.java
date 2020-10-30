package app.whatsapp.gateway.controllers;

import app.whatsapp.commonweb.annotations.log.Log;
import app.whatsapp.commonweb.models.gateway.Message;
import app.whatsapp.gateway.models.StompPrincipal;
import app.whatsapp.gateway.service.MqMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.security.Principal;

@Slf4j
@Controller
public class IncomingMessageController {

    private final MqMessagePublisher mqMessagePublisher;

    public IncomingMessageController(MqMessagePublisher mqMessagePublisher) {
        this.mqMessagePublisher = mqMessagePublisher;
    }

    @Log(args = true)
    @MessageMapping("/send")
    public void mapping(@Log Message message, Principal principal) {
        StompPrincipal stompPrincipal = (StompPrincipal) principal;
        message.setFromUserId(stompPrincipal.getUserProfile().getId());
        message.setTimestamp(System.currentTimeMillis());
        mqMessagePublisher.send(message);
    }

}
