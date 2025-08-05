package ATORY.atory.domain.artist.service;

import ATORY.atory.domain.artist.dto.ArtistNoteExistsResponseDto;
import ATORY.atory.domain.artist.entity.Artist;
import ATORY.atory.domain.artist.repository.ArtistRepository;
import ATORY.atory.domain.post.repository.PostRepository;
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.global.exception.ArtistNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final PostRepository postRepository;

    public ArtistNoteExistsResponseDto checkArtistNoteExists(Long authorId) {
        Artist artist = artistRepository.findById(authorId)
                .orElseThrow(() -> new ArtistNotFoundException("작가를 찾을 수 없습니다. ID: " + authorId));

        User user = artist.getUser();
        long noteCount = postRepository.countByUserIdAndType(user.getId(), "ARTIST_NOTE");
        boolean exists = noteCount > 0;

        return new ArtistNoteExistsResponseDto(authorId, exists, (int) noteCount);
    }
}
