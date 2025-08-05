package ATORY.atory.global.service;

public interface SmsService {
    /**
     * SMS 메시지를 발송합니다.
     * @param phone 수신자 전화번호 (하이픈 제거된 숫자만)
     * @param message 발송할 메시지 내용
     * @return 발송 성공 여부
     */
    boolean sendSms(String phone, String message);
    
    /**
     * 인증번호 SMS를 발송합니다.
     * @param phone 수신자 전화번호
     * @param code 인증번호
     * @return 발송 성공 여부
     */
    default boolean sendVerificationCode(String phone, String code) {
        String message = String.format("[ATORY] 인증번호는 %s입니다. 5분 이내에 입력해주세요.", code);
        return sendSms(phone, message);
    }
} 