package ATORY.atory.domain.user.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class BannerUpdateResponseDto {
    private boolean success;
    private String message;
    private String updatedBannerUrl;

    @Builder
    public BannerUpdateResponseDto(boolean success, String message, String updatedBannerUrl) {
        this.success = success;
        this.message = message;
        this.updatedBannerUrl = updatedBannerUrl;
    }

}