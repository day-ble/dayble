package itcast.news.application;

import itcast.domain.news.News;
import itcast.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SendNewsService {


    private static final int YESTERDAY = 1;
    private static final int ALARM_HOUR = 2;
    private static final int ALARM_DAY = 2;

    private final NewsRepository newsRepository;

    @Transactional
    public void selectNews() {
        LocalDate yesterday = LocalDate.now().minusDays(YESTERDAY);
        List<News> newsList = newsRepository.findRatingTot3ByCreatedAtOrdarByRating(yesterday);

        LocalDateTime sendAt = LocalDateTime.now().plusDays(ALARM_DAY).plusHours(ALARM_HOUR);

        newsList.forEach(alarm -> {
            alarm.newsUpdate(sendAt);
        });
    }

    public void sendNews() {
        List<News> sendNews = newsRepository.findAllBySendAt();


    }
}
