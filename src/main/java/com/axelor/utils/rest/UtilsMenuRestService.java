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

import com.axelor.meta.db.MetaMenu;
import java.util.Map;
import java.util.Set;

public interface UtilsMenuRestService {

  /**
   * Gets all parent menus for the given menu names.
   *
   * @param menus: set of menu names
   * @return a set of parent MetaMenu objects
   */
  Set<MetaMenu> getAllParentMenus(Set<String> menus);

  /**
   * Gets all child menus for the given menu names.
   *
   * @param menus: set of menu names
   * @return a set of child MetaMenu objects
   */
  Set<MetaMenu> getAllChildMenus(Set<String> menus);

  /**
   * Gets related metamodels for the given menu names.
   *
   * @param menus comma-separated list of menu names
   * @return a map of menu names to their related metamodel information
   */
  Map<String, Object> getRelatedMetaModel(Set<String> menus);
}
