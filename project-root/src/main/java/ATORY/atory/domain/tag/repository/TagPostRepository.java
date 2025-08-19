package ATORY.atory.domain.tag.repository;

import ATORY.atory.domain.post.entity.PostType;
import ATORY.atory.domain.tag.dto.TagDto;
import ATORY.atory.domain.tag.entity.Tag;
import ATORY.atory.domain.tag.entity.TagPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagPostRepository extends JpaRepository<TagPost, Long> {
    List<TagPost> findByPostId(Long postId);

    @Query("SELECT DISTINCT t " +
            "FROM TagPost tp " +
            "JOIN tp.post p " +
            "JOIN tp.tag t " +
            "WHERE p.user.id = :userId " +
            "AND p.postType = :postType")
    List<Tag> findTagsByUserAndPostType(@Param("userId") Long userId,
                                        @Param("postType") PostType postType);
}
