package uk.gov.defra.reach.notify.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.defra.reach.notify.entity.Notification;
import uk.gov.defra.reach.notify.entity.NotificationState;
import uk.gov.defra.reach.notify.entity.NotificationTemplate;
import uk.gov.defra.reach.notify.repository.NotificationHistoryRepository;
import uk.gov.defra.reach.notify.repository.NotificationRepository;
import uk.gov.defra.reach.notify.repository.NotificationTemplateRepository;
import uk.gov.defra.reach.notify.utils.TestUtils;
import uk.gov.service.notify.NotificationClient;
import uk.gov.service.notify.NotificationClientException;
import uk.gov.service.notify.SendEmailResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.defra.reach.notify.entity.NotificationState.PENDING;
import static uk.gov.defra.reach.notify.entity.NotificationState.DELIVERED;

@ExtendWith(SpringExtension.class)
public class NotifyServiceTest {

  private NotifyService notifyService;

  @Mock
  NotificationClient mockNotificationClient;

  @Mock
  NotificationTemplateRepository mockNotificationTemplateRepository;

  @Mock
  NotificationRepository mockNotificationRepository;

  @Mock
  NotificationHistoryRepository mockNotificationHistoryRepository;

  @Mock
  uk.gov.service.notify.Notification notificationResponse;

  private static final String TEST_NOTIFICATION_TYPE = "notificationType";
  private static final String TEST_EMAIL_ADDRESS = "testEmailAddress";

  @BeforeEach
  public void setUp() {
    notifyService = new NotifyService(mockNotificationClient, mockNotificationTemplateRepository, mockNotificationRepository, mockNotificationHistoryRepository);
  }

  private NotificationTemplate createTestNotificationTemplate(UUID templateUUID){
    NotificationTemplate notificationTemplate = new NotificationTemplate();
    notificationTemplate.setEvent(TEST_NOTIFICATION_TYPE);
    notificationTemplate.setTemplateId(templateUUID.toString());

    return notificationTemplate;
  }

  @Test
  public void sendEmail_shouldReturnNotification_whenNotificationIsValid() throws NotificationClientException {
    Map<String, String> testPersonalisation = new HashMap<>();
    testPersonalisation.put("test", "test");

    SendEmailResponse sendEmailResponse = TestUtils.createSendEmailResponse();

    NotificationTemplate notificationTemplate = createTestNotificationTemplate(sendEmailResponse.getTemplateId());
    when(mockNotificationTemplateRepository.findById(TEST_NOTIFICATION_TYPE)).thenReturn(
        Optional.of(notificationTemplate));

    when(mockNotificationClient.sendEmail(notificationTemplate.getTemplateId(), TEST_EMAIL_ADDRESS, testPersonalisation, null)).thenReturn(sendEmailResponse);

    Notification expectedNotificationResponse = TestUtils.createNotification();
    expectedNotificationResponse.setState(PENDING);

    NotificationTemplate template = notifyService.getEventTemplate(TEST_NOTIFICATION_TYPE);
    notifyService.sendEmail(template, TEST_EMAIL_ADDRESS, testPersonalisation);

    verify(mockNotificationClient).sendEmail(notificationTemplate.getTemplateId(), TEST_EMAIL_ADDRESS, testPersonalisation, null);
    verify(mockNotificationTemplateRepository).findById(TEST_NOTIFICATION_TYPE);

    final ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
    verify(mockNotificationRepository).save(captor.capture());
    TestUtils.compareNotification(captor.getValue(), expectedNotificationResponse);
  }

  @Test
  public void sendEmail_shouldLogError_whenNotifyFails() throws NotificationClientException{
    Map<String, String> testPersonalisation = new HashMap<>();
    testPersonalisation.put("test", "test");

    SendEmailResponse sendEmailResponse = TestUtils.createSendEmailResponse();

    NotificationTemplate notificationTemplate = createTestNotificationTemplate(sendEmailResponse.getTemplateId());
    when(mockNotificationTemplateRepository.findById(TEST_NOTIFICATION_TYPE)).thenReturn(
            Optional.of(notificationTemplate));

    when(mockNotificationClient.sendEmail(notificationTemplate.getTemplateId(), TEST_EMAIL_ADDRESS, testPersonalisation, null)).thenReturn(sendEmailResponse);

    Notification expectedNotificationResponse = TestUtils.createNotification();
    expectedNotificationResponse.setState(PENDING);

    when(mockNotificationClient.sendEmail(any(), any(), any(), any()))
          .thenThrow(NotificationClientException.class);

    NotificationTemplate template = notifyService.getEventTemplate(TEST_NOTIFICATION_TYPE);
    notifyService.sendEmail(template, TEST_EMAIL_ADDRESS, testPersonalisation);

    verify(mockNotificationRepository, never()).save(any(Notification.class));
  }

  @Test
  public void getEventTemplate_shouldReturnNotificationTemplate_whenEventIsValid() {
    String validTemplateId = "validTemplateId";

    when(mockNotificationTemplateRepository.findById(validTemplateId)).thenReturn(
        Optional.of(new NotificationTemplate()));

    notifyService.getEventTemplate(validTemplateId);

    verify(mockNotificationTemplateRepository).findById(validTemplateId);
  }

  @Test
  public void getEventTemplate_shouldThrowIllegalArgumentException_whenEventNotValid() {
    String invalidTemplateId = "invalidTemplateId";

    Assertions.assertThrows(MissingTemplateException.class, () -> notifyService.getEventTemplate(invalidTemplateId));

    verify(mockNotificationTemplateRepository).findById(invalidTemplateId);
  }

  @Test
  public void getNotificationStatus_shouldUpdateAllStatuses_whenPending() throws NotificationClientException{
    Notification notification = TestUtils.createNotification();
    notification.setState(PENDING);
    List<Notification> pendingNotifications = new ArrayList<>();
    pendingNotifications.add(notification);

    when(mockNotificationRepository.findByStateIn(NotificationState.getNonFinalStates())).thenReturn(pendingNotifications);
    when(mockNotificationClient.getNotificationById(notification.getNotificationId().toString()))
            .thenReturn(notificationResponse);
    when(notificationResponse.getStatus()).thenReturn("delivered");

    notifyService.getNotificationStatus();

    verify(mockNotificationHistoryRepository).save(any());
    verify(mockNotificationRepository).save(notification);
    assertThat(notification.getState()).isEqualTo(DELIVERED);
  }

  @Test
  public void getNotificationStatus_shouldNotWriteToDb_whenNotifyFails() throws NotificationClientException {
    Notification notification = TestUtils.createNotification();
    notification.setState(PENDING);
    List<Notification> pendingNotifications = new ArrayList<>();
    pendingNotifications.add(notification);

    when(mockNotificationRepository.findByStateIn(NotificationState.getNonFinalStates())).thenReturn(pendingNotifications);
    when(mockNotificationClient.getNotificationById(notification.getNotificationId().toString()))
            .thenThrow(NotificationClientException.class);

    notifyService.getNotificationStatus();

    verify(mockNotificationHistoryRepository, never()).save(any());
    verify(mockNotificationRepository, never()).save(notification);
    assertThat(notification.getState()).isEqualTo(PENDING);
  }
}
