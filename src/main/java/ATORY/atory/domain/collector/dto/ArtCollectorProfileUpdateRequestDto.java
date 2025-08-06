package ATORY.atory.domain.collector.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ArtCollectorProfileUpdateRequestDto {

    @NotNull(message = "생년월일은 필수입니다.")
    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    private LocalDate birthDate;

    @NotBlank(message = "학력은 필수입니다.")
    private String educationBackground;

    @NotNull(message = "학력 공개 여부는 필수입니다.")
    private Boolean isEducationPublic;
} 