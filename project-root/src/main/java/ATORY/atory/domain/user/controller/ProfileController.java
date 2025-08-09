package ATORY.atory.domain.user.controller;

import ATORY.atory.domain.user.dto.MeSummaryDto;
import ATORY.atory.domain.user.dto.MyProfileDto;
import ATORY.atory.domain.user.service.ProfileQueryService;
import ATORY.atory.global.dto.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileQueryService profileQueryService;

    @GetMapping("/me")
    public ApiResult<MeSummaryDto> me(HttpServletRequest request) {
        return ApiResult.ok(profileQueryService.me(request));
    }

    @GetMapping("/profile")
    public ApiResult<MyProfileDto> myProfile(HttpServletRequest request) {
        return ApiResult.ok(profileQueryService.myProfile(request));
    }
}
