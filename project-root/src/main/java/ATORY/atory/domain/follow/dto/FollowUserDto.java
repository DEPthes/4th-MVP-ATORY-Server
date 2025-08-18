package ATORY.atory.domain.follow.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowUserDto {
    private Long userId;
    private String nickname;
    private String profileImageUrl;
}
