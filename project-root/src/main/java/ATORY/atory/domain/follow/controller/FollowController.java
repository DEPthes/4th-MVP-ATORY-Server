package ATORY.atory.domain.follow.controller;

import ATORY.atory.domain.follow.dto.FollowToggleResponse;
import ATORY.atory.domain.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/follow")
public class FollowController {
    private final FollowService followService;
    @PostMapping("/{id}/follow")
    public ResponseEntity<FollowToggleResponse> followOrUnfollow(
            @PathVariable("id") Long id,
            @RequestParam("googleId") String googleId
    ) {
        return ResponseEntity.ok(followService.toggle(googleId, id));
    }
}
