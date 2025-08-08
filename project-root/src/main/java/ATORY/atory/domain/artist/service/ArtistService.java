package ATORY.atory.domain.artist.service;

import ATORY.atory.domain.artist.artistNote.dto.ArtistNoteDto;
import ATORY.atory.domain.artist.artistNote.service.ArtistNoteService;
import ATORY.atory.domain.artist.dto.ArtistWithArtistNoteDto;
import ATORY.atory.domain.artist.dto.ArtistWithPostDto;
import ATORY.atory.domain.artist.entity.Artist;
import ATORY.atory.domain.artist.repository.ArtistRepository;
import ATORY.atory.domain.post.dto.PostDto;
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

    public Artist getArtistById(Long id) {
        return  artistRepository.findById(id)
                .orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));

    }

    public ArtistWithArtistNoteDto getArtistNoteById(Long id,User loginUser) {
        Artist found = getArtistById(id);

        boolean login = loginUser==null;
        boolean owner;
        if(login) {
             owner = found.getUser().getId().equals(loginUser.getId());
        }
        else {
            owner=false;
        }



        return ArtistWithArtistNoteDto.builder()
                .user(userService.getById(found.getId()))
                .artistNotes(ArtistNoteDto.from(artistNoteService.getByArtistId(id)))
                .birth(found.getBirth())
                .educationBackground(found.getEducationBackground())
                .disclosureStatus(found.getDisclosureStatus())
                .owner(owner)
                .build();
    }

    public ArtistWithPostDto getPostById(Long id, String postType, Pageable pageable,User loginUser) {
        Artist found = getArtistById(id);


        User user = found.getUser();

        boolean login = loginUser!=null;
        boolean owner;
        if(login) {
            owner = user.getId().equals(loginUser.getId());
        }
        else {
            owner=false;
        }

        UserDto userDto = userService.getById(user.getId());

        Slice<PostDto> posts = postService.getPostsByUserId(user.getId(), postType,pageable);



        return ArtistWithPostDto.builder()
                .id(found.getId())
                .user(userDto)
                .birth(found.getBirth())
                .educationBackground(found.getEducationBackground())
                .disclosureStatus(found.getDisclosureStatus())
                .posts(posts.getContent())
                .hasNext(posts.hasNext())
                .build();


    }

    public ArtistWithPostDto getPostByIdAndTag(Long id, String postType, String tag, Pageable pageable,User loginUser) {
        Artist found = getArtistById(id);

        User user = found.getUser();

        boolean login = loginUser!=null;
        boolean owner;
        if(login) {
            owner = user.getId().equals(loginUser.getId());
        }
        else {
            owner=false;
        }

        UserDto userDto = userService.getById(user.getId());

        Slice<PostDto> posts = postService.getPostsByUserIdAndTag(user.getId(), postType,tag,pageable);
        return ArtistWithPostDto.builder()
                .id(found.getId())
                .user(userDto)
                .birth(found.getBirth())
                .educationBackground(found.getEducationBackground())
                .disclosureStatus(found.getDisclosureStatus())
                .posts(posts.getContent())
                .hasNext(posts.hasNext())
                .build();



    }
}
