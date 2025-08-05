package ATORY.atory.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BusinessValidationResponseDto {
    private boolean isValid;
    private String businessName;
} 