package ATORY.atory.domain.artist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArtistNoteExistsResponseDto {
    private Long authorId;
    private boolean exists;
    private int noteCount;
} 