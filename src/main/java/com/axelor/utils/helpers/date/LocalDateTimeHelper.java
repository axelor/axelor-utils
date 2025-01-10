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

import com.axelor.common.ObjectUtils;
import com.axelor.common.StringUtils;
import com.axelor.utils.helpers.ExceptionHelper;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Date;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class LocalDateTimeHelper {

  private LocalDateTimeHelper() {
    throw new IllegalStateException("Utility class");
  }

  public static LocalDateTime toLocalDateT(Date date) {

    Instant instant = date.toInstant();

    return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  public static LocalDateTime max(LocalDateTime d1, LocalDateTime d2) {
    if (d1 == null && d2 == null) return null;
    if (d1 == null) return d2;
    if (d2 == null) return d1;
    return (d1.isAfter(d2)) ? d1 : d2;
  }

  public static LocalDateTime min(LocalDateTime d1, LocalDateTime d2) {
    if (d1 == null && d2 == null) return null;
    if (d1 == null) return d2;
    if (d2 == null) return d1;
    return (d1.isBefore(d2)) ? d1 : d2;
  }

  public static ZonedDateTime getTodayDateTime(String timeZone) {
    return StringUtils.notBlank(timeZone)
        ? ZonedDateTime.now(ZoneId.of(timeZone))
        : ZonedDateTime.now();
  }

  /**
   * @param intervals list of LocalDateTimeInterval
   * @param instant not null
   * @return a LocalDateTimeInterval object representing the biggest continuous period containing
   *     the given LocalDate day &amp; with each day of the interval contained into at least one of
   *     the given intervals returns null if there is no given intervals.
   *     <p>Or null if the given instant is before the startDateT of the firstInterval or after the
   *     endDateT of the lastInterval.
   * @throws IllegalArgumentException if the given LocalDate is null
   */
  public static LocalDateTimeInterval getMergedIntervalContainingInstant(
      Collection<LocalDateTimeInterval> intervals, LocalDateTime instant)
      throws IllegalArgumentException {

    if (instant == null) {
      throw new IllegalArgumentException("day cannot be null");
    }

    if (ObjectUtils.isEmpty(intervals)) {
      return null;
    }

    Deque<LocalDateTimeInterval> mergedIntervals = new ArrayDeque<>();
    List<LocalDateTimeInterval> sortedIntervals =
        intervals.stream().sorted().collect(Collectors.toList());

    for (LocalDateTimeInterval interval : sortedIntervals) {
      if (mergedIntervals.isEmpty()) {
        mergedIntervals.addFirst(interval);
        continue;
      }

      LocalDateTimeInterval lastInterval = mergedIntervals.peekFirst();
      // lastInterval.getEndDate().plusDays(2) because we want to merge as following
      // [12 May : 13 May] & [14 May : 16 May] --> [12 May : 16 May]
      if (interval.isContinuousAtStartWith(lastInterval)) {
        if (interval.endsAfter(lastInterval)) {
          mergedIntervals.removeFirst();
          mergedIntervals.addFirst(
              new LocalDateTimeInterval(lastInterval.getStartDateT(), interval.getEndDateT()));
        }
      } else {
        mergedIntervals.addFirst(interval);
      }
    }

    for (LocalDateTimeInterval interval : mergedIntervals) {
      if (interval.contains(instant)) {
        return new LocalDateTimeInterval(interval.getStartDateT(), interval.getEndDateT());
      }
    }

    return null;
  }

  /**
   * @param occurrenceDayOfWeek: expected dayOfWeek of the result
   * @param occurrenceTime: expected time of the result
   * @param referenceDateT: date from which the result is searched
   * @return the last date (inclusive) before given referenceDate having same dayOfWeek &amp; same
   *     time as occurrences named parameters.
   */
  public static LocalDateTime getLastOccurrence(
      DayOfWeek occurrenceDayOfWeek, LocalTime occurrenceTime, LocalDateTime referenceDateT) {

    if (occurrenceDayOfWeek == null || occurrenceTime == null || referenceDateT == null) {
      return null;
    }

    LocalDateTime lastOccurrence = referenceDateT.toLocalDate().atTime(occurrenceTime);

    DayOfWeek referenceDayOfWeek = referenceDateT.getDayOfWeek();
    if (occurrenceDayOfWeek == referenceDayOfWeek) {
      if (occurrenceTime.isAfter(referenceDateT.toLocalTime())) {
        lastOccurrence = lastOccurrence.minusWeeks(1);
      }
      return lastOccurrence;
    }

    int occurrenceDayValue = occurrenceDayOfWeek.getValue();
    int referenceDayValue = referenceDayOfWeek.getValue();
    if (occurrenceDayValue > referenceDayValue) {
      lastOccurrence = lastOccurrence.minusWeeks(1);
    }

    return lastOccurrence.plusDays((long) occurrenceDayValue - referenceDayValue);
  }

  /**
   * @param occurrenceDayOfWeek: expected dayOfWeek of the result
   * @param occurrenceTime: expected time of the result
   * @param referenceDateT: date from which the result is searched
   * @return the next date (inclusive) from given referenceDate having same dayOfWeek &amp; same
   *     time as occurrences named parameters.
   */
  public static LocalDateTime getNextOccurrence(
      DayOfWeek occurrenceDayOfWeek, LocalTime occurrenceTime, LocalDateTime referenceDateT) {

    if (occurrenceDayOfWeek == null || occurrenceTime == null || referenceDateT == null) {
      return null;
    }

    LocalDateTime nextOccurrence = referenceDateT.toLocalDate().atTime(occurrenceTime);

    DayOfWeek referenceDayOfWeek = referenceDateT.getDayOfWeek();
    if (occurrenceDayOfWeek == referenceDayOfWeek) {
      if (occurrenceTime.isBefore(referenceDateT.toLocalTime())) {
        nextOccurrence = nextOccurrence.plusWeeks(1);
      }
      return nextOccurrence;
    }

    int occurrenceDayValue = occurrenceDayOfWeek.getValue();
    int referenceDayValue = referenceDayOfWeek.getValue();
    if (occurrenceDayValue < referenceDayValue) {
      nextOccurrence = nextOccurrence.plusWeeks(1);
    }

    return nextOccurrence.plusDays((long) occurrenceDayValue - referenceDayValue);
  }

  /**
   * In case of having the last &amp; the next occurrences at exact same time distance, the next
   * occurrence is returned (to mimic rounding in Math).
   *
   * @param occurrenceDayOfWeek: expected dayOfWeek of the result
   * @param occurrenceTime: expected time of the result
   * @param referenceDateT: date from which the result is searched
   * @return the nearest date (inclusive) from given referenceDate having same dayOfWeek &amp; same
   *     time as occurrences named parameters.
   */
  public static LocalDateTime getNearestOccurrence(
      DayOfWeek occurrenceDayOfWeek, LocalTime occurrenceTime, LocalDateTime referenceDateT) {
    if (occurrenceDayOfWeek == null || occurrenceTime == null || referenceDateT == null) {
      return null;
    }

    LocalDateTime lastOccurrence =
        getLastOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDateT);

    // 5040 min is half a week
    return ChronoUnit.MINUTES.between(lastOccurrence, referenceDateT) < 5040
        ? lastOccurrence
        : lastOccurrence.plusWeeks(1);
  }

  /**
   * @param targetMonth: given java.time.Month to search boundaries on
   * @param referenceDateTime: date from which the result is searched
   * @return a MonthBoundaries object containing the start date and the end date of the first month
   *     in the past from the @param referenceDateTime exclusive corresponding to the given @param
   *     targetMonth
   */
  public static MonthBoundaries getLastMonthOccurrenceBoundaries(
      Month targetMonth, LocalDateTime referenceDateTime) {
    if (targetMonth == null || referenceDateTime == null) {
      return null;
    }
    return LocalDateHelper.getLastMonthOccurrenceBoundaries(
        targetMonth, referenceDateTime.toLocalDate());
  }

  /**
   * @param targetMonth: given java.time.Month to search boundaries on
   * @param referenceDateTime: date from which the result is searched
   * @return a MonthBoundaries object containing the start date and the end date of the first month
   *     in the future from the @param referenceDateTime exclusive corresponding to the given @param
   *     targetMonth
   */
  public static MonthBoundaries getNextMonthOccurrenceBoundaries(
      Month targetMonth, LocalDateTime referenceDateTime) {
    if (targetMonth == null || referenceDateTime == null) {
      return null;
    }
    return LocalDateHelper.getNextMonthOccurrenceBoundaries(
        targetMonth, referenceDateTime.toLocalDate());
  }

  public static XMLGregorianCalendar convert(LocalDateTime in) {

    XMLGregorianCalendar date = null;

    try {

      date = DatatypeFactory.newInstance().newXMLGregorianCalendar(in.toString());

    } catch (DatatypeConfigurationException e) {

      ExceptionHelper.error(e);
    }

    return date;
  }
}
