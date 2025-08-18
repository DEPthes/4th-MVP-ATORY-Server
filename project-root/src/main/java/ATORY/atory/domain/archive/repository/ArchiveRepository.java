package ATORY.atory.domain.archive.repository;

import ATORY.atory.domain.archive.entity.Archive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Long> {

    @Query("SELECT COUNT(a) FROM Archive a WHERE a.post.id = :postId")
    long countByPostId(@Param("postId") Long postId);

    @Query("SELECT COUNT(a) > 0 FROM Archive a WHERE a.user.id = :userId AND a.post.id = :postId")
    boolean existsByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId);

    Optional<Archive> findByUserIdAndPostId(Long userId, Long postId);

    List<Archive> findByPostId(Long postId);
}
