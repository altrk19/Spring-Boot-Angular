package com.spring.angular.reddit.service.auth;

import com.spring.angular.reddit.dto.AuthenticationResponse;
import com.spring.angular.reddit.dto.LoginRequest;
import com.spring.angular.reddit.dto.NotificationEmail;
import com.spring.angular.reddit.dto.RegisterRequest;
import com.spring.angular.reddit.exception.SpringRedditException;
import com.spring.angular.reddit.model.User;
import com.spring.angular.reddit.model.VerificationToken;
import com.spring.angular.reddit.repository.UserRepository;
import com.spring.angular.reddit.repository.VerificationTokenRepository;
import com.spring.angular.reddit.security.JwtProvider;
import com.spring.angular.reddit.service.mail.MailService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    private static final String MAIL_BODY =
            "Thank you for signing up to Spring Reddit, \nplease click on the below url to activate your account : http://localhost:8080/api/auth/accountVerification/";
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public AuthServiceImpl(PasswordEncoder passwordEncoder,
                           UserRepository userRepository,
                           VerificationTokenRepository verificationTokenRepository,
                           MailService mailService,
                           AuthenticationManager authenticationManager,
                           JwtProvider jwtProvider) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.mailService = mailService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @Override
    @Transactional
    public void signUp(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);
        String token = generateVerificationToken(user);

        NotificationEmail notificationEmail = new NotificationEmail();
        notificationEmail.setSubject("Please activate your account");
        notificationEmail.setRecipient(user.getEmail());
        notificationEmail.setBody(MAIL_BODY + token);
        mailService.sendMail(notificationEmail);
    }

    @Override
    public void verifyAccount(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new SpringRedditException("Invalid Token"));
        retrieveUserAndEnable(verificationToken);
    }

    @Override
    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setUsername(loginRequest.getUsername());
        authenticationResponse.setAuthenticationToken(token);
        return authenticationResponse;
    }

    @Transactional
    private void retrieveUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new SpringRedditException("User not found - " + username));

        user.setEnabled(true);
        userRepository.save(user);
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }
}
