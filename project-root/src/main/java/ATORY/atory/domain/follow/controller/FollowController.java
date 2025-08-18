package ATORY.atory.domain.follow.controller;

import ATORY.atory.domain.follow.dto.FollowToggleResponse;
import ATORY.atory.domain.follow.dto.FollowUserDto;
import ATORY.atory.domain.follow.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/follow")
@Tag(name = "Follow", description = "팔로우 관련 API")
public class FollowController {
    private final FollowService followService;
    @Operation(summary = "팔로우 또는 언팔로우", description = "googleId를 기반으로 특정 유저(ID)에 대해 팔로우 또는 언팔로우를 토글합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 팔로우/언팔로우 처리됨"),
            @ApiResponse(responseCode = "400", description = "자기 자신을 팔로우하려는 경우"),
            @ApiResponse(responseCode = "404", description = "대상 유저 또는 나 자신(googleId)이 존재하지 않는 경우"),
            @ApiResponse(responseCode = "409", description = "이미 팔로우되어 있는 경우 등 충돌 발생")
    })
    @PostMapping("/{id}")
    public ResponseEntity<FollowToggleResponse> followOrUnfollow(
            @Parameter(description = "팔로우할 대상 유저 ID", example = "5")
            @PathVariable("id") Long id,

            @Parameter(description = "내 Google ID", example = "example@gmail.com")
            @RequestParam("googleId") String googleId
    ) {
        return ResponseEntity.ok(followService.toggle(googleId, id));
    }

    @Operation(summary = "팔로워 리스트 조회", description = "특정 유저의 팔로워 목록을 ㄱㄴㄷ 순으로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팔로워 목록 반환 성공"),
            @ApiResponse(responseCode = "404", description = "대상 유저를 찾을 수 없음")
    })
    @GetMapping("/{id}/followers")
    public ResponseEntity<List<FollowUserDto>> getAllFollowers(
            @Parameter(description = "유저 ID", example = "5")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(followService.getAllFollowers(id));
    }
    @Operation(summary = "팔로잉 리스트 조회", description = "특정 유저가 팔로우한 유저 목록을 ㄱㄴㄷ 순으로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팔로잉 목록 반환 성공"),
            @ApiResponse(responseCode = "404", description = "대상 유저를 찾을 수 없음")
    })
    @GetMapping("/{id}/following")
    public ResponseEntity<List<FollowUserDto>> getAllFollowing(
            @Parameter(description = "유저 ID", example = "5")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(followService.getAllFollowing(id));
    }
}
