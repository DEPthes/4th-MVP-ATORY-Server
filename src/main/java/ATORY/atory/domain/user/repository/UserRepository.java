package ATORY.atory.domain.user.repository;

import ATORY.atory.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByGoogleID(String googleID);
    Optional<User> findByEmail(String email);
}
