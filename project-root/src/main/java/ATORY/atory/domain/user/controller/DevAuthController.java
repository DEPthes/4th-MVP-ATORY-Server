package ATORY.atory.domain.user.controller;

import ATORY.atory.global.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/dev")
@RequiredArgsConstructor
public class DevAuthController {

    private final JwtProvider jwtProvider;

    @Value("${dev.auth.enabled:false}")
    private boolean devAuthEnabled;

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest req) {
        if (!devAuthEnabled) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "DEV_AUTH_DISABLED");
        }
        if (req.email() == null || req.email().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "EMAIL_REQUIRED");
        }

        long fakeUserId = 999L;
        String token = jwtProvider.createToken(fakeUserId);
        return new TokenResponse(token);
    }

    public record LoginRequest(String email, String password) {}
    public record TokenResponse(String token) {}
}