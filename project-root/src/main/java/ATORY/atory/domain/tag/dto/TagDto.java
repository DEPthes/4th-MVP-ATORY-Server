package ATORY.atory.domain.tag.dto;

import ATORY.atory.domain.tag.entity.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "태그 DTO")
public class TagDto {

    @Schema(description = "태그 ID", example = "10")
    private Long id;

    @Schema(description = "태그 이름", example = "현대미술")
    private String name;

    @Builder
    public TagDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static TagDto from(Tag tag) {
        return TagDto.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }

    public static List<TagDto> from(List<Tag> tags) {
        return tags.stream().map(TagDto::from).collect(Collectors.toList());
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagDto)) return false;

        TagDto tagDto = (TagDto) o;
        return name != null && name.equals(tagDto.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

}
