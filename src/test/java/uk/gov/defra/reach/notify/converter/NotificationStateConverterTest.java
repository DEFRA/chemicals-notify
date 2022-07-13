package uk.gov.defra.reach.notify.converter;

import org.junit.jupiter.api.Test;
import uk.gov.defra.reach.notify.entity.NotificationState;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NotificationStateConverterTest {
  private NotificationStateConverter converter = new NotificationStateConverter();

  @Test
  public void convertToDatabaseColumn_shouldReturnString_whenGivenEnum() {
    NotificationState state = NotificationState.PENDING;

    String dbState = converter.convertToDatabaseColumn(state);

    assertThat(dbState).isEqualTo("pending");
  }

  @Test
  public void convertToDatabaseColumn_shouldReturnNull_whenInputIsNull() {
    String state = converter.convertToDatabaseColumn(null);
    assertThat(state).isNull();
  }

  @Test
  public void convertToEntityAttribute_shouldReturnEnum_whenGivenValidString() {
    String dbState = "pending";

    NotificationState state = converter.convertToEntityAttribute(dbState);

    assertThat(state).isEqualTo(NotificationState.PENDING);
  }

  @Test
  public void convertToEntityAttribute_shouldError_whenGivenInvalidString() {
    String dbState = "invalid-state";

    assertThatThrownBy(() -> {
      converter.convertToEntityAttribute(dbState);
    }).isInstanceOf(IllegalArgumentException.class);
  }
}
