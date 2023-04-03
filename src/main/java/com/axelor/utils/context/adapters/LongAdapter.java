package com.axelor.utils.context.adapters;

import java.util.Objects;

@ContextAdapter(Long.class)
public class LongAdapter implements Adapter<Long> {

  @Override
  public Long process(Object contextRecord) {
    if (contextRecord == null) {
      return null;
    }
    if (contextRecord instanceof Long) {
      return (Long) contextRecord;
    }
    if (contextRecord instanceof Integer) {
      return Long.valueOf((Integer) contextRecord);
    }
    if (contextRecord instanceof String) {
      return Long.valueOf((String) contextRecord);
    }
    return Long.valueOf(Objects.toString(contextRecord));
  }
}
