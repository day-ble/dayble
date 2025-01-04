package itcast.mail.repository;

import itcast.domain.mailEvent.MailEvents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailEventsRepository extends JpaRepository<MailEvents, Long> {
}
