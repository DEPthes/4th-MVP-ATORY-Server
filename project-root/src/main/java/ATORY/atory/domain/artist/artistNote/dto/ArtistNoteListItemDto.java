package ATORY.atory.domain.artist.artistNote.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistNoteListItemDto {
    private Long noteId;
    private String thumbnailUrl;
    private ArtistSummaryDto artist;
}
