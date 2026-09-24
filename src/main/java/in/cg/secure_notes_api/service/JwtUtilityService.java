package in.cg.secure_notes_api.service;

import in.cg.secure_notes_api.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtUtilityService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    public Key getSignKey(){
        return Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateToken(User user){
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role",user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis()
                                    + expiration))
                .signWith(getSignKey())
                .compact();
    }

    public String extractUsername(String token){
        return extractClaims(token)
                .getSubject();
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    public Boolean verifyToken(String token, UserDetails userDetails){
        return extractUsername(token).equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return !extractClaims(token).getExpiration()
                .before(new Date(System.currentTimeMillis()));
    }


//    create Key
//    generate token
//    extract claims
//    extract subject
//    verify token

}
