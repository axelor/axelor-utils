/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2022 Axelor (<http://axelor.com>).
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
package com.axelor.utils.rest;

import com.axelor.meta.db.MetaModel;
import java.util.List;

/**
 * Service interface for utility operations related to MetaModels. Provides methods for retrieving
 * models and their references.
 */
public interface UtilsRestService {

  /**
   * Adds references of the specified types to the provided list. This method traverses
   * the model's properties and adds any directly referenced models that match the specified
   * relationship types (first level only).
   *
   * @param model the MetaModel to find references for
   * @param listOfRef the list to add references to
   * @param types the relationship types to include (e.g., "ONE_TO_ONE", "ONE_TO_MANY")
   */
  void addReferences(MetaModel model, List<MetaModel> listOfRef, String... types);

  /**
   * Gets a MetaModel by its full class name. Extracts the simple class name from the full name and
   * looks up the corresponding MetaModel.
   *
   * @param fullName the full class name (e.g., "com.axelor.meta.db.MetaModel")
   * @return the corresponding MetaModel, or null if not found
   */
  MetaModel getModel(String fullName);
}
