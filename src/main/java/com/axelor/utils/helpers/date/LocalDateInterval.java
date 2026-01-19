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
package com.axelor.utils.helpers.date;

import java.time.LocalDate;
import java.util.Objects;

/**
 * LocalDateInterval is a class used to define intervals of time with precision of a day.
 *
 * <p>LocalDateInterval are closed intervals when bounded -> startDate & endDate are included in the
 * interval -> A one day interval is represented by startDate = endDate
 *
 * <p>startDate = null means minus-infinite endDate = null means plus-infinite -> new
 * LocalDateInterval(null, null) represent all the past & future
 *
 * <p>{@link Comparable} on startDate
 *
 * @author Maxence GALLANCHER
 */
public class LocalDateInterval implements Comparable<LocalDateInterval> {

  private LocalDate startDate;
  private LocalDate endDate;

  public LocalDateInterval(LocalDate startDate, LocalDate endDate) {
    if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
      throw new IllegalStateException("endDate cannot be before startDate");
    }
    this.setStartDate(startDate);
    this.setEndDate(endDate);
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  //////////////////////////////////////////////////////////////

  @Override
  public boolean equals(Object obj) {

    if (obj == null || obj.getClass() != LocalDateInterval.class) {
      return false;
    }

    LocalDateInterval interval = (LocalDateInterval) obj;
    return Objects.equals(this.getStartDate(), interval.getStartDate())
        && Objects.equals(this.getEndDate(), interval.getEndDate());
  }

  @Override
  public int hashCode() {
    return Objects.hash(startDate, endDate);
  }

  @Override
  public int compareTo(LocalDateInterval interval) {

    LocalDate comparedStartDate = interval.getStartDate();
    boolean thisStartsNull = this.startDate == null;

    if (comparedStartDate == null) {
      if (thisStartsNull) {
        return 0;
      }
      return 2;
    }
    if (thisStartsNull) {
      return -2;
    }

    if (this.startDate.equals(comparedStartDate)) {
      return 0;
    }
    if (this.startDate.isBefore(comparedStartDate)) {
      return -1;
    }
    return 1;
  }

  //////////////////////////////////////////////////////////////

  public boolean contains(LocalDate day) {
    return this.startsBeforeOrAt(day) && endsAfterOrAt(day);
  }

  public boolean startsBeforeOrAt(LocalDate day) {
    return this.startsBefore(day) || day.equals(this.startDate);
  }

  public boolean startsBefore(LocalDate day) {
    return this.startDate == null || this.startDate.isBefore(day);
  }

  public boolean endsAfterOrAt(LocalDate day) {
    return this.endsAfter(day) || day.equals(this.endDate);
  }

  public boolean endsAfter(LocalDate day) {
    return this.endDate == null || this.endDate.isAfter(day);
  }

  //////////////////////////////////////////////////////////////

  public boolean startsBefore(LocalDateInterval comparedInterval) {
    LocalDate comparedStartDate = comparedInterval.getStartDate();
    if (comparedStartDate == null) {
      return false;
    } else if (this.startDate == null) {
      return true;
    }

    return this.startDate.isBefore(comparedStartDate);
  }

  public boolean startsAfter(LocalDateInterval comparedInterval) {
    LocalDate comparedStartDate = comparedInterval.getStartDate();
    if (this.startDate == null) {
      return false;
    } else if (comparedStartDate == null) {
      return true;
    }

    return this.startDate.isAfter(comparedStartDate);
  }

  public boolean endsBefore(LocalDateInterval comparedInterval) {
    LocalDate comparedEndDate = comparedInterval.getEndDate();
    if (this.endDate == null) {
      return false;
    } else if (comparedEndDate == null) {
      return true;
    }

    return this.endDate.isBefore(comparedEndDate);
  }

  public boolean endsAfter(LocalDateInterval comparedInterval) {
    LocalDate comparedEndDate = comparedInterval.getEndDate();
    if (comparedEndDate == null) {
      return false;
    } else if (this.endDate == null) {
      return true;
    }

    return this.endDate.isAfter(comparedEndDate);
  }

  //////////////////////////////////////////////////////////////

  public boolean overlapsOrIsContinuousWith(LocalDateInterval comparedInterval) {
    int order = this.compareTo(comparedInterval);
    if (order == 0) {
      return true;
    }
    if (order < 0) {
      return this.isContinuousAtEndWith(comparedInterval);
    }
    return this.isContinuousAtStartWith(comparedInterval);
  }

  /**
   * @param comparedInterval
   * @return true if and only if the {@code comparedInterval.startDate} is before, the same or the
   *     day right after {@code this.endDate}.
   */
  public boolean isContinuousAtEndWith(LocalDateInterval comparedInterval) {
    LocalDate comparedStartDate = comparedInterval.getStartDate();
    if (comparedStartDate == null || this.endDate == null) {
      return true;
    }

    // comparedStartDate.minusDays(2) because following intervals are continuous
    // (this) [12 May : 13 May] & (compared) [14 May : 16 May]
    // --> (true) [12 May : 16 May]
    return this.endDate.isAfter(comparedStartDate.minusDays(2));
  }

  /**
   * @param comparedInterval
   * @return true if and only if the {@code comparedInterval.endDate} is after, the same or the day
   *     right before {@code this.startDate}.
   */
  public boolean isContinuousAtStartWith(LocalDateInterval comparedInterval) {
    LocalDate comparedEndDate = comparedInterval.getEndDate();
    if (comparedEndDate == null || this.startDate == null) {
      return true;
    }

    // comparedStartDate.plusDays(2) because following intervals are continuous
    // [14 May : 16 May] & [12 May : 13 May] --> [12 May : 16 May]
    return this.startDate.isBefore(comparedEndDate.plusDays(2));
  }
}
