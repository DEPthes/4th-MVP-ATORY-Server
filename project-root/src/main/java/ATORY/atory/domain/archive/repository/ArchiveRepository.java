package ATORY.atory.domain.archive.repository;

import ATORY.atory.domain.archive.entity.Archive;
import ATORY.atory.domain.post.entity.Post;
import ATORY.atory.domain.post.entity.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArchiveRepository extends JpaRepository<Archive, Long> {

    @Query("select count(a) from Archive a where a.post.id = :postId")
    long countByPostId(@Param("postId") Long postId);

    @Query("""
           select a.post
             from Archive a
             join a.post p
            where a.user.id = :userId
         order by a.id desc
           """)
    Page<Post> findArchivedPostsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("""
           select a.post
             from Archive a
             join a.post p
            where a.user.id = :userId
              and p.postType = :postType
         order by a.id desc
           """)
    Page<Post> findArchivedPostsByUserIdAndType(@Param("userId") Long userId,
                                                @Param("postType") PostType postType,
                                                Pageable pageable);
}
