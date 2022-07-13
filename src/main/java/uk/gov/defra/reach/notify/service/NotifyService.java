package uk.gov.defra.reach.notify.service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.defra.reach.notify.entity.Notification;
import uk.gov.defra.reach.notify.entity.NotificationHistory;
import uk.gov.defra.reach.notify.entity.NotificationState;
import uk.gov.defra.reach.notify.entity.NotificationTemplate;
import uk.gov.defra.reach.notify.mappers.NotificationMapper;
import uk.gov.defra.reach.notify.repository.NotificationHistoryRepository;
import uk.gov.defra.reach.notify.repository.NotificationRepository;
import uk.gov.defra.reach.notify.repository.NotificationTemplateRepository;
import uk.gov.service.notify.NotificationClient;
import uk.gov.service.notify.NotificationClientException;
import uk.gov.service.notify.SendEmailResponse;


@Service
@Slf4j
@Transactional
public class NotifyService {

  private final NotificationClient notificationClient;
  private final NotificationTemplateRepository notificationTemplateRepository;
  private final NotificationRepository notificationResponseRepository;
  private final NotificationHistoryRepository notificationHistoryRepository;

  @Autowired
  public NotifyService(
      NotificationClient notificationClient,
      NotificationTemplateRepository notificationTemplateRepository,
      NotificationRepository notificationRepository,
      NotificationHistoryRepository notificationHistoryRepository) {
    this.notificationClient = notificationClient;
    this.notificationTemplateRepository = notificationTemplateRepository;
    this.notificationResponseRepository = notificationRepository;
    this.notificationHistoryRepository = notificationHistoryRepository;
  }

  @Async
  public void sendEmail(NotificationTemplate template, String emailAddress, Map<String, ?> personalisation) {
    try {
      SendEmailResponse sendEmailResponse = notificationClient.sendEmail(template.getTemplateId(), emailAddress, personalisation, null);
      Notification notificationResponse = NotificationMapper.map(sendEmailResponse);
      notificationResponse.setState(NotificationState.PENDING);
      notificationResponseRepository.save(notificationResponse);
      NotificationHistory historyEntry = NotificationHistory.initializeNotificationHistory(notificationResponse);
      notificationHistoryRepository.save(historyEntry);
    } catch (NotificationClientException nce) {
      log.error("Failed to send notification request: {}", nce.getLocalizedMessage());
    }
  }

  @Cacheable("eventTemplates")
  @Transactional(readOnly = true)
  public NotificationTemplate getEventTemplate(String event) {
    return notificationTemplateRepository.findById(event).orElseThrow(MissingTemplateException::new);
  }

  @Scheduled(cron = "${reach.notify.status.scheduling.cron}")
  public void getNotificationStatus() {
    log.info("Updating pending emails");

    List<Notification> pendingNotifications = notificationResponseRepository.findByStateIn(NotificationState.getNonFinalStates());
    log.info(pendingNotifications.size() + " pending emails");
    for (Notification notification: pendingNotifications) {
      log.info("Updating status of:" + notification.getNotificationId().toString());
      try {
        uk.gov.service.notify.Notification notificationResponse = notificationClient.getNotificationById(notification.getNotificationId().toString());
        updateNotificationStatus(notification, NotificationState.fromString(notificationResponse.getStatus()));
      } catch (NotificationClientException e) {
        log.error("Notify failed to fetch email with id: " + notification.getNotificationId().toString() + " " + e.getLocalizedMessage());
      }
    }
  }

  private void updateNotificationStatus(Notification notification, NotificationState newState) {
    NotificationHistory historyEntry = NotificationHistory.builder()
            .notificationId(notification.getNotificationId())
            .fromState(notification.getState())
            .toState(newState)
            .build();
    log.info("Notification state updated: " + historyEntry.toString());
    notification.setUpdatedAt(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
    notificationHistoryRepository.save(historyEntry);
    notification.setState(newState);
    notificationResponseRepository.save(notification);
  }

}
