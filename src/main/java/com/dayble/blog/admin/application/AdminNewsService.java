package com.dayble.blog.admin.application;

import static com.dayble.blog.global.exception.ErrorCodes.INVALID_USER;
import static com.dayble.blog.global.exception.ErrorCodes.NEWS_NOT_FOUND;
import static com.dayble.blog.global.exception.ErrorCodes.NEWS_SELECT_ERROR;
import static com.dayble.blog.global.exception.ErrorCodes.USER_NOT_FOUND;

import com.dayble.blog.admin.controller.dto.request.AdminNewsRequest;
import com.dayble.blog.admin.controller.dto.response.AdminNewsResponse;
import com.dayble.blog.admin.domain.AdminRepository;
import com.dayble.blog.global.exception.DaybleApplicationException;
import com.dayble.blog.news.domain.News;
import com.dayble.blog.news.domain.NewsRepository;
import com.dayble.blog.news.domain.enums.NewsStatus;
import com.dayble.blog.user.domain.User;
import com.dayble.blog.user.domain.UserRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminNewsService {

    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public AdminNewsResponse createNews(Long userId, AdminNewsRequest adminNewsRequest) {
        isAdmin(userId);
        News news = AdminNewsRequest.toEntity(adminNewsRequest);
        News savedNews = newsRepository.save(news);
        return new AdminNewsResponse(savedNews);
    }

    public Page<AdminNewsResponse> retrieveNewsList(Long userId, NewsStatus status, LocalDate startAt, LocalDate endAt,
                                                    int page, int size) {
        isAdmin(userId);
        Pageable pageable = PageRequest.of(page, size);
        return newsRepository.findNewsByCondition(status, startAt, endAt, pageable);
    }

    public AdminNewsResponse retrieveNews(Long userId, Long newsId) {
        isAdmin(userId);
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new DaybleApplicationException(NEWS_SELECT_ERROR));
        return new AdminNewsResponse(news);
    }

    @Transactional
    public AdminNewsResponse updateNews(Long userId, Long newsId, AdminNewsRequest adminNewsRequest) {
        isAdmin(userId);
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new DaybleApplicationException(NEWS_NOT_FOUND));

        news.update(adminNewsRequest.title(),
                adminNewsRequest.content(),
                adminNewsRequest.originalContent(),
                adminNewsRequest.interest(),
                adminNewsRequest.publishedAt(),
                adminNewsRequest.rating(),
                adminNewsRequest.link(),
                adminNewsRequest.thumbnail(),
                adminNewsRequest.status(),
                adminNewsRequest.sendAt()
        );

        return new AdminNewsResponse(news);
    }

    @Transactional
    public AdminNewsResponse deleteNews(Long userId, Long newsId) {
        isAdmin(userId);
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new DaybleApplicationException(NEWS_NOT_FOUND));
        newsRepository.delete(news);
        return new AdminNewsResponse(news);
    }

    private void isAdmin(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new DaybleApplicationException(USER_NOT_FOUND));
        String email = user.getKakaoEmail();
        if (!adminRepository.existsByEmail(email)) {
            throw new DaybleApplicationException(INVALID_USER);
        }
    }
}
