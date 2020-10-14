package app.whatsapp.commonweb.services.impl;

import app.whatsapp.common.constants.CommonConstants;
import app.whatsapp.commonweb.properties.RedisConfigProperties;
import app.whatsapp.commonweb.services.CacheService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@ConditionalOnProperty(value = "application.redis.enable", havingValue = "true")
public class LettuceCacheServiceImpl implements CacheService {

    private RedisTemplate<String, Serializable> template;
    private static final TimeUnit DEFAULT_TTL_UNIT = TimeUnit.SECONDS;
    private Long defaultTtl;

    public LettuceCacheServiceImpl(RedisTemplate<String, Serializable> redisTemplate, RedisConfigProperties redisConfigProperties) {
        this.template = redisTemplate;
        this.defaultTtl = redisConfigProperties.getDefaultTtl();
    }


    @Override
    public Set<String> getAllKeys() {
        return template.keys(CommonConstants.SpecialChars.ASTERISK);
    }

    @Override
    public void set(String key, Serializable value) {
        if (defaultTtl != null) {
            this.set(key, value, defaultTtl);
        } else {
            template.opsForValue().set(key, value, defaultTtl, DEFAULT_TTL_UNIT);
        }
    }

    @Override
    public void set(String key, Serializable value, long ttlSeconds) {
        template.opsForValue().set(key, value, ttlSeconds, DEFAULT_TTL_UNIT);
    }

    @Override
    public <T extends Serializable> Optional<T> get(String key, Class<T> tClass) {
        return Optional.ofNullable((T) template.opsForValue().get(key));
    }

    @Override
    public Optional<Serializable> get(String key) {
        return Optional.ofNullable(template.opsForValue().get(key));
    }

    @Override
    public void hSet(String key, Serializable field, Serializable value) {
        template.opsForHash().put(key, field, value);
    }

    @Override
    public void hSet(String key, Serializable field, Serializable value, long ttl) {
        template.opsForHash().put(key, field, value);
        this.expire(key, ttl);
    }

    @Override
    public void del(String key) {
        template.delete(key);
    }

    @Override
    public boolean exists(String key) {
        return 1 == template.countExistingKeys(Arrays.asList(key));
    }

    @Override
    public void expire(String key, long ttlSeconds) {
        template.expire(key, ttlSeconds, DEFAULT_TTL_UNIT);
    }

    @Override
    public void expireAt(String key, long unixMillis) {
        template.expireAt(key, new Date(unixMillis));
    }

    @Override
    public <T extends Serializable> Optional<T> getSet(String key, Serializable value, Class<T> tClass) {
        return Optional.ofNullable((T) template.opsForValue().getAndSet(key, value));
    }

    @Override
    public <T extends Serializable> Optional<T> getSet(String key, Serializable value, Class<T> tClass, long ttlSeconds) {
        Optional<T> oldValue = Optional.ofNullable((T) template.opsForValue().getAndSet(key, value));
        this.expire(key, ttlSeconds);
        return oldValue;
    }

    @Override
    public void hDel(String key, Serializable field) {
        template.opsForHash().delete(key, field);
    }

    @Override
    public boolean hExists(String key, Serializable field) {
        return template.opsForHash().hasKey(key, field);
    }

    @Override
    public Optional<Serializable> hGet(String key, Serializable field) {
        return Optional.ofNullable((Serializable) template.opsForHash().get(key, field));
    }

    @Override
    public Map<Serializable, Serializable> hGetAll(String key) {
        return (Map<Serializable, Serializable>) (Object) template.opsForHash().entries(key);
    }

    @Override
    public void hIncBy(String key, Serializable field, long incBy) {
        template.opsForHash().increment(key, field, incBy);
    }

    @Override
    public Set<Serializable> hKeys(String key) {
        return (Set<Serializable>) (Object) template.opsForHash().keys(key);
    }

    @Override
    public void hSetNx(String key, Serializable field, Serializable value) {
        template.opsForHash().putIfAbsent(key, field, value);
    }

    @Override
    public <T extends Serializable> Optional<T> hGet(String key, Serializable field, Class<T> tClass) {
        return Optional.ofNullable((T) template.opsForHash().get(key, field));
    }
}
