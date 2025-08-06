package ATORY.atory.domain.gallery.dto;

import ATORY.atory.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class GalleryDto {

    private Long id;
    private String name;
    private String location;
    private Integer registrationNumber;
    private User user;

    @Builder
    public GalleryDto(Long id, String name, String location, Integer registrationNumber, User user) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.registrationNumber = registrationNumber;
        this.user = user;
    }
}
