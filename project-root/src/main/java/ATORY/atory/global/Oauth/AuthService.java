package ATORY.atory.global.Oauth;

import ATORY.atory.domain.user.entity.User;
import ATORY.atory.domain.user.repository.UserRepository;
import ATORY.atory.global.Oauth.dto.GoogleTokenResponseDto;
import ATORY.atory.global.Oauth.dto.LoginResponseDto;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final WebClient webClient;

    public String exchangeAuthCodeForGoogleSub (String code){
        //Google Client 정보 가져오기
        ClientRegistration googleRegistration = clientRegistrationRepository.findByRegistrationId("google");

        //토큰 요청
        GoogleTokenResponseDto tokenResponse = webClient.post()
                .uri("https://oauth2.googleapis.com/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("code", code)
                        .with("client_id", googleRegistration.getClientId())
                        .with("client_secret", googleRegistration.getClientSecret())
                        .with("redirect_uri", googleRegistration.getRedirectUri())
                        .with("grant_type", "authorization_code"))
                .retrieve()
                .bodyToMono(GoogleTokenResponseDto.class)
                .block();

        if (tokenResponse == null || tokenResponse.getIdToken() == null) {
            throw new RuntimeException("구글 토큰을 가져올 수 없습니다.");
        }

        //id_token 디코딩 후 sub 추출
        DecodedJWT jwt = JWT.decode(tokenResponse.getIdToken());
        return jwt.getClaim("sub").asString();
    }

    public LoginResponseDto login (String code){
        String sub = exchangeAuthCodeForGoogleSub(code);

        Optional<User> user = userRepository.findByGoogleID(sub);

        LoginResponseDto response = new LoginResponseDto();
        response.setGoogleID(sub);

        if (user.isPresent()) {
            User existUser = user.get();

            response.setIsMember(true);
            response.setUserID(existUser.getId());
        } else {
            response.setIsMember(false);
            response.setUserID(null);
        }

        return response;
    }
}
