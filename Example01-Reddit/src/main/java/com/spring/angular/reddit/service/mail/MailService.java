package com.spring.angular.reddit.service.mail;

import com.spring.angular.reddit.constants.CommonConstants;
import com.spring.angular.reddit.constants.RequestErrorTypes;
import com.spring.angular.reddit.resource.NotificationEmailResource;
import com.spring.angular.reddit.exception.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailService {
    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;

    public MailService(JavaMailSender mailSender,
                       MailContentBuilder mailContentBuilder) {
        this.mailSender = mailSender;
        this.mailContentBuilder = mailContentBuilder;
    }

    @Async
    public void sendMail(NotificationEmailResource notificationEmailResource) throws ServerException {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(notificationEmailResource.getRecipient());
            messageHelper.setSubject(notificationEmailResource.getSubject());
            messageHelper.setText(mailContentBuilder.build(notificationEmailResource.getBody()));
        };
        try{
            mailSender.send(messagePreparator);
            log.info("mail sent");
        }catch (MailException e){
            log.debug("Exception occurred while sending activation mail");
            throw new ServerException(RequestErrorTypes.GENERIC_SERVICE_ERROR,new String[]{CommonConstants.ACTIVATION_MAIL_FAILED,
                    HttpStatus.SERVICE_UNAVAILABLE.toString()}, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
