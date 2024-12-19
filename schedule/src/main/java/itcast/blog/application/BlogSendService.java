package itcast.blog.application;

import itcast.blog.repository.BlogHistoryRepository;
import itcast.blog.repository.BlogRepository;
import itcast.domain.blog.Blog;
import itcast.domain.blogHistory.BlogHistory;
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
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BlogSendService {

    private final BlogRepository blogRepository;
    private final BlogHistoryRepository blogHistoryRepository;
    private final UserRepository userRepository;

    private final MailService mailService;

    public void sendBlogForEmail(LocalDate today) {
        sendBlogsByInterestAndCreateHistory(today, Interest.FRONTEND);
        sendBlogsByInterestAndCreateHistory(today, Interest.BACKEND);
    }

    /**
     * @param sendAt:   보낼 날짜 확인
     * @param interest: 백엔드 / 프론트엔드
     *                  - 파트 별로 나누어 Blog를 3개씩 추출하여 각 파트별 User들에게 전송하고, 
     *                  - 발송한 내역을 User 1명 당 Blog 3개씩 매칭되어 BlogHistory에 저장한다.
     */
    private void sendBlogsByInterestAndCreateHistory(LocalDate sendAt, Interest interest) {
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

        createBlogHistory(blogs, emails);
    }

    private List<String> retrieveUserEmails(Interest interest) {
        return userRepository.findAllByInterest(interest)
                .stream()
                .map(User::getEmail)
                .toList();
    }

    private void createBlogHistory(List<Blog> blogs, List<String> userEmails) {
        List<User> users = userEmails.stream()
                .map(userRepository::findByEmail)
                .toList();

        List<BlogHistory> blogHistories = users.stream()
                .flatMap(user -> blogs.stream() // user 순회하여 모든 blog와 매칭
                        .map(blog -> BlogHistory.builder()
                                .user(user)
                                .blog(blog)
                                .build()))
                .toList();

        blogHistoryRepository.saveAll(blogHistories);
    }
}
