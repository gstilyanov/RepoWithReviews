package com.stilyanov.repository.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.io.File;
import java.security.PrivateKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtils {

    private static final String ISSUER = "7f3fdf41e3a884934507";
    private static final String SUBJECT = "7f3fdf41e3a884934507";

    Instant now = Instant.now();
    File file = new File("src/repowithreview.pem");
    PrivateKey privateKey = PrivateKeyReader.readPrivateKey(file);

    public JwtUtils() throws Exception {
    }

    public String jwt = Jwts.builder()
            .setAudience("https://github.com/login/oauth/access_token")
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plus(5L, ChronoUnit.MINUTES)))
            .setIssuer(ISSUER)
            .setSubject(SUBJECT)
            .setId(UUID.randomUUID().toString())
            .signWith(SignatureAlgorithm.RS256, privateKey)
            .compact();
}
