package ATORY.atory.domain.user.service;

import ATORY.atory.domain.artist.entity.Artist;
import ATORY.atory.domain.artist.repository.ArtistRepository;
import ATORY.atory.domain.collector.entity.Collector;
import ATORY.atory.domain.collector.repository.CollectorRepository;
import ATORY.atory.domain.user.dto.GoogleLoginResponseDto;
import ATORY.atory.domain.user.dto.ProfileSetupRequestDto;
import ATORY.atory.domain.user.dto.ProfileSetupResponseDto;
import ATORY.atory.domain.user.dto.SocialLoginRequestDto;
import ATORY.atory.domain.user.dto.SocialLoginResponseDto;
import ATORY.atory.domain.user.dto.UserDto;
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.domain.user.repository.UserRepository;
import ATORY.atory.global.exception.InvalidRoleException;
import ATORY.atory.global.exception.UserNotFoundException;
import ATORY.atory.global.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;
    private final CollectorRepository collectorRepository;
    private final JwtProvider jwtProvider;

    public UserDto findById(Long userId) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("USER_NOT_FOUND"));

        return UserDto.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .build();
    }

    public GoogleLoginResponseDto googleLogin(String code) {
        String accessToken = getAccessToken(code);
        JSONObject userInfo = getUserInfo(accessToken);

        String googleId = userInfo.optString("sub", null);
        String email    = userInfo.optString("email", null);
        String name     = userInfo.optString("name", "사용자");
        String picture  = userInfo.optString("picture", null);

        if (googleId == null || email == null) {
            throw new IllegalStateException("Google userinfo 응답에 sub/email이 없습니다.");
        }

        boolean isNewUser = false;

        User user = userRepository.findByGoogleID(googleId).orElse(null);
        if (user == null) {
            isNewUser = true;
            user = User.builder()
                    .username(name)
                    .email(email)
                    .googleID(googleId)
                    .isSocial(true)
                    .provider("google")
                    .profileImgUrl(picture)
                    .phone(null)
                    .birthDate(null)
                    .isProfileCompleted(false)
                    .build();
            user = userRepository.save(user);
        } else {
            // 프로필 이미지가 바뀌었으면 업데이트
            if (picture != null && (user.getProfileImgUrl() == null || !picture.equals(user.getProfileImgUrl()))) {
                user.updateProfileImgUrl(picture);
                userRepository.save(user);
            }
        }

        String token = jwtProvider.createToken(user.getId());
        return new GoogleLoginResponseDto(
                token,
                user.getEmail(),
                user.getUsername(),
                user.getProfileImgUrl(),
                isNewUser
        );
    }

    public SocialLoginResponseDto socialLogin(SocialLoginRequestDto requestDto) {
        User user = userRepository.findByGoogleID(requestDto.getProviderId())
                .orElse(null);

        boolean isNewUser = false;

        if (user == null) {
            isNewUser = true;
            user = User.builder()
                    .username(requestDto.getNickname())
                    .email(requestDto.getEmail())
                    .googleID(requestDto.getProviderId())
                    .isSocial(true)
                    .provider(requestDto.getSocialType().toLowerCase())
                    .profileImgUrl(requestDto.getProfileImgUrl())
                    .phone(null)
                    .birthDate(null)
                    .isProfileCompleted(false)
                    .build();
            user = userRepository.save(user);
        } else {
            if (user.getProfileImgUrl() == null ||
                    !user.getProfileImgUrl().equals(requestDto.getProfileImgUrl())) {
                user.updateProfileImgUrl(requestDto.getProfileImgUrl());
                userRepository.save(user);
            }
        }

        String token = jwtProvider.createToken(user.getId());

        return new SocialLoginResponseDto(
                token,
                user.getEmail(),
                user.getUsername(),
                user.getProfileImgUrl(),
                isNewUser
        );
    }

    @Transactional
    public ProfileSetupResponseDto setupProfile(ProfileSetupRequestDto req) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate.parse(req.getBirthDate(), fmt);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("생년월일 형식이 올바르지 않습니다. (yyyy-MM-dd)");
        }

        if (req.getBio() != null && req.getBio().length() > 60) {
            throw new IllegalArgumentException("소개글은 60자 이하여야 합니다.");
        }

        if (!req.getPhone().matches("^[0-9]+$")) {
            throw new IllegalArgumentException("연락처는 숫자만 입력 가능합니다. (- 없이)");
        }

        String role = req.getRole().toUpperCase();

        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + req.getUserId()));

        // 사용자 기본 정보 업데이트
        user.updateProfileInfo(
                req.getName(),
                req.getPhone(),
                req.getEmail(),
                req.getBirthDate(),
                req.getBio()
        );
        user.completeProfile();
        userRepository.save(user);

        switch (role) {
            case "ARTIST" -> {
                Long artistId = artistRepository.findIdByUserId(user.getId()).orElse(null);
                if (artistId == null) {
                    Artist artist = Artist.builder()
                            .user(user)
                            .birth(req.getBirthDate())
                            .educationBackground(req.getEducation())
                            .disclosureStatus(req.getIsEducationPublic())
                            .build();
                    artistRepository.save(artist);
                } else {
                    Artist artist = artistRepository.findById(artistId)
                            .orElseThrow(() -> new IllegalStateException("Artist id를 찾을 수 없습니다."));
                    artist.update(
                            req.getBirthDate(),
                            req.getEducation(),
                            req.getIsEducationPublic()
                    );
                    artistRepository.save(artist);
                }
            }
            case "COLLECTOR" -> {
                Collector collector = collectorRepository.findByUserId(user.getId()).orElse(null);
                if (collector == null) {
                    collector = Collector.builder()
                            .user(user)
                            .birth(req.getBirthDate())
                            .educationBackground(req.getEducation())
                            .disclosureStatus(req.getIsEducationPublic())
                            .build();
                } else {
                    collector.update(
                            req.getBirthDate(),
                            req.getEducation(),
                            req.getIsEducationPublic()
                    );
                }
                collectorRepository.save(collector);
            }
            default -> throw new InvalidRoleException("Invalid role: " + req.getRole() + ". Must be 'ARTIST' or 'COLLECTOR'");
        }

        return new ProfileSetupResponseDto(
                user.getId(),
                role,
                user.getUsername(),
                "Profile setup completed successfully"
        );
    }

    private String getAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "code=" + code +
                "&client_id=" + "419535734312-ghb4pidh6qfag215jpb866s0ua0070e5.apps.googleusercontent.com" +
                "&client_secret=" + "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" +
                "&redirect_uri=" + "https://oauth.pstmn.io/v1/callback" +
                "&grant_type=authorization_code";

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://oauth2.googleapis.com/token",
                request,
                String.class
        );

        JSONObject json = new JSONObject(response.getBody());
        return json.getString("access_token");
    }

    private JSONObject getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>("", headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v3/userinfo",
                HttpMethod.GET,
                entity,
                String.class
        );

        return new JSONObject(response.getBody());
    }
}