package com.spring.angular.reddit.security;

import com.spring.angular.reddit.constants.RequestErrorTypes;
import com.spring.angular.reddit.exception.ClientException;
import com.spring.angular.reddit.exception.SpringRedditException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;

@Service
@Slf4j
public class JwtProvider {
    private Long jwtExpirationInMillis;
    private KeyStore keyStore;

    public JwtProvider(@Value("${jwt.expiration.time}") Long jwtExpirationInMillis) {
        this.jwtExpirationInMillis = jwtExpirationInMillis;
    }

    public Long getJwtExpirationInMillis() {
        return jwtExpirationInMillis;
    }

    @PostConstruct
    public void init() {
        InputStream resourcesAsStream = null;

        try {
            keyStore = KeyStore.getInstance("JKS");
            resourcesAsStream = new FileInputStream(new File("src/main/resources/springblog.jks"));
            keyStore.load(resourcesAsStream, "secret".toCharArray());
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new SpringRedditException("Exception occurred while loading keystore");
        } finally {
            try {
                if (resourcesAsStream != null) {
                    resourcesAsStream.close();
                }
            } catch (IOException e) {
                log.debug("Exception occurred while closing stream");
            }
        }
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();
    }

    public String generateTokenWithUserName(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();
    }

    private Key getPrivateKey() {
        try {
            return keyStore.getKey("springblog", "secret".toCharArray());
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
            throw new SpringRedditException("Exception occurred while retrieving public key from keystore");
        }
    }

    public boolean validateToken(String jwt) {
        Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);
        return true;
    }

    private PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate("springblog").getPublicKey();
        } catch (KeyStoreException e) {
            throw new SpringRedditException("Exception occurred while retrieving public key from keystore");
        }
    }

    public String getUsernameFromJwt(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getPublicKey())
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

}
