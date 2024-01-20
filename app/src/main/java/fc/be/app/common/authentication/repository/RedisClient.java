package fc.be.app.common.authentication.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisClient {
    private static final String COUNT_PREFIX = "count_";
    private static final String BLOCKED_PREFIX = "blocked_email_";

    private final RedisTemplate<String, String> redisTemplate;

    public boolean isBlocked(String prefix, String target) {
        String key = BLOCKED_PREFIX + prefix + target;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public Long getBlockedExpire(String prefix, String target) {
        String key = BLOCKED_PREFIX + prefix + target;
        return redisTemplate.getExpire(key);
    }

    public void blockIfExceed(String prefix, String target, int maxCount, Duration countDuration, Duration blockDuration) {
        String key = COUNT_PREFIX + prefix + target;
        Long count = redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, countDuration);
        if (count != null && count >= maxCount) {
            block(prefix, target, blockDuration);
        }
    }

    private void block(String prefix, String target, Duration blockDuration) {
        String key = BLOCKED_PREFIX + prefix + target;
        redisTemplate.opsForValue().set(key, "blacklisted");
        redisTemplate.expire(key, blockDuration);
    }

    public void setValue(String prefix, String target, String value, Duration storeDuration) {
        String key = prefix + target;
        redisTemplate.opsForValue().set(key, value, storeDuration);
    }

    public String getStringValue(String prefix, String target) {
        String key = prefix + target;
        return redisTemplate.opsForValue().get(key);
    }

    public Boolean setValueIfAbsent(String prefix, String target, String value, Duration storeDuration) {
        String key = prefix + target;
        return redisTemplate.opsForValue().setIfAbsent(key, value, storeDuration);
    }

    public void remove(String prefix, String target) {
        String key = prefix + target;
        redisTemplate.opsForValue().getAndDelete(key);
    }
}
