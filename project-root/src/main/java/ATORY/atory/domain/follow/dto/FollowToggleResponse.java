package ATORY.atory.domain.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FollowToggleResponse {
    private Long targetUserId; // 유저ID
    private boolean isFollowing; //토글 후 내가 팔로우중인지 여부
    private long targetFollowerCount; // 대상 유저의 최신 팔로워 수
}
