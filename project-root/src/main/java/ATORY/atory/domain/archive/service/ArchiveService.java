package ATORY.atory.domain.archive.service;

import ATORY.atory.domain.archive.repository.ArchiveRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ArchiveService {

    private final ArchiveRepository archiveRepository;

    public Long getArchiveCountByPostId(Long postId) {
        return archiveRepository.countByPostId(postId);
    }
}