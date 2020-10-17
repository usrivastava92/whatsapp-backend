package app.whatsapp.gateway.service;

import app.whatsapp.commonweb.models.gateway.Message;

public interface MqMessagePublisher {

    void send(Message message);

    void send(String message);
}
