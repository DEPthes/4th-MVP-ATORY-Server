package ATORY.atory.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileUpdateRequestDto {

    @NotBlank(message = "이름은 필수입니다.")
    private String username;

    @NotBlank(message = "연락처는 필수입니다.")
    @Pattern(regexp = "^[0-9]+$", message = "연락처는 숫자만 입력 가능합니다.")
    private String phone;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    private String introduction;

    private String profileImgUrl;

    private String coverImgUrl;
} 