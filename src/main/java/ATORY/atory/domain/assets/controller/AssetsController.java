package ATORY.atory.domain.assets.controller;

import ATORY.atory.domain.assets.dto.BannerResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/assets")
@Tag(name = "Assets", description = "자산 관련 API")
public class AssetsController {

    @GetMapping("/banner")
    @Operation(summary = "기본 배너 이미지 조회", description = "기본 배너 이미지 URL을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "배너 이미지 조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<BannerResponseDto> getDefaultBanner() {
        BannerResponseDto response = BannerResponseDto.builder()
                .defaultBannerUrl("https://atory-s3-bucket.s3.ap-northeast-2.amazonaws.com/default-banner.jpg")
                .build();
        
        return ResponseEntity.ok(response);
    }
} 