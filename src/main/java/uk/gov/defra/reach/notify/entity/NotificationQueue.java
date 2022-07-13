package uk.gov.defra.reach.notify.entity;

import java.util.Calendar;
import java.util.Map;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKeyColumn;
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
@Table(name = "notification", schema = "notify")
public class NotificationQueue {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "notification_queue_id")
  private Integer notificationQueueId;
  @Column(name = "template_id")
  private UUID templateId;
  @Column(name = "event")
  private String event;
  @Column(name = "created_at")
  private Calendar createdAt;
  @ElementCollection
  @JoinTable(name = "notification_queue_attributes", schema = "notify", joinColumns = @JoinColumn(name = "notification_queue_id"))
  @MapKeyColumn(name = "place_holder")
  @Column(name = "value")
  private Map<String, String> personalisation;
}
