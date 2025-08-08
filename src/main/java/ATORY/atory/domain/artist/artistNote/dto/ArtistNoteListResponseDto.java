package ATORY.atory.domain.artist.artistNote.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtistNoteListResponseDto {
    private Long noteId;
    private String thumbnailUrl;
    private ArtistInfo artist;
    
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ArtistInfo {
        private Long id;
        private String nickname;
        private String englishName;
        private String job;
        private String phone;
        private String email;
        private String statusMessage;
    }
} 