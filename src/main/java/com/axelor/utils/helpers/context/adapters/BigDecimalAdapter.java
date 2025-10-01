package com.axelor.utils.helpers.context.adapters;

import java.math.BigDecimal;
import java.util.Objects;

@ContextAdapter(BigDecimal.class)
public class BigDecimalAdapter implements Adapter<BigDecimal> {

  @Override
  public BigDecimal process(Object contextRecord) {
    return switch (contextRecord) {
      case null -> null;
      case BigDecimal bigDecimal -> bigDecimal;
      case Long l -> BigDecimal.valueOf(l);
      case Double v -> BigDecimal.valueOf(v);
      default -> new BigDecimal(Objects.toString(contextRecord));
    };
  }
}
