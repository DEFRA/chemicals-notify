package uk.gov.defra.reach.notify.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.defra.reach.notify.entity.NotificationHistory;

@Repository
public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, UUID> {
}
