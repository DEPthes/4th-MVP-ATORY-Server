package ATORY.atory.domain.post.repository;

import ATORY.atory.domain.post.entity.Post;
import ATORY.atory.domain.post.entity.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
           select p
             from Post p
            where p.user.id = :userId
              and p.postType = :postType
         order by p.id desc
           """)
    Page<Post> findPostsByUserIdAndPostType(@Param("userId") Long userId,
                                            @Param("postType") PostType postType,
                                            Pageable pageable);
}