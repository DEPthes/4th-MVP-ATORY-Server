package ATORY.atory.domain.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String googleID;
    private String email;
    private String introduction;
    private String contact;

    @Builder
    public User(String username, String googleID, String email, String introduction, String contact) {
        this.username = username;
        this.googleID = googleID;
        this.email = email;
        this.introduction = introduction;
        this.contact = contact;
    }
}
