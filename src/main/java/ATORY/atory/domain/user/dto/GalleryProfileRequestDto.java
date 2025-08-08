package ATORY.atory.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GalleryProfileRequestDto {
    private Long userId;
    private String galleryName;
    private String registrationNumber;
    private String location;
    private String managerPhone;
    private String managerName;
    private String email;
    private String bio;
    private String address;        // 상세주소
    private String zipCode;        // 우편번호
    private String city;           // 시/도
    private String district;       // 구/군
    private String neighborhood;   // 동/읍/면
} 