package ATORY.atory.domain.post.repository;

import ATORY.atory.domain.post.entity.PostDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostDateRepository extends JpaRepository<PostDate, Long> {
}
