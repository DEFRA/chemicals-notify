package uk.gov.defra.reach.notify.entity;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notification_queue_history", schema = "notify")
public class NotificationQueueHistory {
  @Id
  @Column(name = "notification_queue_id")
  private UUID notificationQueueId;
  @Column(name = "from_state")
  private String fromState;
  @Column(name = "to_state")
  private String toState;
}
