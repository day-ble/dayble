package com.dayble.blog.mail.application;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.MessageRejectedException;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.dayble.blog.mail.controller.request.SendMailRequest;
import com.dayble.blog.mail.controller.request.SendValidateMailRequest;
import com.dayble.blog.mail.sender.EmailSender;
import com.dayble.blog.mail.sender.EmailValidatorSender;
import com.dayble.blog.mailEvent.MailEvents;
import com.dayble.blog.mailEvent.MailEventsRepository;
import com.dayble.blog.slack.application.SlackService;
import com.dayble.blog.user.domain.User;
import com.dayble.blog.user.domain.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final AmazonSimpleEmailService amazonSimpleEmailService;
    private final EmailSender emailSender;
    private final EmailValidatorSender emailValidatorSender;
    private final MailEventsRepository mailEventsRepository;
    private final UserRepository userRepository;
    private final SlackService slackService;

    @Async("taskExecutor")
    public void send(final SendMailRequest sendMailRequest) {
        final List<String> failedReceivers = new ArrayList<>();

        for (String receiver : sendMailRequest.receivers()) {
            try {
                final SendEmailRequest emailRequest = emailSender.from(sendMailRequest, receiver);
                amazonSimpleEmailService.sendEmail(emailRequest);
            } catch (Exception ex) {
                failedReceivers.add(receiver);
                log.error("메일 발송에 실패하였습니다. {}", receiver, ex);
            }
        }

        if (!failedReceivers.isEmpty()) {
            retryFailedEmails(failedReceivers, sendMailRequest);
        }
    }

    private void retryFailedEmails(final List<String> failedReceivers, final SendMailRequest sendMailRequest) {
        for (String receiver : failedReceivers) {
            try {
                final SendEmailRequest emailRequest = emailSender.from(sendMailRequest, receiver);
                amazonSimpleEmailService.sendEmail(emailRequest);
            } catch (MessageRejectedException ex) {
                log.error("메일 재발송에 실패하였습니다. {}", receiver, ex);
                final User user = userRepository.findByEmail(receiver);
                sendMailRequest.contents()
                        .stream()
                        .map(mailContent -> MailEvents.of(
                                user,
                                mailContent.title(),
                                mailContent.summary(),
                                mailContent.originalLink(),
                                mailContent.thumbnail()
                        ))
                        .forEach(mailEventsRepository::save);
                slackService.postInquiry(receiver);
            }
        }
    }

    @Async("taskExecutor")
    public void sendValidateEmail(final SendValidateMailRequest request) {
        try {
            final SendEmailRequest emailRequest = emailValidatorSender.from(request);
            amazonSimpleEmailService.sendEmail(emailRequest);
        } catch (Exception ex) {
            throw new AmazonClientException(ex.getMessage(), ex);
        }
    }
}

