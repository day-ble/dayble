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
import itcast.message.application.MessageService;
import itcast.message.dto.request.MessageContent;
import itcast.message.dto.request.RecieverPhoneNumber;
import itcast.message.dto.request.SendMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BlogSendService {

    private final BlogRepository blogRepository;
    private final BlogHistoryRepository blogHistoryRepository;
    private final UserRepository userRepository;

    private final MailService mailService;
    private final MessageService messageService;

    public void sendBlogForEmail(LocalDate today) {
        sendBlogsByInterestAndCreateHistory(today, Interest.FRONTEND);
        sendBlogsByInterestAndCreateHistory(today, Interest.BACKEND);
    }
    public void sendBlogForMessage(LocalDate today) {
        sendMessagesByInterestAndCreateHistory(today, Interest.FRONTEND);
        sendMessagesByInterestAndCreateHistory(today, Interest.BACKEND);
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

        createBlogHistoryByEmail(blogs, emails);
    }

    private List<String> retrieveUserEmails(Interest interest) {
        return userRepository.findAllByInterest(interest)
                .stream()
                .map(User::getEmail)
                .toList();
    }

    private void createBlogHistoryByEmail(List<Blog> blogs, List<String> userEmails) {
        List<User> users = userEmails.stream()
                .map(userRepository::findByEmail)
                .toList();

        List<BlogHistory> blogHistories = users.stream()
                .flatMap(user -> blogs.stream()
                        .map(blog -> BlogHistory.builder()
                                .user(user)
                                .blog(blog)
                                .build()))
                .toList();

        blogHistoryRepository.saveAll(blogHistories);
    }

    private void sendMessagesByInterestAndCreateHistory(LocalDate sendAt, Interest interest) {
        List<Blog> blogs = blogRepository.findAllBySendAtAndInterest(sendAt, interest);

        List<MessageContent> messageContents = blogs.stream()
                .map(blog -> new MessageContent(
                        blog.getTitle(),
                        blog.getContent(),
                        blog.getLink()
                ))
                .collect(Collectors.toList());

        List<RecieverPhoneNumber> phoneNumbers = retrieveUserPhoneNumbers(interest);

        SendMessageRequest sendMessageRequest = new SendMessageRequest(messageContents, phoneNumbers);
        messageService.sendMessages(sendMessageRequest);

        createBlogHistoryByMessage(blogs, phoneNumbers);
    }

    private List<RecieverPhoneNumber> retrieveUserPhoneNumbers(Interest interest) {
        return userRepository.findAllByInterest(interest)
                .stream()
                .map(user -> new RecieverPhoneNumber(user.getPhoneNumber()))
                .collect(Collectors.toList());
    }

    private void createBlogHistoryByMessage(List<Blog> blogs, List<RecieverPhoneNumber> phoneNumbers) {
        List<User> users = phoneNumbers.stream()
                .map(phoneNumber -> userRepository.findByPhoneNumber(phoneNumber.phoneNumber()))
                .toList();

        List<BlogHistory> blogHistories = users.stream()
                .flatMap(user -> blogs.stream()
                        .map(blog -> BlogHistory.builder()
                                .user(user)
                                .blog(blog)
                                .build()))
                .collect(Collectors.toList());

        blogHistoryRepository.saveAll(blogHistories);
    }
}


