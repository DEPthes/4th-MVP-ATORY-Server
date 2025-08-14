package ATORY.atory.domain.user.service;

import ATORY.atory.domain.artist.entity.Artist;
import ATORY.atory.domain.follow.service.FollowService;

import ATORY.atory.domain.post.dto.PostDto;
import ATORY.atory.domain.post.entity.PostType;
import ATORY.atory.domain.post.service.PostService;
import ATORY.atory.domain.user.dto.UserDto;
import ATORY.atory.domain.user.dto.UserWithPostDto;
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.domain.user.repository.UseRepository;
import ATORY.atory.global.exception.ErrorCode;
import ATORY.atory.global.exception.MapperException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UseRepository useRepository;
    private final FollowService followService;
    private final PostService postService;

    private boolean isLogin(User loginUser) {
        return loginUser != null;
    }

    private boolean isOwner(UserDto found,User loginUser,boolean login) {
        if(login) {
            return found.getId().equals(loginUser.getId());
        }
        else {
            return false;
        }
    }

    public UserWithPostDto getPostByIdAndTag(Long id, PostType postType, String tag, Pageable pageable, User loginUser) {

        UserDto user = getById(id);

        boolean login = isLogin(loginUser);
        boolean owner = isOwner(user,loginUser,login);

        Slice<PostDto> posts;
        if(tag!=null && !tag.isEmpty()) {
            posts = postService.getPostsByUserIdAndTag(user.getId(), postType,tag,pageable);
        }
        else {
            posts = postService.getPostsByUserId(user.getId(), postType,pageable);
        }
        return UserWithPostDto.from(user,posts,owner,login);

    }


    public UserWithPostDto getArchiveByIdAndType(Long id, PostType postType, Pageable pageable, User loginUser) {
        UserDto user = getById(id);


        boolean login = isLogin(loginUser);
        boolean owner = isOwner(user,loginUser,login);
        Slice<PostDto> posts;
        if(postType!=null)
        {
            posts= postService.getArchiveByUserIdAndType(user.getId(), postType,pageable);

        }
        else{
            posts = postService.getArchiveByUserId(user.getId(),pageable);

        }

        return UserWithPostDto.from(user,posts,owner,login);
    }

    public UserDto getById(Long Id){
        User user = useRepository.findById(Id).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));
        int follower =  followService.countFollower(user).intValue();
        int following = followService.countFollowing(user).intValue();

        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .introduction(user.getIntroduction())
                .contact(user.getContact())
                .follower(follower)
                .following(following)
                .build();
    }

}
