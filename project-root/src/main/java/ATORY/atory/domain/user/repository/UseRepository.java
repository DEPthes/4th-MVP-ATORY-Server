package ATORY.atory.domain.user.repository;

import ATORY.atory.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UseRepository extends JpaRepository<User, Long> {
}
