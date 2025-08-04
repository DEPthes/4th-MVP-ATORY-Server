package ATORY.atory.domain.artist.artistNote.dto;

import ATORY.atory.domain.artist.artistNote.entity.ArtistNote;
import ATORY.atory.domain.artist.artistNote.entity.ArtistNoteType;
import ATORY.atory.domain.artist.entity.Artist;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
public class ArtistNoteDto {
    private Long id;
    private Artist artist;
    private ArtistNoteType artistNoteType;
    private String year;
    private String description;

    @Builder
    public ArtistNoteDto(Long id, Artist artist, ArtistNoteType artistNoteType, String year, String description) {
        this.id = id;
        this.artist = artist;
        this.artistNoteType = artistNoteType;
        this.year = year;
        this.description = description;
    }

    public static ArtistNoteDto from(ArtistNote artistNote) {
        return ArtistNoteDto.builder()
                .id(artistNote.getId())
                .artist(artistNote.getArtist())
                .artistNoteType(artistNote.getArtistNoteType())
                .year(artistNote.getYear())
                .description(artistNote.getDescription())
                .build();
    }

    public static List<ArtistNoteDto> from(List<ArtistNote> artistNotes) {
        return artistNotes.stream()
                .map(ArtistNoteDto::from)
                .collect(Collectors.toList());
    }
}
