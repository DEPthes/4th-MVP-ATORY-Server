package ATORY.atory.domain.follow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "팔로워/팔로잉 유저 정보 DTO")
public class FollowUserDto {
    @Schema(description = "유저 ID", example = "7")
    private Long userId;

    @Schema(description = "유저 닉네임", example = "짱구")
    private String nickname;

    @Schema(description = "유저 프로필 이미지 URL", example = "https://cdn.example.com/profile.jpg")
    private String profileImageUrl;
}
