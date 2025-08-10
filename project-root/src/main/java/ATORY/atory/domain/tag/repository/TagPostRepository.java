package ATORY.atory.domain.tag.repository;

import ATORY.atory.domain.tag.entity.Tag;
import ATORY.atory.domain.tag.entity.TagPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagPostRepository extends JpaRepository<TagPost, Long> {
    List<TagPost> findByPostId(Long postId);

    void deleteByTagNameAndPostId(String name, Long postId);

    long countByTag(Tag tag);
}
