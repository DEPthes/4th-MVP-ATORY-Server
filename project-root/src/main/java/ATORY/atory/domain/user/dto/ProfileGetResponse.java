package ATORY.atory.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProfileGetResponse {
    private final Long id;
    private final RoleType role;

    private final String name;
    private final String birthDate;
    private final String phone;
    private final String email;
    private final String introduction;
    private final String profileImgUrl;

    private final String education;
    private final EducationVisibility educationVisibility;

    @Builder
    public ProfileGetResponse(Long id,
                              RoleType role,
                              String name,
                              String birthDate,
                              String phone,
                              String email,
                              String introduction,
                              String profileImgUrl,
                              String education,
                              EducationVisibility educationVisibility) {
        this.id = id;
        this.role = role;
        this.name = name;
        this.birthDate = birthDate;
        this.phone = phone;
        this.email = email;
        this.introduction = introduction;
        this.profileImgUrl = profileImgUrl;
        this.education = education;
        this.educationVisibility = educationVisibility;
    }
}