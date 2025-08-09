package ATORY.atory.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthUserResolver {
    private final JwtProvider jwtProvider;

    public Optional<Long> resolveUserId(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) return Optional.empty();
        String token = auth.substring(7);
        if (!jwtProvider.validateToken(token)) return Optional.empty();

        String sub = jwtProvider.getUserIdFromToken(token);
        try {
            return Optional.of(Long.parseLong(sub));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
