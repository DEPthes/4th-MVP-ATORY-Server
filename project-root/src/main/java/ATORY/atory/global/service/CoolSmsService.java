package ATORY.atory.global.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoolSmsService implements SmsService {

    private final RestTemplate restTemplate;

    @Value("${sms.coolsms.api-key}")
    private String apiKey;

    @Value("${sms.coolsms.api-secret}")
    private String apiSecret;

    @Value("${sms.coolsms.from}")
    private String from;

    @Override
    public boolean sendSms(String phone, String message) {
        try {
            // 전화번호 형식 정리 (하이픈 제거)
            String cleanPhone = phone.replaceAll("[^0-9]", "");
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("to", cleanPhone);
            requestBody.put("from", from);
            requestBody.put("text", message);
            requestBody.put("type", "SMS");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + getAccessToken());

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            restTemplate.postForEntity("https://api.coolsms.co.kr/messages/v4/send", request, Map.class);
            
            log.info("SMS 발송 성공: {} -> {}", cleanPhone, message);
            return true;

        } catch (Exception e) {
            log.error("SMS 발송 실패: {} - {}", phone, e.getMessage(), e);
            return false;
        }
    }

    private String getAccessToken() {
        // CoolSMS API 키를 직접 사용 (Bearer 토큰 방식)
        // 실제로는 OAuth2 토큰을 발급받아 사용해야 함
        return apiKey;
    }
} 