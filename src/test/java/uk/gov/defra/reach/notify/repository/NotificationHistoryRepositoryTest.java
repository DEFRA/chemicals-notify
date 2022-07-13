package uk.gov.defra.reach.notify.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration
@TestPropertySource("classpath:application-int.properties")
public class NotificationHistoryRepositoryTest {

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Autowired
  NotificationHistoryRepository notificationHistoryRepository;

  @Test
  public void sanityCheck() {
    assertThat(jdbcTemplate).isNotNull();
    assertThat(notificationHistoryRepository).isNotNull();
  }

}
