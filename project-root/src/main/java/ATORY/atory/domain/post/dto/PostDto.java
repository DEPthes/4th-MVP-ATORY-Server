package ATORY.atory.domain.post.dto;

import ATORY.atory.domain.post.entity.Post;
import ATORY.atory.domain.post.entity.PostType;
import ATORY.atory.domain.tag.dto.TagDto;
import ATORY.atory.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
@Schema(description = "게시물 DTO")
public class PostDto {

    @Schema(description = "게시물 ID", example = "1001")
    private Long id;

    @Schema(description = "게시물 제목", example = "첫 전시회")
    private String name;

    @Schema(description = "이미지 URL", example = "http://example.com/image.jpg")
    private String imageURL;

    @Schema(description = "전시회 URL", example = "http://exhibition.example.com")
    private String exhibitionURL;

    @Schema(description = "게시물 설명", example = "서울 아트페어 참가작")
    private String description;

    @Schema(description = "게시물 타입", example = "ART")
    private PostType postType;

    @Schema(description = "게시물 생성 및 수정 날짜 정보")
    private PostDateDto postDate;

    @Schema(description = "아카이브된 수", example = "5")
    private Long archived;

    @Schema(description = "게시물 태그 목록")
    private List<TagDto> tags;

    @Builder
    public PostDto(Long id, String name, String imageURL, String exhibitionURL, String description, PostType postType,PostDateDto postDate,Long archived,List<TagDto> tags) {
        this.id = id;
        this.name = name;
        this.imageURL = imageURL;
        this.exhibitionURL = exhibitionURL;
        this.description = description;
        this.postType = postType;
        this.postDate = postDate;
        this.archived = archived;
        this.tags = tags;
    }
}
