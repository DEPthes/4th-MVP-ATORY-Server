package ATORY.atory.domain.artist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ArtistProfileResponseDto {
    private Long authorId;
    private String nickname;
    private String profileImageUrl;
    private String profession; // "작가"
    private String statusMessage;
    private String email;
    private String contact;
    private String bannerImageUrl;
    private boolean hasNotes; // 작가노트 게시 여부
    private String englishName;
    private boolean isContactPublic; // 연락처 공개 여부
    private boolean isEmailPublic; // 이메일 공개 여부
} 