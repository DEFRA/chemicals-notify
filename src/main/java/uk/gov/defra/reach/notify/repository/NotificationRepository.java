package uk.gov.defra.reach.notify.repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.defra.reach.notify.entity.Notification;
import uk.gov.defra.reach.notify.entity.NotificationState;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
  List<Notification> findByStateIn(Collection<NotificationState> states);
}
