package com.axelor.utils.service;

import com.axelor.db.Model;
import com.axelor.utils.helpers.context.FullContext;

public interface ActionService {

  <T extends Model> T applyActions(String actions, T bean);

  FullContext applyActions(String actions, FullContext context);
}
