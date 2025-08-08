package ATORY.atory.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthMeResponseDto {
    private Long userId;
    private String job;
    private String nickname;
    private String profileImageUrl;
} 