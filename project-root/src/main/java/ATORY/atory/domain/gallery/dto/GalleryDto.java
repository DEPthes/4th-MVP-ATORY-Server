package ATORY.atory.domain.gallery.dto;

import ATORY.atory.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter @Setter
public class GalleryDto {
    private Long id;
    private UserDto user;
    private String name;
    private String location;
    private Integer registrationNumber;
    private List<Object> notes;

    @Builder
    public GalleryDto(Long id, UserDto user, String name, String location,
                              Integer registrationNumber, List<Object> notes) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.location = location;
        this.registrationNumber = registrationNumber;
        this.notes = notes;
    }
}
