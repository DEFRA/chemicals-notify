package uk.gov.defra.reach.notify.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notification_template", schema = "notify")
public class NotificationTemplate {
  @Id
  @Column(name = "event")
  private String event;
  @Column(name = "template_id")
  private String templateId;
}
