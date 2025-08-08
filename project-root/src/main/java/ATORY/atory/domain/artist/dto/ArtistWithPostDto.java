package ATORY.atory.domain.artist.dto;

import ATORY.atory.domain.artist.entity.Artist;
import ATORY.atory.domain.post.dto.PostDto;
import ATORY.atory.domain.user.dto.UserDto;
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
@Schema(description = "작가 정보와 게시물 리스트를 포함한 DTO")
public class ArtistWithPostDto {

    @Schema(description = "작가 ID", example = "1")
    private Long id;

    @Schema(description = "작가의 사용자 정보")
    private UserDto user;

    @Schema(description = "작가 생년월일", example = "1980-01-01")
    private String birth;

    @Schema(description = "학력 정보", example = "명지대학교 미술학과 졸업")
    private String educationBackground;

    @Schema(description = "공개 여부", example = "true")
    private Boolean disclosureStatus;

    @Schema(description = "작가 게시물 목록")
    private List<PostDto> posts;

    @Schema(description = "다음 페이지 존재 여부", example = "true")
    private Boolean hasNext;

    @Schema(description = "요청자가 해당 작가 본인인지 여부", example = "false")
    private Boolean owner;

    @Schema(description = "요청자 로그인 여부", example = "false")
    private Boolean login;


    @Builder
    public ArtistWithPostDto(Long id, UserDto user, String birth, String educationBackground,
                             Boolean disclosureStatus,List<PostDto> posts, Boolean hasNext,
                             Boolean owner, Boolean login, Boolean hashtag) {
        this.id = id;
        this.user = user;
        this.birth = birth;
        this.educationBackground = educationBackground;
        this.disclosureStatus = disclosureStatus;
        this.posts = posts;
        this.hasNext = hasNext;
        this.owner = owner;
        this.login = login;
    }

    public static ArtistWithPostDto from(Artist artist, UserDto userDto,
                                         Slice<PostDto> posts,boolean owner,boolean login) {
        return ArtistWithPostDto.builder()
                .id(artist.getId())
                .user(userDto)
                .birth(artist.getBirth())
                .educationBackground(artist.getEducationBackground())
                .disclosureStatus(artist.getDisclosureStatus())
                .posts(posts.getContent())
                .hasNext(posts.hasNext())
                .owner(owner)
                .login(login)
                .build();
    }

}
