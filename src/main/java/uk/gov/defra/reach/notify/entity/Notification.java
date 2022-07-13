package uk.gov.defra.reach.notify.entity;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@Table(name = "notification", schema = "notify")
public class Notification {
  @Id
  @Column(name = "notification_id")
  private UUID notificationId;
  @Column(name = "reference")
  private String reference;
  @Column(name = "template_id")
  private UUID templateId;
  @Column(name = "template_version")
  private int templateVersion;
  @Column(name = "template_uri")
  private String templateUri;
  @Column(name = "body")
  private String body;
  @Column(name = "subject")
  private String subject;
  @Column(name = "from_email")
  private String fromEmail;
  @Column(name = "state")
  @Convert(converter = NotificationStateConverter.class)
  private NotificationState state;
  @Column(name = "updatedAt")
  @Temporal(TemporalType.TIMESTAMP)
  @Builder.Default
  private Calendar updatedAt = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
}
