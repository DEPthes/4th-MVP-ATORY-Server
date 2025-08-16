package ATORY.atory.domain.user.service;

import ATORY.atory.domain.artist.dto.ArtistRegisterDto;
import ATORY.atory.domain.artist.entity.Artist;
import ATORY.atory.domain.artist.repository.ArtistRepository;
import ATORY.atory.domain.collector.dto.CollectorRegisterDto;
import ATORY.atory.domain.collector.entity.Collector;
import ATORY.atory.domain.collector.repository.CollectorRepository;
import ATORY.atory.domain.gallery.dto.GalleryRegisterDto;
import ATORY.atory.domain.gallery.entity.Gallery;
import ATORY.atory.domain.gallery.repository.GalleryRepository;
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static ATORY.atory.global.dto.UserType.*;

@Service
@RequiredArgsConstructor
public class UserRegisterService {

    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;
    private final CollectorRepository collectorRepository;
    private final GalleryRepository galleryRepository;

    private final String SERVICE_KEY = "3IFoWsEA2I43PEmk5vmEq8OBQwwiRVas%2BXgeQdOorDmhB2nFtDy3RjypQapBSUUx9Q%2BoZygaL%2FihO051Jd%2FGCQ%3D%3D";
    private final WebClient webClient;

    //작가 회원 가입
    @Transactional
    public Boolean artistRegister(ArtistRegisterDto artistRegisterDto){

        User savedUser = userRepository.save(User.builder()
                .username(artistRegisterDto.getName())
                .email(artistRegisterDto.getEmail())
                .contact(artistRegisterDto.getContact())
                .introduction(artistRegisterDto.getIntroduction())
                .googleID(artistRegisterDto.getGoogleID())
                .coverImageURL(null)
                .profileImageURL(null)
                .userType(ARTIST)
                .build());

            artistRepository.save(Artist.builder()
                            .birth(artistRegisterDto.getBirth())
                            .user(savedUser)
                            .educationBackground(artistRegisterDto.getEducationBackground())
                            .disclosureStatus(artistRegisterDto.getDisclosureStatus())
                            .build());

            return true;
        }

    //콜렉터 회원 가입
    @Transactional
    public Boolean collectorRegister(CollectorRegisterDto collectorRegisterDto){

        User savedUser = userRepository.save(User.builder()
                .username(collectorRegisterDto.getName())
                .email(collectorRegisterDto.getEmail())
                .contact(collectorRegisterDto.getContact())
                .introduction(collectorRegisterDto.getIntroduction())
                .googleID(collectorRegisterDto.getGoogleID())
                .coverImageURL(null)
                .profileImageURL(null)
                .userType(COLLECTOR)
                .build());

        collectorRepository.save(Collector.builder()
                .birth(collectorRegisterDto.getBirth())
                .user(savedUser)
                .educationBackground(collectorRegisterDto.getEducationBackground())
                .disclosureStatus(collectorRegisterDto.getDisclosureStatus())
                .build());

            return true;
    }

    //갤러리 회원 가입
    @Transactional
    public Boolean galleryRegister(GalleryRegisterDto galleryRegisterDto){

        User savedUser = userRepository.save(User.builder()
                .username(galleryRegisterDto.getUserName())
                .email(galleryRegisterDto.getEmail())
                .contact(galleryRegisterDto.getContact())
                .introduction(galleryRegisterDto.getIntroduction())
                .googleID(galleryRegisterDto.getGoogleID())
                .coverImageURL(null)
                .profileImageURL(null)
                .userType(GALLERY)
                .build());

        galleryRepository.save(Gallery.builder()
                .user(savedUser)
                .name(galleryRegisterDto.getGalleryName())
                .location(galleryRegisterDto.getLocation())
                .registrationNumber(galleryRegisterDto.getRegistrationNumber())
                .build());

        return true;
    }

    //사업자 등록번호로 사업자 조회
    public Boolean checkBusinessStatus(String businessNumber) {

        if (businessNumber.length() != 10) {
            throw new IllegalArgumentException("사업자 등록 번호는 숫자 10자리여야합니다.");
        }

        Map<String, Object> body = Map.of("b_no", List.of(businessNumber));
        String url = "https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=" + SERVICE_KEY;

        String rawResponse = webClient.post()
                .uri(URI.create(url))
                .header(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchangeToMono(res -> res.bodyToMono(String.class))
                .block();

        if (rawResponse == null || rawResponse.isBlank()) {
            throw new RuntimeException("서버 응답이 없습니다.");
        }

        if (rawResponse.contains("\"b_stt_cd\":\"01\"")) {
            return true;
        } else {
            return false;
        }
    }
}
