package itcast.mail.repository;

import itcast.domain.mailEvent.MailEvents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MailEventsRepository extends JpaRepository<MailEvents, Long> {

    @Query("SELECT m FROM MailEvents m WHERE m.user.id = :userId AND m.createdAt BETWEEN :startOfDay AND :endOfDay")
    List<MailEvents> findByUserIdAndCreatedAtBetween(
            @Param("userId") Long userId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );
}
