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
package com.axelor.utils.helpers.date;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Object to contain both start date of a month & end date of a month and being able to return it in
 * one return statement.
 */
public class MonthBoundaries {
  public LocalDate firstDayOfMonth = null;
  public LocalDate lastDayOfMonth = null;

  @Override
  public boolean equals(Object obj) {

    if (obj == null || obj.getClass() != MonthBoundaries.class) {
      return false;
    }

    MonthBoundaries monthBoundaries = (MonthBoundaries) obj;
    return Objects.equals(this.firstDayOfMonth, monthBoundaries.firstDayOfMonth)
        && Objects.equals(this.lastDayOfMonth, monthBoundaries.lastDayOfMonth);
  }
}
