package itcast.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import itcast.domain.blog.QBlog;
import itcast.domain.blog.enums.BlogStatus;
import itcast.domain.user.enums.Interest;
import itcast.dto.response.AdminBlogResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static itcast.domain.blog.QBlog.blog;

@Repository
@RequiredArgsConstructor
public class CustomBlogRepositoryImpl implements CustomBlogRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AdminBlogResponse> findBlogByCondition(BlogStatus status, Interest interest, LocalDate startAt, LocalDate endAt, Pageable pageable) {
        QBlog blog = QBlog.blog;

        JPQLQuery<AdminBlogResponse> query = queryFactory
                .select(Projections.constructor(AdminBlogResponse.class,
                        blog.id,
                        blog.platform,
                        blog.title,
                        blog.content,
                        blog.originalContent,
                        blog.interest,
                        blog.publishedAt,
                        blog.rating,
                        blog.link,
                        blog.thumbnail,
                        blog.status,
                        blog.sendAt
                ))
                .from(blog)
                .where(statusEq(status), interestEq(interest), sendAtBetween(startAt, endAt))
                .orderBy(blog.sendAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<AdminBlogResponse> content = query.fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(blog.count())
                .from(blog)
                .where(statusEq(status), interestEq(interest), sendAtBetween(startAt, endAt));

        return new PageImpl<>(content, pageable, countQuery.fetchOne());
    }

    private BooleanExpression statusEq(BlogStatus status) {
        if(status == null){
            return null;
        }
        return blog.status.eq(status);
    }

    private BooleanExpression interestEq(Interest interest) {
        if(interest == null){
            return null;
        }
        return blog.interest.eq(interest);
    }

    private BooleanExpression sendAtBetween(LocalDate startAt, LocalDate endAt) {
        if (startAt == null && endAt == null) {
            return null;
        } else if (startAt == null) {
            return blog.sendAt.loe(endAt);
        } else if (endAt == null) {
            return blog.sendAt.goe(startAt);
        } else {
            return blog.sendAt.between(startAt, endAt);
        }
    }
}
