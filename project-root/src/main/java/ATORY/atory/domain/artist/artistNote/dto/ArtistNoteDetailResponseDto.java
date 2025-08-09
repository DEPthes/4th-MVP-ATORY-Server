package ATORY.atory.domain.artist.artistNote.dto;

import ATORY.atory.domain.artist.artistNote.entity.ArtistNoteType;
import java.util.List;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistNoteDetailResponseDto {
    private Long noteId;
    private String year;
    private ArtistNoteType type;
    private String description;
    private ArtistSummaryDto artist;
}