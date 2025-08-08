package ATORY.atory.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GnbUserInfoResponseDto {
    private Long userId;
    private String username;
    private String email;
    private String profileImgUrl;
} 