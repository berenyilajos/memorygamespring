package hu.fourdsoft.memorygame.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String ROLE_USER = "ROLE_USER";

    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }

    public boolean isValidToken(String token, String userName) {
        String extractedUserName = extractUserName(token);
        return userName.equals(extractedUserName) && isTokenNotExpired(token);
    }

    public boolean isValidToken(String token) {
        return extractUserName(token) != null && isTokenNotExpired(token);
    }

    public String extractUserName(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer("fourdsoft.hu")
                .setIssuedAt(now())
                .setExpiration(nowAfter(1000 * 60 * 60 * 10))
                .signWith(SECRET_KEY)
                .compact();
    }

    private boolean isTokenNotExpired(String token) {
        return extractExpiration(token).after(now());
    }

    private Date now() {
        return new Date(System.currentTimeMillis());
    }

    private Date nowAfter(long after) {
        return new Date(System.currentTimeMillis() + after);
    }
}
