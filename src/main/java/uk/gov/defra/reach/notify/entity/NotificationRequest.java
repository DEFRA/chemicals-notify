package uk.gov.defra.reach.notify.entity;

import java.util.Map;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NotificationRequest {
  @NotEmpty(message = "A valid event is required")
  private String event;
  @NotEmpty(message = "An email addressed is required")
  private String emailAddress;
  private Map<String, ?> personalisation;
}
