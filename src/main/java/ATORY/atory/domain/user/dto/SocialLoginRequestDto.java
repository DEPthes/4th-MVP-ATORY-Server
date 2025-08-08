package ATORY.atory.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SocialLoginRequestDto {
    private String socialType;
    private String email;
    private String nickname;
    private String profileImgUrl;
    private String providerId;
} 