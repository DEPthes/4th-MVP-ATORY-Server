package ATORY.atory.domain.post.repository;

import ATORY.atory.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p JOIN p.postDate pd " +
            "WHERE p.user.id = :userId AND p.postType = :postType " +
            "ORDER BY pd.createdAt DESC")
    Page<Post> findPostsByUserIdAndPostType(@Param("userId") Long userId,
                                            @Param("postType") String postType,
                                            Pageable pageable);

}

