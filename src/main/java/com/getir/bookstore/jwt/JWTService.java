package com.getir.bookstore.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.getir.bookstore.authentication.LoginDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JWTService {

    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;
    private final Integer JWT_EXPIRATION;

    public JWTService(RSAPrivateKey privateKey, RSAPublicKey publicKey, @Value("${app.security.jwt.expiration-in-days}") Integer JWT_EXPIRATION) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.JWT_EXPIRATION = JWT_EXPIRATION;
    }

    public String createJwtForClaims(String subject, Map<String, String> claims) {
        JWTCreator.Builder jwtBuilder = JWT.create().withSubject(subject);

        Calendar calendar = this.createJWTExpiration();
        claims.forEach(jwtBuilder::withClaim);

        return jwtBuilder
                .withNotBefore(new Date())
                .withExpiresAt(calendar.getTime())
                .sign(Algorithm.RSA256(publicKey, privateKey));
    }

    private Calendar createJWTExpiration() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Instant.now().toEpochMilli());
        calendar.add(Calendar.DAY_OF_WEEK, JWT_EXPIRATION);
        return calendar;
    }

    public Map<String, String> getClaims(LoginDTO loginDTO, UserDetails userDetails) {
        Map<String, String> claims = new HashMap<>();
        claims.put("username", loginDTO.getEmail());

        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        claims.put("authorities", authorities);
        return claims;
    }

    public <T> T getClaim(Authentication authentication, String key) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Map<String, Object> tokenAttributes = token.getTokenAttributes();
        return ((T) tokenAttributes.get(key));
    }
}