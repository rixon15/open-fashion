package com.openfashion.openfasion_marketplace.services;

import com.openfashion.openfasion_marketplace.models.entities.JwtBlacklist;
import com.openfashion.openfasion_marketplace.models.entities.User;
import com.openfashion.openfasion_marketplace.repositories.JwtBlacklistRepository;
import com.openfashion.openfasion_marketplace.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {


    private final String secretKey;
    private final UserRepository userRepository;
    private final JwtBlacklistRepository jwtBlacklistRepository;

    public JwtService(UserRepository userRepository, JwtBlacklistRepository jwtBlacklistRepository) {
        this.userRepository = userRepository;
        this.jwtBlacklistRepository = jwtBlacklistRepository;

        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey generatedKey = keyGenerator.generateKey();
            secretKey = Base64.getEncoder().encodeToString(generatedKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    public String generateToken(String userId) {

        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(userId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .and()
                .signWith(getSignKey())
                .compact();


    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserId(String token) {

        return extractClaim(token, Claims::getSubject);

    }

    public boolean validateToken(String token) {

        final int userId =Integer.parseInt(extractUserId(token));

        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()) {
            return userId == user.get().getId() && !isTokenExpired(token) && !isTokenBlacklisted(token);
        }

        return false;

    }

    public void revokeToken(String token) {

        JwtBlacklist jwtBlacklist = new JwtBlacklist();
        jwtBlacklist.setToken(token.substring(7));
        jwtBlacklistRepository.save(jwtBlacklist);

    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    private Date extractExpiration(String token) {

        return extractClaim(token, Claims::getExpiration);

    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private boolean isTokenBlacklisted(String token) {

        return jwtBlacklistRepository.findJwtBlacklistByToken(token).isPresent();

    }



}
