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
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


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

        String googleId = userInfo.getString("sub");
        String email = userInfo.getString("email");
        String name = userInfo.getString("name");

        User user = userRepository.findByGoogleID(googleId)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .username(name)
                            .email(email)
                            .googleID(googleId)
                            .isSocial(true)
                            .provider("google")
                            .phone(null)
                            .birthDate(null)
                            .isProfileCompleted(false)
                            .build();
                    return userRepository.save(newUser);
                });

        String token = jwtProvider.createToken(user.getId());
        return new GoogleLoginResponseDto(token, user.getEmail(), user.getUsername());
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

    public ProfileSetupResponseDto setupProfile(ProfileSetupRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with id: " + requestDto.getUserId()));

        user.updateProfileInfo(
                requestDto.getName(),
                requestDto.getPhone(),
                requestDto.getEmail(),
                requestDto.getBirthDate(),
                requestDto.getBio()
        );

        user.completeProfile();
        userRepository.save(user);

        if ("ARTIST".equals(requestDto.getRole())) {
            Artist artist = Artist.builder()
                    .user(user)
                    .birth(requestDto.getBirthDate())
                    .educationBackground(requestDto.getEducation())
                    .disclosureStatus(requestDto.getIsEducationPublic())
                    .build();
            artistRepository.save(artist);
        } else if ("COLLECTOR".equals(requestDto.getRole())) {
            Collector collector = Collector.builder()
                    .user(user)
                    .birth(requestDto.getBirthDate())
                    .educationBackground(requestDto.getEducation())
                    .disclosureStatus(requestDto.getIsEducationPublic())
                    .build();
            collectorRepository.save(collector);
        } else {
            throw new InvalidRoleException(
                    "Invalid role: " + requestDto.getRole() + ". Must be 'ARTIST' or 'COLLECTOR'");
        }

        return new ProfileSetupResponseDto(
                user.getId(),
                requestDto.getRole(),
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