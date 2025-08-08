package ATORY.atory.domain.gallery.entity;

import ATORY.atory.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "Gallery")
public class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    private String name; // 갤러리명
    private String location; // 위치 정보
    private String registrationNumber; // 사업자 등록번호
    private String managerPhone;
    private String managerName;
    private String email;
    private String bio;
    private String address;        // 상세주소
    private String zipCode;        // 우편번호
    private String city;           // 시/도
    private String district;       // 구/군
    private String neighborhood;   // 동/읍/면

    public Gallery(Long id, User user, String name, String location, String registrationNumber, String managerPhone, String managerName, String email, String bio, String address, String zipCode, String city, String district, String neighborhood) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.location = location;
        this.registrationNumber = registrationNumber;
        this.managerPhone = managerPhone;
        this.managerName = managerName;
        this.email = email;
        this.bio = bio;
        this.address = address;
        this.zipCode = zipCode;
        this.city = city;
        this.district = district;
        this.neighborhood = neighborhood;
    }

    public void updateProfile(String name, String location, String registrationNumber) {
        this.name = name;
        this.location = location;
        this.registrationNumber = registrationNumber;
    }

    // 정적 팩토리 메서드
    public static Gallery createGallery(User user, String name, String location, String registrationNumber) {
        Gallery gallery = new Gallery();
        gallery.user = user;
        gallery.name = name;
        gallery.location = location;
        gallery.registrationNumber = registrationNumber;
        return gallery;
    }
}
