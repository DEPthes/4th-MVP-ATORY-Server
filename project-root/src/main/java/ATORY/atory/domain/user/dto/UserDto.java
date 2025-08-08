package ATORY.atory.domain.user.dto;

import ATORY.atory.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Schema(description = "사용자 기본 정보 DTO")
public class UserDto {
    @Schema(description = "사용자 ID", example = "10")
    private Long id;

    @Schema(description = "사용자 이름", example = "김철수")
    private String username;

    @Schema(description = "이메일 주소", example = "abc@example.com")
    private String email;

    @Schema(description = "자기소개", example = "안녕하세요, 아티스트 철수입니다.")
    private String introduction;

    @Schema(description = "연락처", example = "010-1234-5678")
    private String contact;

    @Schema(description = "나를 팔로우하는 사람의 수", example = "100")
    private int follower;

    @Schema(description = "내가 팔로우하는 사람의 수", example = "50")
    private int following;


    @Builder
    public UserDto(Long id, String username, String email, String introduction, String contact,int follower, int following) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.introduction = introduction;
        this.contact = contact;
        this.follower = follower;
        this.following = following;
    }

}
