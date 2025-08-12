package ATORY.atory.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoogleLoginResponseDto {
    private final String token;
    private final String email;
    private final String username;
    private final String profileImgUrl;
    private final boolean isNewUser;
}