package hu.fourdsoft.memorygame.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    private static final String SECRET_KEY = "secret";
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }

    public boolean isValidToken(String token, String userName) {
        String extractedUserName = extractUserName(token);
        return userName.equals(extractedUserName) && !isTokenExpired(token);
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
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(now())
                .setExpiration(nowAfter(1000 * 60 * 60 * 10)).signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(now());
    }

    private Date now() {
        return new Date(System.currentTimeMillis());
    }

    private Date nowAfter(long after) {
        return new Date(System.currentTimeMillis() + after);
    }
}
