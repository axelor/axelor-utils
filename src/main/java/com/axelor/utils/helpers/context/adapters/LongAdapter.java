package com.axelor.utils.helpers.context.adapters;

import java.util.Objects;

@ContextAdapter(Long.class)
public class LongAdapter implements Adapter<Long> {

  @Override
  public Long process(Object contextRecord) {
    return switch (contextRecord) {
      case null -> null;
      case Long l -> l;
      case Integer i -> Long.valueOf(i);
      case String s -> Long.valueOf(s);
      default -> Long.valueOf(Objects.toString(contextRecord));
    };
  }
}
