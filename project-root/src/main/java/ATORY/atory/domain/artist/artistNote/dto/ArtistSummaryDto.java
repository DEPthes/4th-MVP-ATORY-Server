package ATORY.atory.domain.artist.artistNote.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistSummaryDto {
    private Long id;
    private String nickname;
    private String englishName;
    private String job;
    private String phone;
    private String email;
    private String statusMessage;
    private String profileImageUrl;
}
