package ATORY.atory.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponseDto {
    private String nickname;
    private String englishName;
    private String job;
    private String email;
    private String phone;
    private String statusMessage;
    private String profileImageUrl;
} 