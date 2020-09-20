package com.spring.angular.reddit.security;

import com.spring.angular.reddit.exception.SpringRedditException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

@Service
@Slf4j
public class JwtProvider {

    private KeyStore keyStore;

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

    public String generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        Key key = getPrivateKey();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .signWith(key)
                .compact();
    }

    private Key getPrivateKey() {
        try {
            return keyStore.getKey("springblog", "secret".toCharArray());
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
            throw new SpringRedditException("Exception occurred while retrieving public key from keystore");
        }
    }
}
