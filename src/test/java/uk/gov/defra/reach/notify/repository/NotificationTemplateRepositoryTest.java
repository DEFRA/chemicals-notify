package uk.gov.defra.reach.notify.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import uk.gov.defra.reach.notify.entity.NotificationTemplate;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration
@TestPropertySource("classpath:application-int.properties")
public class NotificationTemplateRepositoryTest {

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Autowired
  NotificationTemplateRepository notificationTemplateRepository;

  @Test
  public void sanity_check() {
    assertThat(jdbcTemplate).isNotNull();
    assertThat(notificationTemplateRepository).isNotNull();
  }

  @Test
  public void should_find_one() {
    String EVENT = "XXXX_TEST";
    UUID templateId = UUID.randomUUID();

    int affected = jdbcTemplate.update("INSERT INTO notify.notification_template (event, template_id) VALUES (?, ?)", EVENT, templateId);
    assertThat(affected).isEqualTo(1);
    Optional<NotificationTemplate> eventTemplate = notificationTemplateRepository.findById(EVENT);
    assertThat(eventTemplate.isPresent()).isTrue();
    assertThat(eventTemplate.get().getEvent()).isEqualTo(EVENT);
    assertThat(eventTemplate.get().getTemplateId()).isEqualTo(templateId.toString());
  }
}
