package com.axelor.utils.helpers.context.adapters;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@ContextAdapter(LocalDateTime.class)
public class LocalDateTimeAdapter implements Adapter<LocalDateTime> {

  @Override
  public LocalDateTime process(Object contextRecord) {
    if (contextRecord == null) {
      return null;
    }
    if (contextRecord instanceof LocalDateTime) {
      return (LocalDateTime) contextRecord;
    }
    ZonedDateTime zonedDateTime =
        ZonedDateTime.parse(contextRecord.toString(), DateTimeFormatter.ISO_DATE_TIME);
    return LocalDateTime.ofInstant(zonedDateTime.toInstant(), ZoneId.systemDefault());
  }
}
