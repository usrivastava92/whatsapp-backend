package app.whtsapp.clientapp.handler;

import app.whtsapp.clientapp.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;
import java.util.Date;

public class MyStompSessionHandler extends StompSessionHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyStompSessionHandler.class);

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        LOGGER.info("New session established : " + session.getSessionId());
        session.subscribe("/topic/messages", this);
        LOGGER.info("Subscribed to /topic/messages");
        session.send("/app/chat", getSampleMessage());
        LOGGER.info("Message sent to websocket server");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        LOGGER.error("Got an exception", exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Message.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        Message msg = (Message) payload;
        LOGGER.info("Received : " + msg.getText() + " from : " + msg.getFrom());
    }

    private Message getSampleMessage() {
        Message msg = new Message();
        msg.setFrom("Client");
        msg.setText("Time is : " + new Date());
        return msg;
    }
}