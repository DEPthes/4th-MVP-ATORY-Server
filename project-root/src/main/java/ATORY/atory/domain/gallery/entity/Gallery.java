package ATORY.atory.domain.gallery.entity;

import ATORY.atory.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "Gallery")
public class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    private String name;
    private String location;
    private String registrationNumber;
    private String managerPhone;
    private String managerName;
    private String email;
    private String bio;
    private String address;        // 상세주소
    private String zipCode;        // 우편번호
    private String city;           // 시/도
    private String district;       // 구/군
    private String neighborhood;   // 동/읍/면

    @Builder
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
}
