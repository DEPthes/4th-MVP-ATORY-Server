package ATORY.atory.domain.artist.artistNote.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtistNoteDetailResponseDto {
    private Long noteId;
    private String title;
    private String content;
    private List<String> imageUrls;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ArtistProfileSummary artist;
    
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ArtistProfileSummary {
        private Long id;
        private String nickname;
        private String englishName;
        private String job;
        private String email;
        private String phone;
        private String statusMessage;
        private String profileImageUrl;
    }
} 