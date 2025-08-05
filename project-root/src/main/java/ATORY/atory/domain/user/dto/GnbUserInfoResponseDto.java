package ATORY.atory.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GnbUserInfoResponseDto {
    private boolean isAuthenticated;
    private String nickname;
    private String profession;
    private String profileImageUrl;
    private String message; // 비로그인 시 메시지
} 