package ATORY.atory.domain.gallery.service;

import ATORY.atory.domain.gallery.repository.SmsCertification;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {
    private final SmsCertification smsCertification;

    @Value("${coolsms.api.key}") private String apiKey;
    @Value("${coolsms.api.secret}") private String apiSecret;
    @Value("${coolsms.api.sender}") private String sender;

    private DefaultMessageService messageService;

    @PostConstruct
    public void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    public SingleMessageSentResponse sendVerificationCode(String to) {
        String code = generateCode(6);
        Message message = new Message();
        message.setFrom(sender);
        message.setTo(to);
        message.setText("[인증] 인증번호는 " + code + " 입니다. (유효시간 3분)");

        // 문자 전송
        SingleMessageSentResponse res = messageService.sendOne(new SingleMessageSendingRequest(message));

        // Redis에 저장
        smsCertification.createSmsCode(to, code);

        return res;
    }

    public boolean verifyCode(String phone, String inputCode) {
        if (!smsCertification.hasKey(phone)) return false;
        String saved = smsCertification.getSmsCode(phone);
        boolean ok = saved != null && saved.equals(inputCode);
        if (ok) smsCertification.deleteSmsCode(phone);
        return ok;
    }

    private String generateCode(int digits) {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(digits);
        for (int i = 0; i < digits; i++) sb.append(rnd.nextInt(10));
        return sb.toString();
    }
}
