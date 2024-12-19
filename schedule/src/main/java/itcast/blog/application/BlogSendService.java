package itcast.blog.application;

import itcast.blog.repository.BlogRepository;
import itcast.domain.blog.Blog;
import itcast.domain.user.User;
import itcast.domain.user.enums.Interest;
import itcast.jwt.repository.UserRepository;
import itcast.mail.application.MailService;
import itcast.mail.dto.request.MailContent;
import itcast.mail.dto.request.SendMailRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BlogSendService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final MailService mailService;

    public void sendBlogForEmail(LocalDate today) {
        LocalDateTime sendAt = today.atTime(9, 0);

        sendBlogsByInterest(sendAt, Interest.FRONTEND);
        sendBlogsByInterest(sendAt, Interest.BACKEND);
    }

    private void sendBlogsByInterest(LocalDateTime sendAt, Interest interest) {
        List<Blog> blogs = blogRepository.findAllBySendAtAndInterest(sendAt, interest);

        List<MailContent> mailContents = blogs.stream()
                .map(blog -> MailContent.of(
                        blog.getTitle(),
                        blog.getContent(),
                        blog.getLink(),
                        blog.getThumbnail())
                )
                .toList();
        List<String> emails = retrieveUserEmails(interest);

        SendMailRequest mailRequest = SendMailRequest.of(emails, mailContents);
        mailService.send(mailRequest);
    }

    private List<String> retrieveUserEmails(Interest interest) {
        return userRepository.findAllByInterest(interest)
                .stream()
                .map(User::getEmail)
                .toList();
    }
}
