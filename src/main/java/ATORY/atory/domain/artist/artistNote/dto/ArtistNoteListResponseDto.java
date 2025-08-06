package ATORY.atory.domain.artist.artistNote.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ArtistNoteListResponseDto {
    private Long artistId;
    private String nickname;
    private String job; // 고정값 "작가"
    private String profileImageUrl;
    private String bio; // 작가 상태메시지
} 