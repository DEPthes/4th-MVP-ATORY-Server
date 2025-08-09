package ATORY.atory.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyProfileDto {
    private Long userId;
    private String nickname;
    private String emailMasked;
    private String phoneMasked;
    private String profileImgUrl;
    private String bannerImgUrl;
    private String statusMessage; // User.introduction
    private String contactMasked; // User.contact 마스킹
}