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
    private Integer registrationNumber;

    @Builder
    public Gallery(Long id, User user, String name, String location, Integer registrationNumber) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.location = location;
        this.registrationNumber = registrationNumber;
    }
}
