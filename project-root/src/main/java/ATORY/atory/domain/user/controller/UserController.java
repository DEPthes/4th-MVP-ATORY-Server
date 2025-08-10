package ATORY.atory.domain.user.controller;

import ATORY.atory.domain.user.dto.BannerUpdateResponseDto;
import ATORY.atory.domain.user.dto.CustomUserDetails;
import ATORY.atory.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    @PutMapping("/banner")
    public ResponseEntity<BannerUpdateResponseDto> updateBanner(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = userDetails.getUser().getId();

        BannerUpdateResponseDto responseDto = userService.updateUserBanner(userId, file);
        if (responseDto.isSuccess()) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.badRequest().body(responseDto);
        }
    }
}
