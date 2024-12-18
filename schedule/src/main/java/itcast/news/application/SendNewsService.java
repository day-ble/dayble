package itcast.news.application;

import itcast.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendNewsService {

    private final NewsRepository newsRepository;

    public void selectNews() {

    }

    public void sendNews() {

    }
}
