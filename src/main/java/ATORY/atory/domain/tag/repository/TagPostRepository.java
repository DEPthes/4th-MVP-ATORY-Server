package ATORY.atory.domain.tag.repository;

import ATORY.atory.domain.tag.entity.TagPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagPostRepository extends JpaRepository<TagPost, Long> {
}
