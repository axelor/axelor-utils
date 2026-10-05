/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2026 Axelor (<http://axelor.com>).
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axelor.utils.service;

import java.util.Map;

public interface ArchivingService {

  /**
   * Finds the models which still reference the given object through a many-to-one, one-to-one or
   * many-to-many field. One-to-many fields and non-persistable models are not checked.
   *
   * @param object the referenced object, only its class is used
   * @param id the id of the referenced object
   * @return a map of simple model name to relationship type ({@code ManyToOne}, {@code OneToOne} or
   *     {@code ManyToMany}), empty if no model references the object
   */
  Map<String, String> getObjectLinkTo(Object object, Long id);

  /**
   * Returns the title of the form view of the given model.
   *
   * @param modelName the simple name of the model
   * @return the title of the {@code <model-name>-form} view, or the model name if there is none
   */
  String getModelTitle(String modelName);
}
