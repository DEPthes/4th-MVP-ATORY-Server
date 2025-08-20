package ATORY.atory.domain.gallery.dto;

import ATORY.atory.domain.user.dto.ProfileDetailDto;
import ATORY.atory.global.dto.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class GalleryDetailDto implements ProfileDetailDto {
    private Long GalleryID;
    private String name;
    private UserType userType;
    private String profileImageUrl;
    private String coverImageUrl;
    private Long followersCount;
    private Long followingCount;
    private String description;
    private String location;
    private String galleryName;
    private String contact;
    private String email;

    @Schema(description = "조회한 프로필이 나 인지 아닌지")
    private Boolean isMe;
    @Schema(description = "팔로우 여부")
    private Boolean isFollowed;
}
