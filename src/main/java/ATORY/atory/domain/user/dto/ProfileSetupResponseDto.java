package ATORY.atory.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileSetupResponseDto {
    private Long userId;
    private String role;
    private String name;
    private String message;
} 