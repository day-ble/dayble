package itcast.auth.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "JwtUtil")
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final SecretKey secretKey;
    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final long expirationTime = 1000L * 60 * 60;

    public JwtUtil() {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public String createToken(Long userId, String email) {
        try {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + expirationTime);

            return Jwts.builder()
                    .setSubject(String.valueOf(userId))
                    .claim("email", email)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(SignatureAlgorithm.HS512, secretKey)
                    .compact();
        } catch (Exception e) {
            log.error("JWT 토큰 생성 실패: ", e);
            throw new RuntimeException("JWT 토큰 생성에 실패했습니다.");
        }
    }

    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            log.error("토큰에서 userId 추출 실패: ", e);
            throw new RuntimeException("유효하지 않거나 만료된 토큰입니다.");
        }
    }
}