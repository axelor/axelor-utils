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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * LocalDateTimeInterval is a class used to define intervals of time with variable precision.
 *
 * <p>For precision of a day better {@link LocalDateInterval}
 *
 * <p>LocalDateTimeInterval are closed intervals when bounded -&gt; startDateT &amp; endDateT are
 * included in the interval
 *
 * <p>startDateT = null means minus-infinite endDateT = null means plus-infinite
 *
 * <p>-&gt; new LocalDateTimeInterval(null, null) represent all the past &amp; future
 *
 * <p>{@link Comparable} on startDateT
 *
 * @author Maxence GALLANCHER
 */
@Setter
@Getter
public class LocalDateTimeInterval implements Comparable<LocalDateTimeInterval> {

  private LocalDateTime startDateT;
  private LocalDateTime endDateT;

  /**
   * To consider if 2 LocalDateTimeInterval are continuous a small gap between the end of an
   * interval and the start of the next one can be ignored. This is by default 1 minute because the
   * widget used to select a dateTime has 1 minute precision.
   */
  private ChronoUnit continuousToleranceUnit;

  private Integer continuousToleranceAmount;

  /**
   * By default, the continuousToleranceUnit is set to MINUTES and the continuousToleranceAmount is
   * set to 1
   *
   * @param startDateT the start date of the interval
   * @param endDateT the end date of the interval
   */
  public LocalDateTimeInterval(LocalDateTime startDateT, LocalDateTime endDateT) {
    this(startDateT, endDateT, ChronoUnit.MINUTES, 1);
  }

  public LocalDateTimeInterval(
      LocalDateTime startDateT, LocalDateTime endDateT, ChronoUnit continuousToleranceUnit) {
    this(startDateT, endDateT, continuousToleranceUnit, 1);
  }

  public LocalDateTimeInterval(
      LocalDateTime startDateT,
      LocalDateTime endDateT,
      ChronoUnit continuousToleranceUnit,
      Integer continuousToleranceAmount) {
    if (startDateT != null && endDateT != null && startDateT.isAfter(endDateT)) {
      throw new IllegalStateException("endDateT cannot be before startDateT");
    }
    if (continuousToleranceAmount < 0) {
      throw new IllegalStateException("Tolerance amount cannot be negative");
    }
    this.startDateT = startDateT;
    this.endDateT = endDateT;
    this.continuousToleranceUnit = continuousToleranceUnit;
    this.continuousToleranceAmount = continuousToleranceAmount;
  }

  //////////////////////////////////////////////////////////////

  @Override
  public boolean equals(Object obj) {

    if (obj == null || obj.getClass() != LocalDateTimeInterval.class) {
      return false;
    }

    LocalDateTimeInterval interval = (LocalDateTimeInterval) obj;
    return Objects.equals(this.startDateT, interval.getStartDateT())
        && Objects.equals(this.endDateT, interval.getEndDateT());
  }

