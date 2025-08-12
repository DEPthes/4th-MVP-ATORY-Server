package ATORY.atory.domain.follow.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ATORY.atory.domain.user.entity.User;

@NoArgsConstructor
@Getter
@Setter
public class FollowDto {

    private Long id;
    private User follower;
    private User following;

    @Builder
    public FollowDto(Long id, User follower, User following){
        this.id = id;
        this.follower = follower;
        this.following = following;
    }
}
