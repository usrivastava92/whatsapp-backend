package app.whatsapp.sessions.service;

import app.whatsapp.commonweb.models.gateway.Message;

public interface MqMessagePublisher {

    void send(String exchangeName, String routingKey, Message message);

    void send(String exchangeName, String routingKey, String message);
}