  public boolean deepEquals(Object obj) {

    if (obj == null || obj.getClass() != LocalDateTimeInterval.class) {
      return false;
    }

    LocalDateTimeInterval interval = (LocalDateTimeInterval) obj;
    return Objects.equals(this.startDateT, interval.getStartDateT())
        && Objects.equals(this.endDateT, interval.getEndDateT())
        && Objects.equals(this.continuousToleranceUnit, interval.getContinuousToleranceUnit())
        && Objects.equals(this.continuousToleranceAmount, interval.getContinuousToleranceAmount());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.startDateT, this.endDateT);
  }

  @Override
  public int compareTo(LocalDateTimeInterval interval) {

    LocalDateTime comparedStartDate = interval.getStartDateT();
    boolean thisStartsNull = this.startDateT == null;

    if (comparedStartDate == null) {
      if (thisStartsNull) {
        return 0;
      }
      return 2;
    }
    if (thisStartsNull) {
      return -2;
    }

    if (this.startDateT.equals(comparedStartDate)) {
      return 0;
    }
    if (this.startDateT.isBefore(comparedStartDate)) {
      return -1;
    }
    return 1;
  }

  //////////////////////////////////////////////////////////////

  public boolean contains(LocalDateTime day) {
    return this.startsBeforeOrAt(day) && endsAfterOrAt(day);
  }

  public boolean startsBeforeOrAt(LocalDateTime day) {
    return this.startsBefore(day) || day.equals(this.startDateT);
  }

  public boolean startsBefore(LocalDateTime day) {
    return this.startDateT == null || this.startDateT.isBefore(day);
  }

  public boolean endsAfterOrAt(LocalDateTime day) {
    return this.endsAfter(day) || day.equals(this.endDateT);
  }

  public boolean endsAfter(LocalDateTime day) {
    return this.endDateT == null || this.endDateT.isAfter(day);
  }

  //////////////////////////////////////////////////////////////

  public boolean startsBefore(LocalDateTimeInterval comparedInterval) {
    LocalDateTime comparedStartDate = comparedInterval.getStartDateT();
    if (comparedStartDate == null) {
      return false;
    } else if (this.startDateT == null) {
      return true;
    }

    return this.startDateT.isBefore(comparedStartDate);
  }

  public boolean startsAfter(LocalDateTimeInterval comparedInterval) {
    LocalDateTime comparedStartDate = comparedInterval.getStartDateT();
    if (this.startDateT == null) {
      return false;
    } else if (comparedStartDate == null) {
      return true;
    }

    return this.startDateT.isAfter(comparedStartDate);
  }

  public boolean endsBefore(LocalDateTimeInterval comparedInterval) {
    LocalDateTime comparedEndDate = comparedInterval.getEndDateT();
    if (this.endDateT == null) {
      return false;
    } else if (comparedEndDate == null) {
      return true;
    }

    return this.endDateT.isBefore(comparedEndDate);
  }

  public boolean endsAfter(LocalDateTimeInterval comparedInterval) {
    LocalDateTime comparedEndDate = comparedInterval.getEndDateT();
    if (comparedEndDate == null) {
      return false;
    } else if (this.endDateT == null) {
      return true;
    }

    return this.endDateT.isAfter(comparedEndDate);
  }

  //////////////////////////////////////////////////////////////

  /**
   * @param comparedInterval the interval to compare with
   * @return true if and only if - the two interval overlaps, - or if {@code
   *     comparedInterval.startDateT} is before, or at the same moment as the {@code this.endDateT}
   *     + tolerance from this interval
   */
  public boolean overlapsOrIsContinuousWith(LocalDateTimeInterval comparedInterval) {
    int order = this.compareTo(comparedInterval);
    if (order == 0) {
      return true;
    }
    if (order < 0) {
      return this.isContinuousAtEndWith(comparedInterval)
          || comparedInterval.isContinuousAtStartWith(this);
    }
    return this.isContinuousAtStartWith(comparedInterval)
        || comparedInterval.isContinuousAtEndWith(this);
  }

  /**
   * @param comparedInterval the interval to compare with
   * @return true if and only if the {@code comparedInterval.startDateT} is before, or at the same
   *     moment as the {@code this.endDateT} + tolerance from this interval
   *     <p>- Warning, this method will return true if {@code this} ends before {@code
   *     comparedInterval} starts, so even if the 2 intervals does not overlap
   */
  public boolean isContinuousAtEndWith(LocalDateTimeInterval comparedInterval) {
    LocalDateTime comparedStartDate = comparedInterval.getStartDateT();
    if (comparedStartDate == null || this.endDateT == null) {
      return true;
    }

    if (continuousToleranceUnit == ChronoUnit.FOREVER) {
      if (continuousToleranceAmount != 0) {
        return true;
      }
      continuousToleranceUnit =
          ChronoUnit.HOURS; // Arbitrary value to avoid exception on .plus(0 times forever)
    }

    LocalDateTime minimalStartDateForContinuousIntervalAtEnd =
        this.endDateT.plus(continuousToleranceAmount, continuousToleranceUnit);
    return comparedStartDate.equals(minimalStartDateForContinuousIntervalAtEnd)
        || comparedStartDate.isBefore(minimalStartDateForContinuousIntervalAtEnd);
  }

  /**
   * @param comparedInterval the interval to compare with
   * @return true if and only if the {@code comparedInterval.startDateT} is before, or at the same
   *     moment as the {@code this.endDateT} + tolerance from the compared interval
   *     <p>- Warning, this method will return true if {@code comparedInterval} ends before {@code
   *     this} starts, so even if the 2 intervals does not overlap
   */
  public boolean isContinuousAtStartWith(LocalDateTimeInterval comparedInterval) {
    return comparedInterval.isContinuousAtEndWith(this);
  }
}
