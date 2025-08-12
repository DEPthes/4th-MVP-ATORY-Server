package ATORY.atory.domain.user.controller;

import ATORY.atory.domain.user.dto.MeSummaryDto;
import ATORY.atory.domain.user.dto.MyProfileDto;
import ATORY.atory.domain.user.dto.ProfileUpdateRequest;
import ATORY.atory.domain.user.service.ProfileQueryService;
import ATORY.atory.domain.user.service.ProfileService;
import ATORY.atory.global.dto.ApiResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileQueryService profileQueryService;
    private final ProfileService profileService;

    @GetMapping("/me")
    public ApiResult<MeSummaryDto> me(HttpServletRequest request) {
        return ApiResult.ok(profileQueryService.me(request));
    }

    @GetMapping("/profile")
    public ApiResult<MyProfileDto> myProfile(HttpServletRequest request) {
        return ApiResult.ok(profileQueryService.myProfile(request));
    }

    @PatchMapping("/profile")
    public ApiResult<MyProfileDto> updateMyProfile(
            HttpServletRequest request,
            @Valid @RequestBody ProfileUpdateRequest req
    ) {
        return ApiResult.ok(profileService.updateMyProfile(request, req));
    }
}