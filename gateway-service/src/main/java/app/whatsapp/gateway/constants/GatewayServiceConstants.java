package app.whatsapp.gateway.constants;

public class GatewayServiceConstants {

    public static final class Extra {
        public static final String TOKEN = "token";
        public static final String PRINCIPAL = "principal";
    }

    public static final class MqConstants{
        public static final String INCOMING_MESSAGES_QUEUE = "whatsapp-incoming-messages";
        public static final String INCOMING_QUEUE_EXCHANGE = "whatsapp-incoming-exchange";
        public static final String OUTGOING_MESSAGES_QUEUE = "whatsapp-outgoing-messages";
        public static final String OUTGOING_QUEUE_EXCHANGE = "whatsapp-outgoing-exchange";
    }

}
