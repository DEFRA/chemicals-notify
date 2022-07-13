package uk.gov.defra.reach.notify.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import uk.gov.defra.reach.notify.entity.Notification;
import uk.gov.defra.reach.notify.entity.NotificationState;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource("classpath:application-int.properties")
@ContextConfiguration
public class NotificationRepositoryTest {

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Autowired
  NotificationRepository notificationRepository;

  private Notification notification;

  @BeforeEach
  public void setup() {
    notification = Notification.builder()
        .notificationId(UUID.randomUUID())
        .templateId(UUID.randomUUID())
        .templateVersion(1)
        .templateUri("/anything")
        .body("This is your email")
        .subject("TEST")
        .fromEmail("dd@dd.com")
        .state(NotificationState.PENDING)
        .updatedAt(Calendar.getInstance(TimeZone.getTimeZone("UTC")))
        .build();
  }

  @Test
  public void sanity_check() {
    assertThat(jdbcTemplate).isNotNull();
    assertThat(notificationRepository).isNotNull();
  }

  @Test
  public void default_status_should_be_pending() {
    final String sql = "insert \n" +
            "    into\n" +
            "        notify.notification\n" +
            "        (body, from_email, reference, subject, template_id, template_uri, template_version, notification_id, updated_at) \n" +
            "    values\n" +
            "        (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    jdbcTemplate.update(sql, notification.getBody(), notification.getFromEmail(), notification.getReference()
            , notification.getSubject(), notification.getTemplateId()
            , notification.getTemplateUri(), notification.getTemplateVersion(), notification.getNotificationId()
            , Calendar.getInstance(TimeZone.getTimeZone("UTC")));
    Optional<Notification> byId = notificationRepository.findById(notification.getNotificationId());
    assertThat(byId.isPresent()).isTrue();
    assertThat(byId.get().getState()).isEqualTo(NotificationState.PENDING);
  }

  @Test
  public void findByStatePending_shouldReturnEmpty_whenDbIsEmpty() {
    List<Notification> pendingNotifications = notificationRepository.findByStateIn(NotificationState.getNonFinalStates());
    assertThat(pendingNotifications.size()).isEqualTo(0);
  }

  @Test
  public void findByStatePending_shouldReturnList_whenDbIsNotEmpty() {
    notificationRepository.save(notification);
    List <Notification> pendingNotifications = notificationRepository.findByStateIn(NotificationState.getNonFinalStates());
    assertThat(pendingNotifications.size()).isEqualTo(1);
  }

}
