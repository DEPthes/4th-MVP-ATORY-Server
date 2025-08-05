package ATORY.atory.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SocialLoginResponseDto {
    private String accessToken;
    private String email;
    private String username;
    private String profileImgUrl;
    private boolean isNewUser;
} 