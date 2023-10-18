package com.axelor.utils.helpers.context.adapters;

public interface Adapter<T> {

  T process(Object contextRecord);

  default <U> U transform(Class<U> uClass, Object processedRecord) {
    try {
      return uClass.cast(processedRecord);
    } catch (ClassCastException e) {
      return null;
    }
  }
}
