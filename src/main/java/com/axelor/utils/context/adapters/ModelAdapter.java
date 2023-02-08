package com.axelor.utils.context.adapters;

import com.axelor.db.JpaRepository;
import com.axelor.db.Model;
import java.util.Map;

@ContextAdapter(Model.class)
public class ModelAdapter implements Adapter<Long> {

  @Override
  @SuppressWarnings("unchecked")
  public Long process(Object contextRecord) {
    if (contextRecord == null) {
      return null;
    }
    if (contextRecord instanceof Model && ((Model) contextRecord).getId() != null) {
      return ((Model) contextRecord).getId();
    }
    if (((Map<String, Object>) contextRecord).get("id") == null) {
      return null;
    }
    return Long.parseLong(((Map<String, Object>) contextRecord).get("id").toString());
  }

  @Override
  @SuppressWarnings("unchecked")
  public <U> U transform(Class<U> uClass, Object processedRecord) {
    if (!(processedRecord instanceof Long)) {
      return null;
    }
    if (Model.class.isAssignableFrom(uClass)) {
      return (U) JpaRepository.of(uClass.asSubclass(Model.class)).find((Long) processedRecord);
    }
    return null;
  }
}
