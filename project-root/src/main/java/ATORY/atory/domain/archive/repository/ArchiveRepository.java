package ATORY.atory.domain.archive.repository;

import ATORY.atory.domain.archive.entity.Archive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Long> {

    @Query("SELECT COUNT(a) FROM Archive a WHERE a.post.id = :postId")
    long countByPostId(@Param("postId") Long postId);

}
