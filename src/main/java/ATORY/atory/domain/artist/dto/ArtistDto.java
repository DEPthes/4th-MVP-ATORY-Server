package ATORY.atory.domain.artist.dto;

import ATORY.atory.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ArtistDto {

    private Long id;
    private User user;
    private String birth;
    private String educationBackground;
    private Boolean disclosureStatus;

    @Builder
    public ArtistDto(Long id, User user, String birth, String educationBackground, Boolean disclosureStatus) {
        this.id = id;
        this.user = user;
        this.birth = birth;
        this.educationBackground = educationBackground;
        this.disclosureStatus = disclosureStatus;
    }
}
