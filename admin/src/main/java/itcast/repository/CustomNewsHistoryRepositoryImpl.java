package itcast.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import itcast.domain.newsHistory.QNewsHistory;
import itcast.dto.response.AdminNewsHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static itcast.domain.newsHistory.QNewsHistory.newsHistory;

@Repository
@RequiredArgsConstructor
public class CustomNewsHistoryRepositoryImpl implements CustomNewsHistoryRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AdminNewsHistoryResponse> findNewsHistoryListByCondition(Long userId, Long newsId, LocalDate createdAt, Pageable pageable) {
        QNewsHistory newsHistory = QNewsHistory.newsHistory;

        JPQLQuery<AdminNewsHistoryResponse> query = queryFactory
                .select(Projections.constructor(AdminNewsHistoryResponse.class,
                        newsHistory.id,
                        newsHistory.user.id,
                        newsHistory.news.id,
                        newsHistory.createdAt,
                        newsHistory.modifiedAt
                ))
                .from(newsHistory)
                .where(userIdEq(userId), newsIdEq(newsId), createdAtEq(createdAt))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<AdminNewsHistoryResponse> content = query.fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(newsHistory.count())
                .from(newsHistory)
                .where(userIdEq(userId), newsIdEq(newsId), createdAtEq(createdAt));

        return new PageImpl<>(content, pageable, countQuery.fetchOne());
    }

    private BooleanExpression newsIdEq(Long newsId) {
        if(newsId == null) {
            return null;
        }
        return newsHistory.news.id.eq(newsId);
    }

    private BooleanExpression userIdEq(Long userId) {
        if(userId == null) {
            return null;
        }
        return newsHistory.user.id.eq(userId);
    }

    private BooleanExpression createdAtEq(LocalDate createdAt) {
        if(createdAt == null) {
            return null;
        }
        LocalDateTime startAt = LocalDateTime.of(createdAt, LocalTime.of(0, 0,0));
        LocalDateTime endAt = LocalDateTime.of(createdAt, LocalTime.of(23, 59, 59));
        return newsHistory.createdAt.between(startAt, endAt);
    }
}
