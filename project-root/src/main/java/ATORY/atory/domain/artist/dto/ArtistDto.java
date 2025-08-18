package ATORY.atory.domain.artist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ArtistDto {
    private Long id;
    private String name;
    private String contact;
    private String introduction;
    private String email;
    private String profileImageURL;
    private Boolean isMine;
}
