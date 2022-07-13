package uk.gov.defra.reach.notify.entity;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.defra.reach.notify.converter.NotificationStateConverter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notification_history", schema = "notify")
public class NotificationHistory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "notification_history_id")
  private int id;
  @Column(name = "notification_id")
  private UUID notificationId;
  @Column(name = "from_state")
  @Convert(converter = NotificationStateConverter.class)
  private NotificationState fromState;
  @Column(name = "to_state")
  @Convert(converter = NotificationStateConverter.class)
  private NotificationState toState;

  public static NotificationHistory initializeNotificationHistory(Notification notification) {
    return NotificationHistory.builder()
        .notificationId(notification.getNotificationId())
        .fromState(null)
        .toState(notification.getState())
        .build();

  }
}
