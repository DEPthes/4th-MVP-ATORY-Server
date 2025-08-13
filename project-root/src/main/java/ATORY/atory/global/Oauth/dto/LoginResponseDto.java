package ATORY.atory.global.Oauth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponseDto {

    @Schema(description = "Google Sub 고유값")
    private String googleID;

    @Schema(description = "유저 DB ID")
    private Long UserID;

    @Schema(description = "이미 가입된 회원 이면 true 아니면 false")
    private Boolean isMember;
}
