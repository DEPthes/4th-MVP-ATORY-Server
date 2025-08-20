package ATORY.atory.domain.user.dto;

import ATORY.atory.global.dto.UserType;

public interface ProfileDetailDto {
    String getName();
    void setName(String name);

    UserType getUserType();
    void setUserType(UserType type);

    String getProfileImageUrl();
    void setProfileImageUrl(String url);

    String getCoverImageUrl();
    void setCoverImageUrl(String url);

    Long getFollowersCount();
    void setFollowersCount(Long count);

    Long getFollowingCount();
    void setFollowingCount(Long count);

    String getDescription();
    void setDescription(String desc);

    String getContact();
    void setContact(String contact);

    String getEmail();
    void setEmail(String email);

    Boolean getIsMe();
    void setIsMe(Boolean isMe);

    Boolean getIsFollowed();
    void setIsFollowed(Boolean isFollowed);
}
