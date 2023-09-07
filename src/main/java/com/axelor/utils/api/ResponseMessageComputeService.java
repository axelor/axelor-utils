package com.axelor.utils.api;

import com.axelor.db.Model;

public interface ResponseMessageComputeService {

  /**
   * Compute the response message for creation request for the given model.
   *
   * @param model The created model.
   * @return The computed response.
   */
  String computeCreateMessage(Model model);
}
