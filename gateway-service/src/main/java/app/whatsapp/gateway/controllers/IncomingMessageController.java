package app.whatsapp.gateway.controllers;

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

    private SimpMessagingTemplate template;
    private MqMessagePublisher mqMessagePublisher;

    public IncomingMessageController(SimpMessagingTemplate template, MqMessagePublisher mqMessagePublisher) {
        this.template = template;
        this.mqMessagePublisher = mqMessagePublisher;
    }

    @MessageMapping("/test")
    public void mapping(Message message, Principal principal) {
        StompPrincipal stompPrincipal = (StompPrincipal) principal;
        log.info("message received : {} : {}", message, stompPrincipal);
        message.setFromUserId(stompPrincipal.getName());
        message.setTimestamp(String.valueOf(System.currentTimeMillis()));
        mqMessagePublisher.send(message);
    }

}
