package app.whatsapp.commonweb.hooks;

@FunctionalInterface
public interface MqMessageReceiver {

    void handleMessage(byte[] bytes) throws Exception;

}
