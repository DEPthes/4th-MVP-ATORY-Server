package ATORY.atory.domain.gallery.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class SmsCertification {
    private static final String PREFIX = "sms:";
    private static final int TTL_SECONDS = 180; // 3분

    private final StringRedisTemplate redisTemplate;

    public void createSmsCode(String phone, String code) {
        redisTemplate.opsForValue().set(PREFIX + phone, code, Duration.ofSeconds(TTL_SECONDS));
    }

    public String getSmsCode(String phone) {
        return redisTemplate.opsForValue().get(PREFIX + phone);
    }

    public void deleteSmsCode(String phone) {
        redisTemplate.delete(PREFIX + phone);
    }

    public boolean hasKey(String phone) {
        Boolean exists = redisTemplate.hasKey(PREFIX + phone);
        return exists != null && exists;
    }
}
