package ATORY.atory.domain.user.service;

import ATORY.atory.global.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationService {

    private final SmsService smsService;
    
    // 임시 저장소 (Redis 대신 사용, 실제 운영에서는 Redis로 교체 필요)
    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();
    private final Map<String, Long> codeTimestamps = new ConcurrentHashMap<>();
    
    private static final int CODE_LENGTH = 6;
    private static final long CODE_EXPIRY_TIME = 5 * 60 * 1000; // 5분

    public String sendVerificationCode(String phone) {
        try {
            // 전화번호 형식 정리 (하이픈 제거)
            String cleanPhone = phone.replaceAll("[^0-9]", "");
            
            // 6자리 랜덤 인증번호 생성
            String code = generateRandomCode();
            
            // 인증번호 저장 (5분 만료)
            verificationCodes.put(cleanPhone, code);
            codeTimestamps.put(cleanPhone, System.currentTimeMillis());
            
            // SMS 발송
            boolean smsSent = smsService.sendVerificationCode(cleanPhone, code);
            
            if (smsSent) {
                log.info("인증번호 발송 성공: {} -> {}", cleanPhone, code);
                return code;
            } else {
                log.error("SMS 발송 실패: {}", cleanPhone);
                // SMS 발송 실패 시 저장된 코드 삭제
                verificationCodes.remove(cleanPhone);
                codeTimestamps.remove(cleanPhone);
                throw new RuntimeException("SMS 발송에 실패했습니다.");
            }
            
        } catch (Exception e) {
            log.error("인증번호 발송 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("인증번호 발송에 실패했습니다.", e);
        }
    }

    public boolean verifyCode(String phone, String code) {
        try {
            // 전화번호 형식 정리 (하이픈 제거)
            String cleanPhone = phone.replaceAll("[^0-9]", "");
            
            // 저장된 인증번호 조회
            String storedCode = verificationCodes.get(cleanPhone);
            Long timestamp = codeTimestamps.get(cleanPhone);
            
            if (storedCode == null || timestamp == null) {
                log.warn("인증번호가 만료되었거나 존재하지 않음: {}", cleanPhone);
                return false;
            }
            
            // 인증번호 만료 확인
            if (System.currentTimeMillis() - timestamp > CODE_EXPIRY_TIME) {
                verificationCodes.remove(cleanPhone);
                codeTimestamps.remove(cleanPhone);
                log.warn("인증번호 만료: {}", cleanPhone);
                return false;
            }
            
            // 인증번호 일치 확인
            if (storedCode.equals(code)) {
                // 인증 성공 시 저장된 코드 삭제
                verificationCodes.remove(cleanPhone);
                codeTimestamps.remove(cleanPhone);
                log.info("인증번호 확인 성공: {}", cleanPhone);
                return true;
            } else {
                log.warn("인증번호 불일치: {} (입력: {}, 저장: {})", cleanPhone, code, storedCode);
                return false;
            }
            
        } catch (Exception e) {
            log.error("인증번호 확인 중 오류 발생: {}", e.getMessage(), e);
            return false;
        }
    }

    private String generateRandomCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }
        
        return code.toString();
    }
} 