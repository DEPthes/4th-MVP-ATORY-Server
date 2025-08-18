package ATORY.atory.domain.follow.repository;

import ATORY.atory.domain.follow.entity.Follow;
import ATORY.atory.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    //user를 팔로우 하고있는 사람의 수
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.following = :user")
    Long countFollowers(@Param("user") User user);

    
    //user가 팔로우 하고있는 사람의 수
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.follower = :user")
    Long countFollowing(@Param("user") User user);

    // 특정 팔로우 관계 존재 여부 확인
    boolean existsByFollower_IdAndFollowing_Id(Long meId, Long id);

    // 팔로우 관계 삭제(언팔)
    void deleteByFollower_IdAndFollowing_Id(Long meId, Long id);

    // 대상 유저의 팔로워 수 조회
    long countByFollowing_Id(Long id);
}
