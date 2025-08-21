package ATORY.atory.domain.user.controller;

import ATORY.atory.domain.artist.dto.ArtistDetailDto;
import ATORY.atory.domain.artist.dto.ArtistRegisterDto;
import ATORY.atory.domain.collector.dto.CollectorDetailDto;
import ATORY.atory.domain.collector.dto.CollectorRegisterDto;
import ATORY.atory.domain.gallery.dto.GalleryDetailDto;
import ATORY.atory.domain.gallery.dto.GalleryRegisterDto;
import ATORY.atory.domain.user.dto.ProfileDetailDto;
import ATORY.atory.domain.user.dto.UserInfoSideDto;
import ATORY.atory.domain.user.service.UserDetailService;
import ATORY.atory.domain.user.service.UserRegisterService;
import ATORY.atory.domain.user.service.UserService;
import ATORY.atory.global.dto.ApiResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRegisterService userRegisterService;
    private final UserService userService;
    private final UserDetailService userDetailService;

    @Operation(summary = "작가 회원가입", description = "작가 회원가입 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 확인됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping("/register/artist")
    public ApiResult<?> registerArtist(@RequestBody ArtistRegisterDto artistRegisterDto) {

        Boolean result = userRegisterService.artistRegister(artistRegisterDto);

        return ApiResult.ok(result);
    }

    @Operation(summary = "콜렉터 회원가입", description = "콜렉터 회원가입 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 확인됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping("/register/collector")
    public ApiResult<?> registerCollector(@RequestBody CollectorRegisterDto collectorRegisterDto) {
        Boolean result = userRegisterService.collectorRegister(collectorRegisterDto);

        return ApiResult.ok(result);
    }

    @Operation(summary = "갤러리 회원가입", description = "갤러리 회원가입 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 확인됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping("/register/gallery")
    public ApiResult<?> registerGallery(@RequestBody GalleryRegisterDto galleryRegisterDto) {
        Boolean result = userRegisterService.galleryRegister(galleryRegisterDto);

        return ApiResult.ok(result);
    }

    @Operation(summary = "유저 사이드 정보 조회", description = "메인 페이지 사이드바 유저 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 확인됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @GetMapping("/side/profile")
    public ApiResult<UserInfoSideDto> getUserSideInfo (@RequestParam String google_id){
        UserInfoSideDto result = userService.loadUserSideInfo(google_id);

        return ApiResult.ok(result);
    }

    @Operation(summary = "유저 프로필 사진 변경", description = "유저 프로필 사진 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 확인됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping(value = "/change/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResult<Boolean> changeProfileImage(@RequestParam String googleID, @RequestParam MultipartFile profileImage) throws IOException {
        return ApiResult.ok(userService.changeProfileImage(googleID, profileImage));
    }

    @Operation(summary = "유저 커버 사진 변경", description = "유저 커버 사진 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 확인됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping(value = "/change/cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResult<Boolean> changeCoverImage(@RequestParam String googleID, @RequestParam MultipartFile profileImage) throws IOException {
        return ApiResult.ok(userService.changeCoverImage(googleID, profileImage));
    }

    @Operation(summary = "아티스트 프로필 변경", description = "아티스트 프로필 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 확인됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping("/change/artist")
    public ApiResult<Boolean> changeArtist(@RequestParam String googleID, @RequestBody ArtistRegisterDto artistRegisterDto){
        return ApiResult.ok(userRegisterService.changeProfileArtist(googleID, artistRegisterDto));
    }

    @Operation(summary = "콜렉터 프로필 변경", description = "콜렉터 프로필 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 확인됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping("/change/collector")
    public ApiResult<Boolean> changeCollector(@RequestParam String googleID, @RequestBody CollectorRegisterDto collectorRegisterDto){
        return ApiResult.ok(userRegisterService.changeProfileCollector(googleID, collectorRegisterDto));
    }

    @Operation(summary = "갤러리 프로필 변경", description = "갤러리 프로필 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 확인됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping("/change/gallery")
    public ApiResult<Boolean> changeGallery(@RequestParam String googleID, @RequestBody GalleryRegisterDto galleryRegisterDto){
        return ApiResult.ok(userRegisterService.changeProfileGallery(googleID, galleryRegisterDto));
    }

    @Operation(summary = "유저 프로필 조회", description = "유저 프로필 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 확인됨" ,content = @Content(
                    schema = @Schema(oneOf = {ArtistDetailDto.class, CollectorDetailDto.class, GalleryDetailDto.class})
            )),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @GetMapping("/profile")
    public ApiResult<ProfileDetailDto> getProfile(@RequestParam String googleID, @RequestParam Long userId) throws JsonProcessingException {
        return ApiResult.ok(userDetailService.loadProfile(googleID, userId));
    }
}
