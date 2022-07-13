package uk.gov.defra.reach.notify.entity;

import java.util.Arrays;
import java.util.List;

public enum NotificationState {
  PENDING("pending"),
  CREATED("created"),
  SENDING("sending"),
  DELIVERED("delivered"),
  PERMANENT_FAILURE("permanent-failure"),
  TEMPORARY_FAILURE("temporary-failure"),
  TECHNICAL_FAILURE("technical-failure");

  private String status;

  NotificationState(String status) {
    this.status = status;
  }

  public static NotificationState fromString(String serializedStatus) {
    if (serializedStatus == null) {
      return null;
    }
    for (NotificationState state : NotificationState.values()) {
      if (serializedStatus.equalsIgnoreCase(state.status)) {
        return state;
      }
    }
    throw new IllegalArgumentException(serializedStatus + "does not correspond to a NotificationState");
  }

  public String getStatus() {
    return this.status;
  }

  // Returns a collection of all notification states which require updates
  public static List<NotificationState> getNonFinalStates() {
    return Arrays.asList(PENDING, CREATED, SENDING);
  }

}
