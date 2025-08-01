/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2025 Axelor (<http://axelor.com>).
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
package com.axelor.utils.helpers;

import com.google.common.base.Strings;

public class ComputeNameHelper {

  private ComputeNameHelper() {
    throw new IllegalStateException("Utility class");
  }

  public static String computeSimpleFullName(String firstName, String lastName, String id) {
    if (!Strings.isNullOrEmpty(lastName) && !Strings.isNullOrEmpty(firstName)) {
      return lastName + " " + firstName;
    } else if (!Strings.isNullOrEmpty(lastName)) {
      return lastName;
    } else if (!Strings.isNullOrEmpty(firstName)) {
      return firstName;
    } else {
      return id;
    }
  }

  public static String computeFullName(
      String firstName, String lastName, String sequence, String id) {
    if (!Strings.isNullOrEmpty(sequence)) {
      return sequence + " - " + computeSimpleFullName(firstName, lastName, id);
    }
    return computeSimpleFullName(firstName, lastName, id);
  }
}
