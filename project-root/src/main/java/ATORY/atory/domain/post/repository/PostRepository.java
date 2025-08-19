package ATORY.atory.domain.post.repository;

import ATORY.atory.domain.post.entity.Post;
import ATORY.atory.domain.post.entity.PostType;
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

    // 전체 조회 (최신순)
    @Query("""
        SELECT p
        FROM Post p
        JOIN FETCH p.user u
        JOIN PostDate pd ON pd.post = p
        WHERE p.postType = :postType
        ORDER BY pd.createdAt DESC
    """)
    Page<Post> findAllOrderByCreatedAtDesc(@Param("postType") PostType postType,
                                           Pageable pageable);

    // 태그별 조회 (최신순)
    @Query("""
        SELECT DISTINCT p
        FROM Post p
        JOIN FETCH p.user u
        JOIN PostDate pd ON pd.post = p
        JOIN TagPost tp ON tp.post = p
        JOIN Tag t ON tp.tag = t
        WHERE t.name = :tagName
          AND p.postType = :postType
        ORDER BY pd.createdAt DESC
    """)
    Page<Post> findByTagNameOrderByCreatedAtDesc(
            @Param("tagName") String tagName,
            @Param("postType") PostType postType,
            Pageable pageable
    );

    // 전체 조회 (검색 + 최신순)
    @Query("""
    SELECT p
    FROM Post p
    JOIN FETCH p.user u
    JOIN PostDate pd ON pd.post = p
    WHERE p.postType = :postType
      AND (
          :keyword IS NULL
          OR :keyword = ''
          OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
          OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
      )
    ORDER BY pd.createdAt DESC
""")
    Page<Post> searchAllOrderByCreatedAtDesc(@Param("postType") PostType postType,
                                             @Param("keyword") String keyword,
                                             Pageable pageable);

    // 태그별 조회 (검색 + 최신순)
    @Query("""
    SELECT DISTINCT p
    FROM Post p
    JOIN FETCH p.user u
    JOIN PostDate pd ON pd.post = p
    JOIN TagPost tp ON tp.post = p
    JOIN Tag t ON tp.tag = t
    WHERE t.name = :tagName
      AND p.postType = :postType
      AND (
          :keyword IS NULL
          OR :keyword = ''
          OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
          OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
      )
    ORDER BY pd.createdAt DESC
""")
    Page<Post> searchByTagNameOrderByCreatedAtDesc(@Param("tagName") String tagName,
                                                   @Param("postType") PostType postType,
                                                   @Param("keyword") String keyword,
                                                   Pageable pageable);

    // 특정 유저 + 전체 조회 (최신순)
    @Query("""
    SELECT p
    FROM Post p
    JOIN FETCH p.user u
    JOIN PostDate pd ON pd.post = p
    WHERE p.postType = :postType
      AND u.id = :userId
    ORDER BY pd.createdAt DESC
""")
    Page<Post> findByUserOrderByCreatedAtDesc(
            @Param("userId") Long userId,
            @Param("postType") PostType postType,
            Pageable pageable
    );

    // 특정 유저 + 태그별 조회 (최신순)
    @Query("""
    SELECT DISTINCT p
    FROM Post p
    JOIN FETCH p.user u
    JOIN PostDate pd ON pd.post = p
    JOIN TagPost tp ON tp.post = p
    JOIN Tag t ON tp.tag = t
    WHERE t.name = :tagName
      AND p.postType = :postType
      AND u.id = :userId
    ORDER BY pd.createdAt DESC
""")
    Page<Post> findByUserAndTagNameOrderByCreatedAtDesc(
            @Param("userId") Long userId,
            @Param("tagName") String tagName,
            @Param("postType") PostType postType,
            Pageable pageable
    );
}

