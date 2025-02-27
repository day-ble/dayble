package com.dayble.blog.news.domain;


import static com.dayble.blog.news.domain.QNews.news;

import com.dayble.blog.admin.controller.dto.response.AdminNewsResponse;
import com.dayble.blog.news.domain.enums.NewsStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomNewsRepositoryImpl implements CustomNewsRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AdminNewsResponse> findNewsByCondition(NewsStatus status, LocalDate startAt, LocalDate endAt, Pageable pageable) {
        QNews news = QNews.news;

        JPQLQuery<AdminNewsResponse> query = queryFactory
                .select(Projections.constructor(AdminNewsResponse.class,
                        news.id,
                        news.title,
                        news.content,
                        news.originalContent,
                        news.interest,
                        news.publishedAt,
                        news.rating,
                        news.link,
                        news.thumbnail,
                        news.status,
                        news.sendAt
                ))
                .from(news)
                .where(statusEq(status), sendAtBetween(startAt, endAt))
                .orderBy(news.sendAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<AdminNewsResponse> content = query.fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(news.count())
                .from(news)
                .where(statusEq(status), sendAtBetween(startAt, endAt));

        return new PageImpl<>(content, pageable, countQuery.fetchOne());
    }

    private BooleanExpression statusEq(NewsStatus status) {
        if (status == null) {
            return null;
        }
        return news.status.eq(status);
    }

    private BooleanExpression sendAtBetween(LocalDate startAt, LocalDate endAt) {
        if (startAt == null && endAt == null) {
            return null;
        } else if (startAt == null) {
            return news.sendAt.loe(endAt);
        } else if (endAt == null) {
            return news.sendAt.goe(startAt);
        } else {
            return news.sendAt.between(startAt, endAt);
        }
    }
}
