package itcast.blog.repository;

import itcast.domain.blog.Blog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    @Query("SELECT b.link FROM Blog b")
    List<String> findAllLinks();
}
