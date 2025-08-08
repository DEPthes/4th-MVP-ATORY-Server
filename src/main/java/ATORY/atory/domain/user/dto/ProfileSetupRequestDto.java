package ATORY.atory.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ProfileSetupRequestDto {
    private Long userId;
    private String role; // "ARTIST" 또는 "COLLECTOR"
    private String name;
    private String birthDate;
    private String education;
    private Boolean isEducationPublic;
    private String phone;
    private String email;
    private String bio;
} 