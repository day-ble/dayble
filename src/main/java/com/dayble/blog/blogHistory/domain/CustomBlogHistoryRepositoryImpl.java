package com.dayble.blog.blogHistory.domain;


import static com.dayble.blog.blogHistory.domain.QBlogHistory.blogHistory;

import com.dayble.blog.admin.controller.dto.response.AdminBlogHistoryResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomBlogHistoryRepositoryImpl implements CustomBlogHistoryRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AdminBlogHistoryResponse> findBlogHistoryListByCondition(Long userId, Long blogId, LocalDate createdAt,
                                                                         Pageable pageable) {
        QBlogHistory blogHistory = QBlogHistory.blogHistory;

        JPQLQuery<AdminBlogHistoryResponse> query = queryFactory
                .select(Projections.constructor(AdminBlogHistoryResponse.class,
                        blogHistory.id,
                        blogHistory.user.id,
                        blogHistory.blog.id,
                        blogHistory.createdAt,
                        blogHistory.modifiedAt
                ))
                .from(blogHistory)
                .where(userIdEq(userId), blogIdEq(blogId), createdAtEq(createdAt))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<AdminBlogHistoryResponse> content = query.fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(blogHistory.count())
                .from(blogHistory)
                .where(userIdEq(userId), blogIdEq(blogId), createdAtEq(createdAt));

        return new PageImpl<>(content, pageable, countQuery.fetchOne());
    }

    @Override
    public List<AdminBlogHistoryResponse> downloadBlogHistoryListByCondition(Long userId, Long blogId,
                                                                             LocalDate startAt, LocalDate endAt) {
        QBlogHistory blogHistory = QBlogHistory.blogHistory;

        JPQLQuery<AdminBlogHistoryResponse> query = queryFactory
                .select(Projections.constructor(AdminBlogHistoryResponse.class,
                        blogHistory.id,
                        blogHistory.user.id,
                        blogHistory.blog.id,
                        blogHistory.createdAt,
                        blogHistory.modifiedAt
                ))
                .from(blogHistory)
                .where(userIdEq(userId), blogIdEq(blogId), createAtBetween(startAt, endAt));

        List<AdminBlogHistoryResponse> content = query.fetch();
        return content;
    }

    private BooleanExpression userIdEq(Long userId) {
        if (userId == null) {
            return null;
        }
        return blogHistory.user.id.eq(userId);
    }

    private BooleanExpression blogIdEq(Long blogId) {
        if (blogId == null) {
            return null;
        }
        return blogHistory.blog.id.eq(blogId);
    }

    private BooleanExpression createdAtEq(LocalDate createdAt) {
        if (createdAt == null) {
            return null;
        }

        LocalDateTime startAt = LocalDateTime.of(createdAt, LocalTime.of(0, 0, 0));
        LocalDateTime endAt = LocalDateTime.of(createdAt, LocalTime.of(23, 59, 59));
        return blogHistory.createdAt.between(startAt, endAt);
    }

    private BooleanExpression createAtBetween(LocalDate startAt, LocalDate endAt) {
        if (startAt == null && endAt == null) {
            return null;
        } else if (startAt == null) {
            return blogHistory.createdAt.loe(endAt.atTime(23, 59, 59));
        } else if (endAt == null) {
            return blogHistory.createdAt.goe(startAt.atStartOfDay());
        } else {
            return blogHistory.createdAt.between(startAt.atStartOfDay(), endAt.atTime(23, 59, 59));
        }
    }
}
