package com.axelor.utils.context.adapters;

public class DefaultAdapter implements Adapter<Object> {

  @Override
  public Object process(Object contextRecord) {
    return contextRecord;
  }
}
