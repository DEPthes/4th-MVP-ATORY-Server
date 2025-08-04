package ATORY.atory.domain.user.dto;

import ATORY.atory.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class UserDto {
    private Long id;
    private String username;
    private String googleID;
    private String email;
    private String introduction;
    private String contact;
    //나를 팔로우하는 사람의 수
    private int follower;
    //내가 팔로우하는 사람의 수
    private int following;

    @Builder
    public UserDto(Long id, String username, String googleID, String email, String introduction, String contact,int follower, int following) {
        this.id = id;
        this.username = username;
        this.googleID = googleID;
        this.email = email;
        this.introduction = introduction;
        this.contact = contact;
        this.follower = follower;
        this.following = following;
    }

}
