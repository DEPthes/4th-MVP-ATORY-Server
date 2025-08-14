package ATORY.atory.global.Oauth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthCodeRequestDto {
    @Schema(description = "Google Auth Code")
    private String code; //프론트에서 받을 Google Auth Code
}
