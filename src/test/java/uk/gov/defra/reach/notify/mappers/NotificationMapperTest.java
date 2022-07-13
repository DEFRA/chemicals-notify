package uk.gov.defra.reach.notify.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.defra.reach.notify.entity.Notification;
import uk.gov.defra.reach.notify.utils.TestUtils;
import uk.gov.service.notify.SendEmailResponse;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NotificationMapperTest {

  private Notification expectedNotification;

  @BeforeEach
  public void setUp() {
    expectedNotification = TestUtils.createNotification();
  }

  @Test
  @SuppressWarnings("squid:S2699")
  public void mapper_shouldThrowInstantiationException() {
    assertThatThrownBy(() -> {
      new NotificationMapper();
    }).isInstanceOf(InstantiationException.class).hasMessage("NotificationMapper cannot be instantiated");
  }

  @Test
  @SuppressWarnings("squid:S2699")
  public void map_shouldMapNotifyEmailResponseToNotification_whenValid() {

    SendEmailResponse sendEmailResponse = TestUtils.createSendEmailResponse();
    Notification actualNotification = NotificationMapper.map(sendEmailResponse);

    TestUtils.compareNotification(actualNotification, expectedNotification);
  }

  @Test
  @SuppressWarnings("squid:S2699")
  public void map_shouldMapNotifyEmailResponseToNotification_whenEmailIsMissing() {

    SendEmailResponse sendEmailResponse = TestUtils.createSendEmailResponseWithoutEmail();
    Notification actualNotification = NotificationMapper.map(sendEmailResponse);
    expectedNotification.setFromEmail(null);

    TestUtils.compareNotification(actualNotification, expectedNotification);
  }

  @Test
  @SuppressWarnings("squid:S2699")
  public void map_shouldMapNotifyEmailResponseToNotification_whenReferenceIsMissing() {

    SendEmailResponse sendEmailResponse = TestUtils.createSendEmailResponseWithoutReference();
    Notification actualNotification = NotificationMapper.map(sendEmailResponse);
    expectedNotification.setReference(null);

    TestUtils.compareNotification(actualNotification, expectedNotification);
  }
}
