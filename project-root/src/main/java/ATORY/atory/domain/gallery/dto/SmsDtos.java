package ATORY.atory.domain.gallery.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class SmsDtos {
    @Getter
    @NoArgsConstructor
    public static class SendRequest {
        private String phoneNumber;
    }

    @Getter @NoArgsConstructor
    public static class VerifyRequest {
        private String phoneNumber;
        private String code;
    }
}
