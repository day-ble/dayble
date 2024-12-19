package itcast.blog.application;

import itcast.blog.repository.BlogRepository;
import itcast.domain.blog.Blog;
import itcast.domain.blog.enums.Platform;
import itcast.domain.user.enums.Interest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BlogSelectService {

    private static final int BLOG_MIN_RATING = 7;
    private static final int VELOG_COUNT = 2;
    private static final int YOZM_COUNT = 1;

    private final BlogRepository blogRepository;

    public void selectBlogs(LocalDate today) {
        // FrontEnd
        List<Blog> velogWithFront = retrieveToSelect(Platform.VELOG, Interest.FRONTEND, VELOG_COUNT, today);
        List<Blog> yozmWithFront = retrieveToSelect(Platform.YOZM, Interest.FRONTEND, YOZM_COUNT, today);

        // BackEnd
        List<Blog> velogWithBack = retrieveToSelect(Platform.VELOG, Interest.BACKEND, VELOG_COUNT, today);
        List<Blog> yozmWithBack = retrieveToSelect(Platform.YOZM, Interest.BACKEND, YOZM_COUNT, today);

        List<Blog> blogs = Stream.of(velogWithFront, yozmWithFront, velogWithBack, yozmWithBack)
                .flatMap(List::stream)
                .toList();

        updateSendAt(blogs, today);
    }

    private List<Blog> retrieveToSelect(Platform platform, Interest interest, int count, LocalDate today) {
        LocalDate recentDate = today.minusDays(7);
        LocalDateTime recentDateTime = recentDate.atStartOfDay();

        Pageable pageable = PageRequest.of(0, count);
        return blogRepository.findByBlogForSelection(platform, interest, recentDateTime, BLOG_MIN_RATING, pageable);
    }

    private void updateSendAt(List<Blog> blogs, LocalDate today) {
        LocalDate sendDate = today.plusDays(2);
        blogs.forEach(blog -> {
            blog.updateSendAt(sendDate);
            blogRepository.save(blog);
        });
    }
}
