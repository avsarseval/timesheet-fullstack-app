package com.aksigorta.timesheet.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component // Bu sınıfın bir Spring bileşeni olduğunu ve başka sınıflara enjekte edilebileceğini belirtir.
public class JwtTokenProvider {

    // application.properties'den gizli anahtarı okur.
    @Value("${app.jwt-secret}")
    private String jwtSecret;

    // application.properties'den geçerlilik süresini okur.
    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpirationInMs;

    // Kimliği doğrulanmış kullanıcı için bir JWT üretir.
    public String generateToken(Authentication authentication) {
        // Kimliği doğrulanmış kullanıcının adını al.
        String username = authentication.getName();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        // JWT'yi oluşturma süreci
        return Jwts.builder()
                .setSubject(username) // JWT'nin kime ait olduğunu ayarla
                .setIssuedAt(new Date()) // Ne zaman oluşturulduğunu ayarla
                .setExpiration(expiryDate) // Son kullanma tarihini ayarla
                .signWith(SignatureAlgorithm.HS256, jwtSecret) // Özel damgamız (imza) ile mühürle
                .compact(); // Her şeyi birleştirip String olarak döndür.
    }

    // --- BU METOTLARI 6. GÜNDE KULLANACAĞIZ, ŞİMDİLİK BOŞ DURABİLİRLER ---

    // Gelen bir JWT'den kullanıcı adını okur.
    public String getUsernameFromJWT(String token) {
        // TODO: Burayı 6. günde dolduracağız.
        return null;
    }

    // Gelen bir JWT'nin geçerli olup olmadığını (imzası doğru mu, süresi dolmuş mu) kontrol eder.
    public boolean validateToken(String authToken) {
        // TODO: Burayı 6. günde dolduracağız.
        return false;
    }
}