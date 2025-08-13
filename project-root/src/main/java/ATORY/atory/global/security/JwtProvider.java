package ATORY.atory.global.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider {

    private final long EXPIRATION_TIME_MS;
    private final SecretKey key;

    public JwtProvider(
            @Value("${jwt.secret:}") String base64Secret,
            @Value("${jwt.expiration-ms:86400000}") long expiration
    ) {
        // 환경변수 jwt.secret이 없으면 기본 시크릿 생성
        String secretToUse = base64Secret;
        if (secretToUse == null || secretToUse.isBlank()) {
            String defaultSecret = "default-jwt-secret-key-change-this-to-secure-one-123456";
            secretToUse = Base64.getEncoder().encodeToString(defaultSecret.getBytes(StandardCharsets.UTF_8));
            System.err.println("[JWT] 경고: jwt.secret 미설정. 기본 키 사용 중");
        }

        byte[] keyBytes = Decoders.BASE64.decode(secretToUse);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.EXPIRATION_TIME_MS = expiration;
    }

    @PostConstruct
    void logKeyInfo() {
        System.out.println("[JWT] alg=HS256, keyLength=" + (key.getEncoded().length * 8) + " bits");
    }

    public String createToken(Long userId) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + EXPIRATION_TIME_MS);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .setAllowedClockSkewSeconds(60)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .setAllowedClockSkewSeconds(60)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            System.err.println("[JWT] 서명/형식 에러: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.err.println("[JWT] 만료: exp=" + e.getClaims().getExpiration());
        } catch (UnsupportedJwtException e) {
            System.err.println("[JWT] 미지원 토큰: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("[JWT] 비어있음/누락: " + e.getMessage());
        }
        return false;
    }
}