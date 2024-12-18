package itcast.news.application;

import itcast.domain.news.News;
import itcast.exception.ItCastApplicationException;
import itcast.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static itcast.exception.ErrorCodes.TODAY_NEWS_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SendNewsService {

    private static final int NEWS_SIZE = 3;
    private final NewsRepository newsRepository;

    private static List<News> NEWS_LIST = Arrays.asList();
    public void selectNews() {
        NEWS_LIST = newsRepository.findTop3ByTodayOrderByRating();
        if (NEWS_LIST.isEmpty() || NEWS_LIST.size() < NEWS_SIZE) {
            throw new ItCastApplicationException(TODAY_NEWS_NOT_FOUND);
        }

    }

    public void sendNews() {

    }
}
