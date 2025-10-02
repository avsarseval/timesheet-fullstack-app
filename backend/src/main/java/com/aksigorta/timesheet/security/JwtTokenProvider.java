package com.aksigorta.timesheet.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;
import java.util.List;

import javax.crypto.SecretKey; // Bu import önemli!
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwt-secret}")
    private String jwtSecretString;

    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpirationInMs;


    private SecretKey key;

    // Sınıf oluşturulduktan hemen sonra bu metot çalışır ve String'den güvenli Key nesnesini oluşturur.
    @jakarta.annotation.PostConstruct
    public void init() {
        System.out.println("YÜKLENEN GİZLİ ANAHTAR: '" + this.jwtSecretString + "'");

        if (this.jwtSecretString == null || this.jwtSecretString.trim().isEmpty()) {
            System.err.println("UYARI: app.jwt-secret ORTAM DEĞİŞKENİ YÜKLENEMEDİ VEYA BOŞ!");
        }

        byte[] keyBytes = Decoders.BASE64.decode(this.jwtSecretString);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // Token üretme metodu, artık String yerine Key nesnesini kullanacak.
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());


        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}