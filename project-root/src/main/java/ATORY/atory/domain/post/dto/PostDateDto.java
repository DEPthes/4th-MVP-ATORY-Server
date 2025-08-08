package ATORY.atory.domain.post.dto;

import ATORY.atory.domain.post.entity.Post;
import ATORY.atory.domain.post.entity.PostDate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Schema(description = "게시물 생성 및 수정 날짜 정보 DTO")
public class PostDateDto {

    @Schema(description = "ID", example = "500")
    private Long id;


    @Schema(description = "생성 일시", example = "2025-08-09T12:34:56")
    private LocalDateTime createdAt;

    @Schema(description = "수정 일시", example = "2025-08-10T15:30:00")
    private LocalDateTime modifiedAt;


    @Builder
    public PostDateDto(Long id, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static PostDateDto from(PostDate postDate) {
        return PostDateDto.builder()
                .id(postDate.getId())
                .createdAt(postDate.getCreatedAt())
                .modifiedAt(postDate.getModifiedAt())
                .build();
    }
}
