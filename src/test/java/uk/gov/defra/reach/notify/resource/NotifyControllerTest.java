package uk.gov.defra.reach.notify.resource;

import static org.mockito.Mockito.verify;

import com.microsoft.applicationinsights.TelemetryClient;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.defra.reach.notify.entity.NotificationRequest;
import uk.gov.defra.reach.notify.entity.NotificationTemplate;
import uk.gov.defra.reach.notify.service.NotifyService;
import uk.gov.defra.reach.notify.utils.TestUtils;


@ExtendWith(SpringExtension.class)
public class NotifyControllerTest {

  @Mock
  private NotifyService mockNotifyService;

  @Mock
  private TelemetryClient mockTelemetryClient;

  @InjectMocks
  private NotifyController notifyController;

  @Test
  public void sendEmail_VerifySendEmailCalled_WhenNotificationRequestCorrect() {

    NotificationRequest notificationRequest = TestUtils.createTestNotificationRequest("testEvent");
    NotificationTemplate template = mockNotifyService.getEventTemplate(notificationRequest.getEvent());
    notifyController.sendEmail(notificationRequest);

    verify(mockNotifyService).sendEmail(template, notificationRequest.getEmailAddress(), notificationRequest.getPersonalisation());
  }
}
