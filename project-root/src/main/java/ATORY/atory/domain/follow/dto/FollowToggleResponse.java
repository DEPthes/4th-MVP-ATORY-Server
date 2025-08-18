package ATORY.atory.domain.follow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "팔로우/언팔로우 반환 DTO")
public class FollowToggleResponse {
    @Schema(description = "팔로우 대상 유저 ID", example = "3")
    private Long targetUserId; // 유저ID

    @Schema(description = "현재 팔로우 상태 (true면 팔로우 중)", example = "true")
    private boolean following; //토글 후 내가 팔로우중인지 여부

    @Schema(description = "대상 유저의 팔로워 수", example = "12")
    private long targetFollowerCount; // 대상 유저의 최신 팔로워 수
}
