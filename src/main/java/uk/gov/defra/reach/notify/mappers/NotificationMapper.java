package uk.gov.defra.reach.notify.mappers;

import uk.gov.defra.reach.notify.entity.Notification;
import uk.gov.service.notify.SendEmailResponse;

public final class NotificationMapper {

  protected NotificationMapper() throws InstantiationException {
    throw new InstantiationException("NotificationMapper cannot be instantiated");
  }

  public static Notification map(SendEmailResponse notifyResponse) {
    Notification notification = new Notification();
    notification.setNotificationId(notifyResponse.getNotificationId());
    notification.setReference(notifyResponse.getReference().orElse(null));
    notification.setTemplateId(notifyResponse.getTemplateId());
    notification.setTemplateVersion(notifyResponse.getTemplateVersion());
    notification.setTemplateUri(notifyResponse.getTemplateUri());
    notification.setBody(notifyResponse.getBody());
    notification.setSubject(notifyResponse.getSubject());
    notification.setFromEmail(notifyResponse.getFromEmail().orElse(null));
    notification.setState(null);
    return notification;
  }
}
