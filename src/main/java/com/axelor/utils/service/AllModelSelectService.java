package com.axelor.utils.service;

import com.axelor.event.Observes;
import com.axelor.events.StartupEvent;

public interface AllModelSelectService {
  void createSelectionOfAllModels(@Observes StartupEvent event);
}
