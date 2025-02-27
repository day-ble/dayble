package com.dayble.blog.global.interceptor;

import com.dayble.blog.global.exception.DaybleApplicationException;
import com.dayble.blog.global.exception.ErrorCodes;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String secretKeyString;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = new SecretKeySpec(secretKeyString.getBytes(), SignatureAlgorithm.HS512.getJcaName());
    }


    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final long expirationTime = 1000L * 60 * 60;

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
            throw new DaybleApplicationException(ErrorCodes.JWT_TOKEN_CREATE_ERROR);
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
            throw new DaybleApplicationException(ErrorCodes.INVALID_TOKEN);
        }
    }
}
