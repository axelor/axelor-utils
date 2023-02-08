package com.axelor.utils.context.adapters;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@ContextAdapter(BigDecimal.class)
public class BigDecimalAdapter implements Adapter<BigDecimal> {

  @Override
  public BigDecimal process(Object contextRecord) {
    if (contextRecord == null) {
      return null;
    }
    if (contextRecord instanceof BigDecimal) {
      return (BigDecimal) contextRecord;
    }
    if (contextRecord instanceof Long) {
      return BigDecimal.valueOf((Long) contextRecord);
    }
    if (contextRecord instanceof Double) {
      return BigDecimal.valueOf((Double) contextRecord);
    }
    return new BigDecimal(Objects.toString(contextRecord));
  }
}
