package com.axelor.utils.helpers.context.adapters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@ContextAdapter(LocalDate.class)
public class LocalDateAdapter implements Adapter<LocalDate> {

  @Override
  public LocalDate process(Object contextRecord) {
    if (contextRecord == null) {
      return null;
    }
    if (contextRecord instanceof LocalDate) {
      return (LocalDate) contextRecord;
    }
    return LocalDate.parse(contextRecord.toString(), DateTimeFormatter.ISO_DATE);
  }
}
