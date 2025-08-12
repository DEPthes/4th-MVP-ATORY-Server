package ATORY.atory.domain.user.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class ProfileUpdateRequest {

    @NotBlank(message = "name은 필수입니다.")
    private String name;

    private String birthDate;

    @NotBlank(message = "phone은 필수입니다.")
    @Pattern(regexp = "^\\d{8,15}$", message = "phone은 숫자만 8~15자리여야 합니다.")
    private String phone;

    @NotBlank(message = "email은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @Size(min = 60, max = 500, message = "소개글은 60~500자여야 합니다.")
    private String introduction;

    private RoleType role = RoleType.ARTIST;

    @Valid
    private RoleSpecificFields roleFields;
}