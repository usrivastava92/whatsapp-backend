package app.whatsapp.sessions.config;

import app.whatsapp.common.utils.JsonUtils;
import app.whatsapp.commonweb.hooks.MqMessageReceiver;
import app.whatsapp.commonweb.models.gateway.Message;
import app.whatsapp.commonweb.services.CacheService;
import app.whatsapp.sessions.constants.SessionServiceConstants;
import app.whatsapp.sessions.service.MqMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Exchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class MqMessageReceiverImpl implements MqMessageReceiver {

    private final CacheService cacheService;
    private final MqMessagePublisher mqMessagePublisher;
    private final Exchange exchange;

    public MqMessageReceiverImpl(CacheService cacheService, MqMessagePublisher mqMessagePublisher, @Qualifier("outgoingQueueExchange") Exchange exchange) {
        this.cacheService = cacheService;
        this.mqMessagePublisher = mqMessagePublisher;
        this.exchange = exchange;
    }

    @Override
    public void handleMessage(byte[] bytes) throws Exception {
        Message message = JsonUtils.map(new String(bytes), Message.class);
        if (message.getToUserId() != null) {
            Optional<String> optionalRoutingKey = cacheService.hGet(SessionServiceConstants.USER_GATEWAY_MAPPING_KEY, message.getToUserId(), String.class);
            if (optionalRoutingKey.isPresent() && StringUtils.isNotBlank(optionalRoutingKey.get())) {
                mqMessagePublisher.send(exchange.getName(), optionalRoutingKey.get(), message);
            } else {
                throw new Exception("Routing key can't be null or empty");
            }
        }
        log.info("Message received from queue : {}", message);
    }
}
