package ATORY.atory.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class RoleSpecificFields {
    @NotBlank(message = "education은 필수입니다.")
    private String education;

    @NotNull(message = "educationVisibility는 필수입니다.")
    private EducationVisibility educationVisibility;
}