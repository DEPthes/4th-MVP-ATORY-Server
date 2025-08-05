package ATORY.atory.domain.tag.dto;

import ATORY.atory.domain.tag.entity.Tag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class TagDto {
    private Long id;
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
}
