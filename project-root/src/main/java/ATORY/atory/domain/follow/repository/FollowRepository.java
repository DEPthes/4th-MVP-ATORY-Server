package ATORY.atory.domain.follow.repository;

import ATORY.atory.domain.follow.entity.Follow;
import ATORY.atory.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    //user를 팔로우 하고있는 사람의 수
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.following = :user")
    Long countFollowers(@Param("user") User user);

    
    //user가 팔로우 하고있는 사람의 수
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.follower = :user")
    Long countFollowing(@Param("user") User user);

    // user를 팔로우하고 있는 사람들의 닉네임
    @Query("SELECT f.follower.username FROM Follow f WHERE f.following = :user")
    List<String> findFollowerNicknames(@Param("user") User user);

    // user가 팔로우하고 있는 사람들의 닉네임
    @Query("SELECT f.following.username FROM Follow f WHERE f.follower = :user")
    List<String> findFollowingNicknames(@Param("user") User user);
}
