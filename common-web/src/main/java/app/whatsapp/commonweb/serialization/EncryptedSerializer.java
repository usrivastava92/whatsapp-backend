package app.whatsapp.commonweb.serialization;

import app.whatsapp.common.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;

import java.io.Serializable;

public class EncryptedSerializer implements RedisSerializer<Serializable> {

    @Override
    public byte[] serialize(Serializable serializable) throws SerializationException {
        try {
            return JsonUtils.OBJECT_MAPPER.writeValueAsBytes(serializable);
        } catch (JsonProcessingException e) {
            throw new SerializationException(e.getMessage(), e);
        }
    }

    @Override
    public Serializable deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null) {
            return null;
        }
        try {
            return JsonUtils.OBJECT_MAPPER.readValue(bytes, Serializable.class);
        } catch (Exception e) {
            throw new SerializationException(e.getMessage(), e);
        }
    }
}
