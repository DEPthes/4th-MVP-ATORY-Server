package ATORY.atory.domain.artist.service;

import ATORY.atory.domain.artist.artistNote.dto.ArtistNoteDto;
import ATORY.atory.domain.artist.artistNote.service.ArtistNoteService;
import ATORY.atory.domain.artist.dto.ArtistDto;
import ATORY.atory.domain.artist.dto.ArtistWithArtistNoteDto;
import ATORY.atory.domain.artist.dto.ArtistWithPostDto;
import ATORY.atory.domain.artist.entity.Artist;
import ATORY.atory.domain.artist.repository.ArtistRepository;
import ATORY.atory.domain.post.dto.PostDto;
import ATORY.atory.domain.post.entity.PostType;
import ATORY.atory.domain.post.service.PostService;
import ATORY.atory.domain.user.dto.UserDto;
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.domain.user.service.UserService;
import ATORY.atory.global.exception.ErrorCode;
import ATORY.atory.global.exception.MapperException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;


@Service
@Transactional
@RequiredArgsConstructor
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final ArtistNoteService artistNoteService;
    private final UserService userService;
    private final PostService postService;

    private boolean isLogin(User loginUser) {
        return loginUser != null;
    }

    private boolean isOwner(Artist found,User loginUser,boolean login) {
        if(login) {
            return found.getUser().getId().equals(loginUser.getId());
        }
        else {
            return false;
        }
    }

    public Artist getArtistById(Long id) {
        return  artistRepository.findById(id)
                .orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));

    }

    public ArtistDto getArtistDtoById(Long id, User loginUser) {
        Artist found = getArtistById(id);
        boolean login = isLogin(loginUser);
        boolean owner = isOwner(found,loginUser,login);

        return ArtistDto.builder()
                .id(id)
                .user(userService.getById(found.getUser().getId()))
                .birth(found.getBirth())
                .educationBackground(found.getEducationBackground())
                .disclosureStatus(found.getDisclosureStatus())
                .owner(owner)
                .login(login)
                .build();

    }


    public ArtistWithArtistNoteDto getArtistNoteById(Long id,User loginUser) {
        Artist found = getArtistById(id);

        boolean login = isLogin(loginUser);
        boolean owner = isOwner(found,loginUser,login);

        return ArtistWithArtistNoteDto.builder()
                .id(found.getId())
                .username(found.getUser().getUsername())
                .artistNotes(ArtistNoteDto.from(artistNoteService.getByArtistId(id)))
                .login(login)
                .owner(owner)
                .build();
    }

    public ArtistWithPostDto getPostByIdAndTag(Long id, PostType postType, String tag, Pageable pageable,User loginUser) {
        Artist found = getArtistById(id);

        User user = found.getUser();

        boolean login = isLogin(loginUser);
        boolean owner = isOwner(found,loginUser,login);
        Slice<PostDto> posts;
        if(tag!=null && !tag.isEmpty()) {
            posts = postService.getPostsByUserIdAndTag(user.getId(), postType,tag,pageable);
        }
        else {
            posts = postService.getPostsByUserId(user.getId(), postType,pageable);
        }
        return ArtistWithPostDto.from(found,user.getUsername(),posts,owner,login);

    }

    
    public ArtistWithPostDto getArchiveByIdAndType(Long id, PostType postType, Pageable pageable, User loginUser) {
        Artist found = getArtistById(id);
        User user = found.getUser();

        boolean login = isLogin(loginUser);
        boolean owner = isOwner(found,loginUser,login);
        Slice<PostDto> posts;
        if(postType!=null)
        {
            posts= postService.getArchiveByUserIdAndType(user.getId(), postType,pageable);

        }
        else{
            posts = postService.getArchiveByUserId(user.getId(),pageable);

        }

        return ArtistWithPostDto.from(found,user.getUsername(),posts,owner,login);
    }


}
