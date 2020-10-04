package com.spring.angular.reddit.service.user;

import com.spring.angular.reddit.constants.CommonConstants;
import com.spring.angular.reddit.constants.RequestErrorTypes;
import com.spring.angular.reddit.exception.ClientException;
import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.model.User;
import com.spring.angular.reddit.model.UserActivationToken;
import com.spring.angular.reddit.repository.UserRepository;
import com.spring.angular.reddit.repository.VerificationTokenRepository;
import com.spring.angular.reddit.resource.NotificationEmailResource;
import com.spring.angular.reddit.security.JwtProvider;
import com.spring.angular.reddit.service.mail.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private static final String MAIL_BODY =
            "Thank you for signing up to Spring Reddit, \nplease click on the below url to activate your account : http://localhost:8080/api/user/userVerification/";
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final JwtProvider jwtProvider;

    public UserServiceImpl(UserRepository userRepository,
                           VerificationTokenRepository verificationTokenRepository,
                           MailService mailService, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.mailService = mailService;
        this.jwtProvider = jwtProvider;
    }

    @Override
    @Transactional
    public void signUp(User user) throws ServerException, ClientException {
        checkUserIsDuplicate(user);

        userRepository.save(user);
        String token = generateUserActivationToken(user);

        NotificationEmailResource notificationEmailResource = new NotificationEmailResource();
        notificationEmailResource.setSubject("Please activate your account");
        notificationEmailResource.setRecipient(user.getEmail());
        notificationEmailResource.setBody(MAIL_BODY + token);
        mailService.sendMail(notificationEmailResource);
    }

    @Override
    @Transactional
    public void deleteUser(String username) throws ServerException {
        //check user is exist
        User user = getSingleUserByUsername(username);
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void verifyUser(String token) throws ServerException, ClientException {
        UserActivationToken userActivationToken = verificationTokenRepository.findByToken(token).orElseThrow(
                () -> new ClientException(RequestErrorTypes.INVALID_ACCESS_TOKEN, null, HttpStatus.FORBIDDEN));

        String username = userActivationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new ServerException(RequestErrorTypes.UNKNOWN_RESOURCE,
                        new String[]{CommonConstants.USERNAME, username}, HttpStatus.NOT_FOUND));

        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public User getSingleUserByUsername(String username) throws ServerException {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new ServerException(RequestErrorTypes.UNKNOWN_RESOURCE,
                        new String[]{CommonConstants.USERNAME, username}, HttpStatus.NOT_FOUND));
    }

    private String generateUserActivationToken(User user) {
        String token = UUID.randomUUID().toString();

        UserActivationToken userActivationToken = new UserActivationToken();
        userActivationToken.setToken(token);
        userActivationToken.setUser(user);
        userActivationToken.setExpiryDate(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()));

        verificationTokenRepository.save(userActivationToken);
        return token;
    }

    private void checkUserIsDuplicate(User user) throws ClientException {
        String username = user.getUsername();
        User userDuplicateUsername = userRepository.findByUsername(username).orElse(null);
        if (Objects.nonNull(userDuplicateUsername)) {
            log.debug("already registered user with username: " + username);
            throw new ClientException(RequestErrorTypes.GENERIC_POLICY_ERROR,
                    new String[]{CommonConstants.REGISTERED_USERNAME_ERROR, HttpStatus.FORBIDDEN.toString()},
                    HttpStatus.FORBIDDEN);
        }

        String email = user.getEmail();
        User userDuplicateEmail = userRepository.findByEmail(email).orElse(null);
        if (Objects.nonNull(userDuplicateEmail)) {
            log.debug("already registered user with email: " + email);
            throw new ClientException(RequestErrorTypes.GENERIC_POLICY_ERROR,
                    new String[]{CommonConstants.REGISTERED_EMAIL_ERROR, HttpStatus.FORBIDDEN.toString()},
                    HttpStatus.FORBIDDEN);
        }
    }
}
