package ATORY.atory.domain.user.dto;

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

    @Builder
    public UserDto(Long id, String username, String googleID, String email, String introduction, String contact) {
        this.id = id;
        this.username = username;
        this.googleID = googleID;
        this.email = email;
        this.introduction = introduction;
        this.contact = contact;
    }
}
