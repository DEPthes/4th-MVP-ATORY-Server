package ATORY.atory.domain.user.repository;

import ATORY.atory.domain.user.entity.User;
import ATORY.atory.global.dto.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByGoogleID(String googleID);

    @Query("""
        SELECT u
        FROM User u
        LEFT JOIN Follow f ON f.following = u
        WHERE u.userType = :userType
        GROUP BY u
        ORDER BY COUNT(f.id) DESC
        """)
    Page<User> findTopByUserTypeOrderByFollowerCountDesc(
            @Param("userType") UserType userType,
            Pageable pageable
    );
}
