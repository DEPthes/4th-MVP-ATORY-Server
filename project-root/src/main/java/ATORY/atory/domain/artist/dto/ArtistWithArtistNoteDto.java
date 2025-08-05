package ATORY.atory.domain.artist.dto;

import ATORY.atory.domain.artist.artistNote.dto.ArtistNoteDto;
import ATORY.atory.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ArtistWithArtistNoteDto {

    private Long id;
    private UserDto user;
    private List<ArtistNoteDto> artistNotes;
    private String birth;
    private String educationBackground;
    private Boolean disclosureStatus;

    @Builder
    public ArtistWithArtistNoteDto(Long id, UserDto user, List<ArtistNoteDto> artistNotes, String birth, String educationBackground, Boolean disclosureStatus) {
        this.id = id;
        this.user = user;
        this.artistNotes = artistNotes;
        this.birth = birth;
        this.educationBackground = educationBackground;
        this.disclosureStatus = disclosureStatus;
    }
}
