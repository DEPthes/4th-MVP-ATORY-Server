package ATORY.atory.domain.user.service;

import ATORY.atory.domain.artist.entity.Artist;
import ATORY.atory.domain.artist.repository.ArtistRepository;
import ATORY.atory.domain.user.dto.EducationVisibility;
import ATORY.atory.domain.user.dto.MyProfileDto;
import ATORY.atory.domain.user.dto.ProfileUpdateRequest;
import ATORY.atory.global.security.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ATORY.atory.domain.user.repository.UserRepository;
import ATORY.atory.domain.user.entity.User;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;
    private final ProfileQueryService profileQueryService;
    private final JwtProvider jwtProvider;

    @Transactional
    public MyProfileDto updateMyProfile(HttpServletRequest request, ProfileUpdateRequest req) {
        Long userId = extractUserId(request);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        user.updateProfileInfo(
                req.getName(),
                req.getPhone(),
                req.getEmail(),
                req.getBirthDate(),
                req.getIntroduction()
        );

        if (req.getRoleFields() == null) {
            throw new IllegalArgumentException("roleFields가 비어 있습니다. (education, educationVisibility 필요)");
        }

        Artist artist = artistRepository.findIdByUserId(userId)
                .flatMap(artistRepository::findById)
                .orElse(null);

        boolean disclose = toDisclosureBoolean(req.getRoleFields().getEducationVisibility());

        if (artist == null) {
            artist = Artist.builder()
                    .user(user)
                    .birth(user.getBirthDate())
                    .educationBackground(req.getRoleFields().getEducation())
                    .disclosureStatus(disclose)
                    .build();
        } else {
            artist.update(
                    user.getBirthDate(),
                    req.getRoleFields().getEducation(),
                    disclose
            );
        }
        artistRepository.save(artist);

        user.completeProfile();

        return profileQueryService.myProfile(request);
    }

    private boolean toDisclosureBoolean(EducationVisibility v) {
        if (v == null) return true; // 기본 공개
        return v == EducationVisibility.PUBLIC;
    }

    private Long extractUserId(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || header.isBlank()) {
            throw new IllegalStateException("Authorization 헤더가 없습니다.");
        }

        if (!header.regionMatches(true, 0, "Bearer ", 0, 7)) {
            throw new IllegalStateException("Bearer 토큰이 아닙니다.");
        }

        String token = header.substring(7).trim();
        if (!jwtProvider.validateToken(token)) {
            throw new IllegalStateException("JWT 토큰이 유효하지 않습니다.");
        }

        String sub = jwtProvider.getUserIdFromToken(token);
        try {
            return Long.parseLong(sub);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("JWT subject가 숫자 userId 형식이 아닙니다: " + sub);
        }
    }
}