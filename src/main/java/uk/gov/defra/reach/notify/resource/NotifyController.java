package uk.gov.defra.reach.notify.resource;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.defra.reach.notify.entity.NotificationRequest;
import uk.gov.defra.reach.notify.entity.NotificationTemplate;
import uk.gov.defra.reach.notify.service.NotifyService;


@RestController
public class NotifyController {

  private NotifyService notifyService;

  @Autowired
  public NotifyController(NotifyService notifyService) {
    this.notifyService = notifyService;
  }

  @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
  public String root() {
    return "ok";
  }

  @PostMapping(value = "/email", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public void sendEmail(@Valid @RequestBody @NotNull NotificationRequest notificationRequest) {
    NotificationTemplate template = notifyService.getEventTemplate(notificationRequest.getEvent());
    notifyService.sendEmail(template, notificationRequest.getEmailAddress(), notificationRequest.getPersonalisation());
  }
}
