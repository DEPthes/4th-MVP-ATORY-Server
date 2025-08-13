package ATORY.atory.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyProfileDto {
    private Long userId;
    private String nickname;
    private String emailMasked;
    private String phoneMasked;

    @JsonProperty("profileImgUrl")
    private String profileImgUrl;

    @JsonProperty("bannerImgUrl")
    private String bannerImgUrl;

    private String statusMessage;
    private String contactMasked;
}