package ATORY.atory.domain.post.repository;

import ATORY.atory.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p " +
            "JOIN PostDate pd ON p.id = pd.post.id " +
            "WHERE p.user.id = :userId AND p.postType = :postType " +
            "ORDER BY pd.createdAt DESC")
    Slice<Post> findPostsByUserId(@Param("userId") Long userId,
                                  @Param("postType") String postType,
                                  Pageable pageable);


    @Query("SELECT DISTINCT p FROM Post p " +
            "JOIN TagPost tp ON p.id = tp.post.id " +
            "JOIN Tag t ON tp.tag.id = t.id " +
            "JOIN PostDate pd ON p.id = pd.post.id " +
            "WHERE p.user.id = :userId " +
            "AND p.postType = :postType " +
            "AND t.name = :tagName " +
            "ORDER BY pd.createdAt DESC")
    Slice<Post> findPostsByUserIdAndTag(@Param("userId") Long userId,
                                               @Param("postType") String postType,
                                               @Param("tagName") String tagName,
                                               Pageable pageable);

    @Query("SELECT p FROM Post p " +
            "JOIN Archive a ON a.post = p " +
            "JOIN PostDate pd ON pd.post = p " +
            "WHERE a.user.id = :userId " +
            "ORDER BY pd.createdAt DESC")
    Slice<Post> findArchiveByUserId(@Param("userId") Long userId,
                                  Pageable pageable);

    @Query("SELECT p FROM Post p " +
            "JOIN Archive a ON a.post = p " +
            "JOIN PostDate pd ON pd.post = p " +
            "WHERE a.user.id = :userId " +
            "AND p.postType = :postType "+
            "ORDER BY pd.createdAt DESC")
    Slice<Post> findArchiveByUserIdAndPostType(@Param("userId") Long userId,
                                    @Param("postType") String postType,
                                    Pageable pageable);

}

