package ATORY.atory.domain.user.controller;

import ATORY.atory.domain.artist.dto.ArtistRegisterDto;
import ATORY.atory.domain.collector.dto.CollectorRegisterDto;
import ATORY.atory.domain.gallery.dto.GalleryRegisterDto;
import ATORY.atory.domain.user.service.UserRegisterService;
import ATORY.atory.global.dto.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRegisterService userRegisterService;

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
}
