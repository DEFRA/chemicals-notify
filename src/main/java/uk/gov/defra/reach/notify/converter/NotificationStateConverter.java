package uk.gov.defra.reach.notify.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import uk.gov.defra.reach.notify.entity.NotificationState;

@Converter
public class NotificationStateConverter implements AttributeConverter<NotificationState, String> {

  @Override
  public String convertToDatabaseColumn(NotificationState attribute) {
    if (attribute == null) {
      return null;
    }
    return attribute.getStatus();
  }

  @Override
  public NotificationState convertToEntityAttribute(String dbData) {
    return NotificationState.fromString(dbData);
  }
}
