package uk.gov.defra.reach.notify.entity;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NotificationStateTest {

  @Test
  public void fromString_shouldReturnCorrectly_whenValid() {
    Map<String, NotificationState> statePairs = new HashMap<>();
    statePairs.put("pending", NotificationState.PENDING);
    statePairs.put("created", NotificationState.CREATED);
    statePairs.put("sending", NotificationState.SENDING);
    statePairs.put("delivered", NotificationState.DELIVERED);
    statePairs.put("permanent-failure", NotificationState.PERMANENT_FAILURE);
    statePairs.put("temporary-failure", NotificationState.TEMPORARY_FAILURE);
    statePairs.put("technical-failure", NotificationState.TECHNICAL_FAILURE);

    for(Map.Entry<String, NotificationState> pair: statePairs.entrySet()) {
      NotificationState state = null;
      state = NotificationState.fromString(pair.getKey());
      assertThat(state).isEqualTo(pair.getValue());
      assertThat(state).isNotNull();
    }
  }

  @Test
  public void fromString_shouldReturnNull_whenGivenNullInput() {
    NotificationState state = NotificationState.fromString(null);
    assertThat(state).isNull();
  }

  @Test
  public void fromString_showThrowIllegalArgumentException_whenNotFound() {
    assertThatThrownBy(() -> {
      NotificationState.fromString("Non Existing State");
    }).isInstanceOf(IllegalArgumentException.class);
  }
}
