package app.whatsapp.commonweb.services;

import java.io.Serializable;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface CacheService {

    Set<String> getAllKeys();

    void set(String key, Serializable value);

    void set(String key, Serializable value, Duration ttl);

    <T extends Serializable> Optional<T> get(String key, Class<T> tClass);

    Optional<Serializable> get(String key);

    void del(String key);

    boolean exists(String key);

    void expire(String key, Duration ttl);

    void expireAt(String key, long unixMillis);

    <T extends Serializable> Optional<T> getSet(String key, Serializable value, Class<T> tClass);

    <T extends Serializable> Optional<T> getSet(String key, Serializable value, Class<T> tClass, Duration ttl);

    Set<Serializable> hKeys(String key);

    Map<Serializable, Serializable> hGetAll(String key);

    boolean hExists(String key, String field);

    void hIncBy(String key, String field, long incBy);

    void hSet(String key, String field, Serializable value);

    void hSet(String key, String field, Serializable value, Duration ttl);

    void hSetNx(String key, String field, Serializable value);

    <T extends Serializable> Optional<T> hGet(String key, String field, Class<T> tClass);

    Optional<Serializable> hGet(String key, String field);

    void hDel(String key, String field);

}
