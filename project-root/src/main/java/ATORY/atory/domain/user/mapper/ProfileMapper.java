package ATORY.atory.domain.user.mapper;

import ATORY.atory.domain.artist.entity.Artist;
import ATORY.atory.domain.user.dto.EducationVisibility;
import ATORY.atory.domain.user.dto.ProfileGetResponse;
import ATORY.atory.domain.user.dto.RoleType;
import ATORY.atory.domain.user.entity.User;

public class ProfileMapper {

    public static ProfileGetResponse toArtistResponse(User user, Artist artist) {
        return ProfileGetResponse.builder()
                .id(user.getId())
                .role(RoleType.ARTIST)
                .name(user.getUsername())
                .birthDate(user.getBirthDate())
                .phone(user.getPhone())
                .email(user.getEmail())
                .introduction(user.getIntroduction())
                .profileImgUrl(user.getProfileImgUrl())
                .education(artist != null ? artist.getEducationBackground() : null)
                .educationVisibility(artist != null
                        ? (Boolean.TRUE.equals(artist.getDisclosureStatus())
                        ? EducationVisibility.PUBLIC
                        : EducationVisibility.PRIVATE)
                        : null)
                .build();
    }

    public static boolean toDisclosureBoolean(EducationVisibility v) {
        if (v == null) return true;
        return v == EducationVisibility.PUBLIC;
    }
}
