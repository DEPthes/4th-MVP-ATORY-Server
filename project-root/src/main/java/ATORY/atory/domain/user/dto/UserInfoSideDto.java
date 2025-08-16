package ATORY.atory.domain.user.dto;

import ATORY.atory.global.dto.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class UserInfoSideDto {
    @Schema(description = "사용자 ID", example = "10")
    private Long id;

    @Schema(description = "사용자 이름", example = "김철수")
    private String username;

    @Schema(description = "프로필 사진 주소", example = "www.abcd.com")
    private String profileImageURL;

    @Schema(description = "유저 타입", example = "ARTIST, GALLERY, COLLECTOR")
    private UserType userType;

    @Builder
    public UserInfoSideDto(Long id, String username, String profileImageURL, UserType userType) {
        this.id = id;
        this.username = username;
        this.profileImageURL = profileImageURL;
        this.userType = userType;
    }
}
