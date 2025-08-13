package ATORY.atory.domain.user.service;

import ATORY.atory.domain.artist.repository.ArtistRepository;
import ATORY.atory.domain.user.dto.MeSummaryDto;
import ATORY.atory.domain.user.dto.MyProfileDto;
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.domain.user.repository.UseRepository;
import ATORY.atory.global.exception.UserNotFoundException;
import ATORY.atory.global.security.AuthUserResolver;
import ATORY.atory.global.util.PhoneMasker;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileQueryService {

    private final UseRepository userRepository;
    private final ArtistRepository artistRepository;
    private final PhoneMasker phoneMasker;
    private final AuthUserResolver authUserResolver;

    @Value("${dev.auth.enabled:false}")
    private boolean devAuthEnabled;

    private static final String DEFAULT_PROFILE = "https://cdn.atory.app/defaults/profile.png";
    private static final String DEFAULT_BANNER  = "https://cdn.atory.app/defaults/banner.png";

    /** GNB 요약 */
    public MeSummaryDto me(HttpServletRequest req) {
        var optUserId = authUserResolver.resolveUserId(req);
        if (optUserId.isEmpty()) return MeSummaryDto.guest();

        Long userId = optUserId.get();

        // DEV 모드 (DB 없이 동작)
        if (devAuthEnabled) {
            return MeSummaryDto.builder()
                    .authenticated(true)
                    .userId(userId)
                    .role("USER")
                    .nickname("로컬유저")
                    .profileImgUrl(DEFAULT_PROFILE)
                    .hasArtistProfile(false)
                    .artistId(null)
                    .build();
        }

        // 실제 DB
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("USER_NOT_FOUND"));

        boolean isArtist = artistRepository.existsByUser_Id(userId);
        Long artistId = isArtist ? artistRepository.findIdByUserId(userId).orElse(null) : null;
        String role = isArtist ? "AUTHOR" : "USER";

        return MeSummaryDto.builder()
                .authenticated(true)
                .userId(user.getId())
                .role(role)
                .nickname(nvl(user.getUsername(), "사용자"))
                .profileImgUrl(nvl(user.getProfileImgUrl(), DEFAULT_PROFILE))
                .hasArtistProfile(isArtist)
                .artistId(artistId)
                .build();
    }

    /** 마이페이지 상세 */
    public MyProfileDto myProfile(HttpServletRequest req) {
        Long userId = authUserResolver.resolveUserId(req)
                .orElseThrow(() -> new UserNotFoundException("UNAUTHORIZED"));

        // DEV 모드
        if (devAuthEnabled) {
            String devPhone = "01012345678";
            return MyProfileDto.builder()
                    .userId(userId)
                    .nickname("로컬유저")
                    .emailMasked(maskEmail("local@example.com"))
                    .phoneMasked(phoneMasker.mask(devPhone))
                    .profileImgUrl(DEFAULT_PROFILE)
                    .bannerImgUrl(DEFAULT_BANNER)
                    .statusMessage("로컬 상태메시지입니다.")
                    .contactMasked(phoneMasker.mask(devPhone))
                    .build();
        }

        // 실제 DB
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("USER_NOT_FOUND"));

        return MyProfileDto.builder()
                .userId(user.getId())
                .nickname(nvl(user.getUsername(), "사용자"))
                .emailMasked(maskEmail(user.getEmail()))
                .phoneMasked(phoneMasker.mask(user.getPhone()))
                .profileImgUrl(nvl(user.getProfileImgUrl(), DEFAULT_PROFILE))
                .bannerImgUrl(DEFAULT_BANNER)
                .statusMessage(nvl(user.getIntroduction(), ""))
                .contactMasked(phoneMasker.mask(user.getContact()))
                .build();
    }

    // ===== 유틸 =====
    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) return email;
        String[] parts = email.split("@", 2);
        return maskMid(parts[0]) + "@" + maskMid(parts[1]);
    }

    private String maskMid(String s) {
        if (s == null || s.isEmpty()) return s;
        if (s.length() <= 2) return s.charAt(0) + "*";
        int mid = s.length() / 2;
        return s.substring(0, Math.max(1, mid - 1)) + "**" + s.substring(mid + 1);
    }

    private String nvl(String v, String def) {
        return (v == null || v.isBlank()) ? def : v;
    }
}