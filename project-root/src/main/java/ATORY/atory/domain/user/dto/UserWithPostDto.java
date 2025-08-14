package ATORY.atory.domain.user.dto;

import ATORY.atory.domain.artist.entity.Artist;
import ATORY.atory.domain.post.dto.PostDto;
import ATORY.atory.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Slice;

import java.util.List;


@NoArgsConstructor
@Getter
@Setter
@Schema(description = "유저 정보와 게시물 리스트를 포함한 DTO")
public class UserWithPostDto {

    @Schema(description = "유저 ID", example = "1")
    private Long id;

    @Schema(description = "사용자 이름", example = "김철수")
    private String username;

    @Schema(description = "유저 게시물 목록")
    private List<PostDto> posts;

    @Schema(description = "다음 페이지 존재 여부", example = "true")
    private Boolean hasNext;

    @Schema(description = "요청자가 해당 유저 본인인지 여부", example = "false")
    private Boolean owner;

    @Schema(description = "요청자 로그인 여부", example = "false")
    private Boolean login;


    @Builder
    public UserWithPostDto(Long id, String username, List<PostDto> posts, Boolean hasNext,
                           Boolean owner, Boolean login) {
        this.id = id;
        this.username = username;
        this.posts = posts;
        this.hasNext = hasNext;
        this.owner = owner;
        this.login = login;
    }

    public static UserWithPostDto from(UserDto user,
                                       Slice<PostDto> posts, boolean owner, boolean login) {
        return UserWithPostDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .posts(posts.getContent())
                .hasNext(posts.hasNext())
                .owner(owner)
                .login(login)
                .build();
    }

}
