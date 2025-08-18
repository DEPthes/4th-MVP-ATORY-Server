package ATORY.atory.domain.archive.service;

import ATORY.atory.domain.archive.entity.Archive;
import ATORY.atory.domain.archive.repository.ArchiveRepository;
import ATORY.atory.domain.post.entity.Post;
import ATORY.atory.domain.post.repository.PostRepository;
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.domain.user.repository.UserRepository;
import ATORY.atory.global.exception.ErrorCode;
import ATORY.atory.global.exception.MapperException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ArchiveService {
    private final ArchiveRepository archiveRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public Long getArchiveCountByPostId(Long postId) {
        return archiveRepository.countByPostId(postId);
    }

    public Boolean switchArchive(Long postId, String googleID) {
        User user = userRepository.findByGoogleID(googleID).orElseThrow(() -> new MapperException(ErrorCode.SER_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(() -> new MapperException(ErrorCode.INTERNAL_SERVER_ERROR));

        Optional<Archive> archiveOpt = archiveRepository.findByUserIdAndPostId(user.getId(), postId);

        if (archiveOpt.isPresent()) {
            // 이미 있으면 삭제
            archiveRepository.delete(archiveOpt.get());
            return false;
        } else {
            archiveRepository.save(Archive.builder()
                            .user(user)
                            .post(post)
                    .build());

            return true; // true = 새로 저장됨
        }
    }
}
