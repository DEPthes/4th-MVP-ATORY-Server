package ATORY.atory.domain.tag.repository;

import ATORY.atory.domain.post.entity.PostType;
import ATORY.atory.domain.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagQueryRepository extends JpaRepository<Tag, Long> {

    @Query("""
        select distinct t.name
          from TagPost tp
          join tp.post p
          join tp.tag  t
         where p.user.id  = :userId
           and p.postType = :postType
         order by t.name
    """)
    List<String> findTagNamesByUserAndPostType(@Param("userId") Long userId,
                                               @Param("postType") PostType postType);

    @Query("""
        select distinct t.name
          from TagPost tp
          join tp.post p
          join tp.tag  t
         where p.user.id  = :userId
           and str(p.postType) = :postType
         order by t.name
    """)
    List<String> findTagNamesByUserAndPostType(@Param("userId") Long userId,
                                               @Param("postType") String postType);
}