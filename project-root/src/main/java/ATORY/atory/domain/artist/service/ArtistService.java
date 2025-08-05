package ATORY.atory.domain.artist.service;

import ATORY.atory.domain.artist.artistNote.dto.ArtistNoteDto;
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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final UserService userService;
    private final PostService postService;

    public Artist findArtistById(Long id) {
        return  artistRepository.findById(id)
                .orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));

    }

    public ArtistWithArtistNoteDto findArtistNoteById(Long id) {
        Artist found = findArtistById(id);

        return ArtistWithArtistNoteDto.builder()
                .id(found.getId())
                .user(userService.findById(found.getId()))
                .artistNotes(ArtistNoteDto.from(found.getArtistNotes()))
                .birth(found.getBirth())
                .educationBackground(found.getEducationBackground())
                .disclosureStatus(found.getDisclosureStatus())
                .build();
    }

    public ArtistWithPostDto findPostById(Long id, String postType, Pageable pageable) {
        Artist found = findArtistById(id);

        User user = found.getUser();

        UserDto userDto = userService.findById(user.getId());

        Page<PostDto> posts = postService.findPostsByUserId(user.getId(), postType,pageable);
        return ArtistWithPostDto.builder()
                .id(found.getId())
                .user(userDto)
                .birth(found.getBirth())
                .educationBackground(found.getEducationBackground())
                .disclosureStatus(found.getDisclosureStatus())
                .post(posts)
                .build();


    }

    public ArtistWithPostDto findPostByIdAndTag(Long id, String postType, String tag, Pageable pageable) {
        Artist found = findArtistById(id);

        User user = found.getUser();

        UserDto userDto = userService.findById(user.getId());

        Page<PostDto> posts = postService.findPostsByUserIdAndTag(user.getId(), postType,tag,pageable);
        return ArtistWithPostDto.builder()
                .id(found.getId())
                .user(userDto)
                .birth(found.getBirth())
                .educationBackground(found.getEducationBackground())
                .disclosureStatus(found.getDisclosureStatus())
                .post(posts)
                .build();



    }
}
