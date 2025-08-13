package ATORY.atory.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeSummaryDto {
    private boolean authenticated;
    private Long userId;
    private String role;
    private String nickname;
    private String profileImgUrl;
    private boolean hasArtistProfile;
    private Long artistId;

    public static MeSummaryDto guest() {
        return MeSummaryDto.builder()
                .authenticated(false)
                .build();
    }
}
