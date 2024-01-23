package fc.be.app.common.authentication.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RedisClient {
    private final RedisTemplate<String, String> redisTemplate;

    public String getValue(String prefix, String target) {
        String key = prefix + target;
        return redisTemplate.opsForValue().get(key);
    }

    public void setValue(String prefix, String target, String value, Duration storeDuration) {
        String key = prefix + target;
        redisTemplate.opsForValue().set(key, value, storeDuration);
    }

    public Boolean setValueIfAbsent(String prefix, String target, String value, Duration storeDuration) {
        String key = prefix + target;
        return redisTemplate.opsForValue().setIfAbsent(key, value, storeDuration);
    }

    public Map<String, String> getValues(String prefix, String target) {
        String key = prefix + target;
        Map<String, String> convertedMap = new HashMap<>();
        redisTemplate.opsForHash().entries(key).forEach((hashKey, value) -> convertedMap.put(hashKey.toString(), value.toString()));
        return convertedMap;
    }

    public void setValues(String prefix, String target, Map<String, String> values, Duration storeDuration) {
        String key = prefix + target;
        redisTemplate.opsForHash().putAll(key, values);
        redisTemplate.expire(key, storeDuration);
    }

    public Long getExpire(String prefix, String target) {
        String key = prefix + target;
        return redisTemplate.getExpire(key);
    }

    public void setExpire(String prefix, String target, Duration expireDuration) {
        String key = prefix + target;
        redisTemplate.expire(key, expireDuration);
    }

    public Boolean isExist(String prefix, String target) {
        String key = prefix + target;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public Long increment(String prefix, String target) {
        String key = prefix + target;
        return redisTemplate.opsForValue().increment(key);
    }

    public void remove(String prefix, String target) {
        String key = prefix + target;
        redisTemplate.opsForValue().getAndDelete(key);
    }
}
