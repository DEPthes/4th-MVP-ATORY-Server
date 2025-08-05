package ATORY.atory.domain.post.repository;

import ATORY.atory.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    @Query("SELECT COUNT(p) FROM Post p WHERE p.user.id = :userId AND p.type = :type")
    long countByUserIdAndType(@Param("userId") Long userId, @Param("type") String type);
}
