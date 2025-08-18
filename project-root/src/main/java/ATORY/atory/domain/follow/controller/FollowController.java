package ATORY.atory.domain.follow.controller;

import ATORY.atory.domain.follow.dto.FollowToggleResponse;
import ATORY.atory.domain.follow.dto.FollowUserDto;
import ATORY.atory.domain.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/follow")
public class FollowController {
    private final FollowService followService;
    @PostMapping("/{id}")
    public ResponseEntity<FollowToggleResponse> followOrUnfollow(
            @PathVariable("id") Long id,
            @RequestParam("googleId") String googleId
    ) {
        return ResponseEntity.ok(followService.toggle(googleId, id));
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity<List<FollowUserDto>> getAllFollowers(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(followService.getAllFollowers(id));
    }

    @GetMapping("/{id}/following")
    public ResponseEntity<List<FollowUserDto>> getAllFollowing(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(followService.getAllFollowing(id));
    }
}
