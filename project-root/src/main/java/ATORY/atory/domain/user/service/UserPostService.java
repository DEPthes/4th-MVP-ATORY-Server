package ATORY.atory.domain.user.service;

import ATORY.atory.domain.follow.service.FollowService;

import ATORY.atory.domain.post.dto.PostDto;
import ATORY.atory.domain.post.entity.PostType;
import ATORY.atory.domain.post.service.PostService;
import ATORY.atory.domain.user.dto.CustomUserDetails;
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
public class UserPostService {

    private final PostService postService;
    private final UserService userService;

    private boolean isLogin(CustomUserDetails loginUser) {
        return loginUser != null;
    }

    private boolean isOwner(User found,CustomUserDetails loginUser,boolean login) {
        if(login) {
            return found.getId().equals(loginUser.getUser().getId());
        }
        else {
            return false;
        }
    }

    public UserWithPostDto getPostByIdAndTag(Long id, PostType postType, String tag, Pageable pageable, CustomUserDetails loginUser) {

        User user = userService.getUserEntityById(id);

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


    public UserWithPostDto getArchiveByIdAndType(Long id, PostType postType, Pageable pageable, CustomUserDetails loginUser) {
        User user = userService.getUserEntityById(id);


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





}
