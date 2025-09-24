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
package com.axelor.utils.helpers.date;

import static java.time.temporal.ChronoUnit.DAYS;

import com.axelor.common.ObjectUtils;
import java.lang.invoke.MethodHandles;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Date;
import java.util.Deque;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalDateHelper {

  private LocalDateHelper() {
    throw new IllegalStateException("Utility class");
  }

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public static long daysBetween(LocalDate date1, LocalDate date2, boolean days360) {

    long days;

    if (days360) {
      days = date1.isBefore(date2) ? days360Between(date1, date2) : -days360Between(date2, date1);
    } else {
      days = daysBetween(date1, date2);
    }

    LOG.debug(
        "Number of days between {} - {} (month of 30 days ? {}) : {}", date1, date2, days360, days);
    return days;
  }

  private static long daysBetween(LocalDate date1, LocalDate date2) {

    if (date2.isBefore(date1)) {
      return DAYS.between(date1, date2) - 1;
    } else {
      return DAYS.between(date1, date2) + 1;
    }
  }

  private static int days360Between(LocalDate startDate, LocalDate endDate) {

    int nbDayOfFirstMonth = 0;
    int nbDayOfOthersMonths = 0;
    int nbDayOfLastMonth;

    LocalDate start = startDate;

    if (endDate.getMonthValue() != startDate.getMonthValue()
        || endDate.getYear() != startDate.getYear()) {

      // First month :: if the startDate is not the last day of the month
      if (!startDate.isEqual(startDate.withDayOfMonth(startDate.lengthOfMonth()))) {
        nbDayOfFirstMonth = 30 - startDate.getDayOfMonth();
      }

      // The startDate is included
      nbDayOfFirstMonth = nbDayOfFirstMonth + 1;

      // Months between the first one and the last one
      LocalDate date1 = startDate.plusMonths(1).withDayOfMonth(1);
      while (endDate.getMonthValue() != date1.getMonthValue()
          || endDate.getYear() != date1.getYear()) {

        nbDayOfOthersMonths = nbDayOfOthersMonths + 30;
        date1 = date1.plusMonths(1);
      }

      // Last Month
      start = endDate.withDayOfMonth(1);
    }

    if (endDate.isEqual(endDate.withDayOfMonth(endDate.lengthOfMonth()))) {
      nbDayOfLastMonth = 30 - start.getDayOfMonth();
    } else {
      nbDayOfLastMonth = endDate.getDayOfMonth() - start.getDayOfMonth();
    }

    // The endDate is included
    nbDayOfLastMonth = nbDayOfLastMonth + 1;

    return nbDayOfFirstMonth + nbDayOfOthersMonths + nbDayOfLastMonth;
  }

  public static int days360MonthsBetween(LocalDate startDate, LocalDate endDate) {

    if (startDate.isBefore(endDate)) {
      return days360Between(startDate, endDate) / 30;
    } else {
      return -days360Between(endDate, startDate) / 30;
    }
  }

  public static boolean isProrata(
      LocalDate dateFrame1, LocalDate dateFrame2, LocalDate date1, LocalDate date2) {

    if (date2 == null && (date1.isBefore(dateFrame2) || date1.isEqual(dateFrame2))) {
      return true;
    } else if (date2 == null) {
      return false;
    }

    return ((date1.isAfter(dateFrame1) || date1.isEqual(dateFrame1))
            && (date1.isBefore(dateFrame2) || date1.isEqual(dateFrame2)))
        || ((date2.isAfter(dateFrame1) || date2.isEqual(dateFrame1))
            && (date2.isBefore(dateFrame2) || date2.isEqual(dateFrame2)))
        || (date1.isBefore(dateFrame1) && date2.isAfter(dateFrame2));
  }

  public static boolean isBetween(LocalDate dateFrame1, LocalDate dateFrame2, LocalDate date) {

    return (dateFrame2 == null && (date.isAfter(dateFrame1) || date.isEqual(dateFrame1)))
        || (dateFrame2 != null
            && (date.isAfter(dateFrame1) || date.isEqual(dateFrame1))
            && (date.isBefore(dateFrame2) || date.isEqual(dateFrame2)));
  }

  /**
   * Computes the date of the next occurrence of an event according to the following calculation:
   * deletes as many times as possible the frequency in month to the targeted date while being
   * greater than the start date.
   *
   * @param startDate The start date
   * @param goalDate The targeted date
   * @param frequencyInMonth Number of months depicting the frequency of the event
   * @return The date of the next occurrence of the event
   */
  public static LocalDate nextOccurrence(
      LocalDate startDate, LocalDate goalDate, int frequencyInMonth) {

    if (isInputsInvalid(startDate, goalDate, frequencyInMonth)) {
      return null;

    } else if (startDate.isAfter(goalDate)) {
      return goalDate;
    }

    return minusMonths(
        goalDate,
        days360MonthsBetween(startDate.plusDays(1), goalDate.minusDays(1))
            / frequencyInMonth
            * frequencyInMonth);
  }

  /**
   * Computes the date of the next occurrence of an event according to the following calculation:
   * deletes as many times as possible the frequency in month to the targeted date while being
   * greater than or equal to the start date.
   *
   * @param startDate The start date
   * @param goalDate The targeted date
   * @param frequencyInMonth Number of months depicting the frequency of the event
   * @return The date of the next occurrence of the event
   */
  public LocalDate nextOccurrenceStartDateIncluded(
      LocalDate startDate, LocalDate goalDate, int frequencyInMonth) {

    if (isInputsInvalid(startDate, goalDate, frequencyInMonth)) {
      return null;

    } else if (startDate.isAfter(goalDate)) {
      return goalDate;
    }

    return minusMonths(
        goalDate,
        days360MonthsBetween(startDate, goalDate.minusDays(1))
            / frequencyInMonth
            * frequencyInMonth);
  }

  /**
   * Computes the date of the last occurrence of an event according to the following calculation:
   * adds as many times as possible the frequency in month to the start date while being less than
   * or equal to the end date.
   *
   * @param startDate The start date
   * @param endDate The end date
   * @param frequencyInMonth Number of months depicting the frequency of the event
   * @return The date of the last occurrence of the event
   */
  public static LocalDate lastOccurrence(
      LocalDate startDate, LocalDate endDate, int frequencyInMonth) {

    if (isInputsInvalid(startDate, endDate, frequencyInMonth)) {
      return null;

    } else if (startDate.isAfter(endDate)) {
      return null;

    } else {
      return plusMonths(
          startDate,
          days360MonthsBetween(startDate, endDate) / frequencyInMonth * frequencyInMonth);
    }
  }

  /**
   * Checks that the frequency is not equal to zero and that the start and end dates are not null.
   *
   * @param startDate The start date
   * @param endDate The end date
   * @param frequencyInMonth The frequency in month
   * @return True if the inputs are invalid, false otherwise
   */
  private static boolean isInputsInvalid(
      LocalDate startDate, LocalDate endDate, int frequencyInMonth) {
    if (frequencyInMonth == 0) {
      LOG.debug("The frequency should not be zero.");
      return true;
    } else if (startDate == null) {
      LOG.debug("The start date should not be null.");
      return true;
    } else if (endDate == null) {
      LOG.debug("The end date should not be null.");
      return true;
    }
    return false;
  }

  public static LocalDate minusMonths(LocalDate date, int nbMonths) {

    return date.plusDays(1).minusMonths(nbMonths).minusDays(1);
  }

  public static LocalDate plusMonths(LocalDate date, int nbMonths) {

    return date.plusDays(1).plusMonths(nbMonths).minusDays(1);
  }

  public static LocalDateTime plusSeconds(LocalDateTime datetime, long duration) {

    return datetime.plusSeconds(duration);
  }

  public static LocalDateTime minusSeconds(LocalDateTime datetime, long duration) {

    return datetime.minusSeconds(duration);
  }

  /**
   * Checks if a date is in a specific period.
   *
   * @param date The date to check
   * @param dayBegin The start day of the period
   * @param monthBegin The start month of the period
   * @param dayEnd The end day of the period
   * @param monthEnd The start month of the period
   * @return True if the date is in the period, false otherwise
   */
  public static boolean dateInPeriod(
      LocalDate date, int dayBegin, int monthBegin, int dayEnd, int monthEnd) {

    if (monthBegin > monthEnd) {
      return (date.getMonthValue() == monthBegin && date.getDayOfMonth() >= dayBegin)
          || (date.getMonthValue() > monthBegin)
          || (date.getMonthValue() < monthEnd)
          || (date.getMonthValue() == monthEnd && date.getDayOfMonth() <= dayEnd);

    } else if (monthBegin == monthEnd) {
      return (date.getMonthValue() == monthBegin
          && date.getDayOfMonth() >= dayBegin
          && date.getDayOfMonth() <= dayEnd);

    } else {
      return (date.getMonthValue() == monthBegin && date.getDayOfMonth() >= dayBegin)
          || (date.getMonthValue() > monthBegin && date.getMonthValue() < monthEnd)
          || (date.getMonthValue() == monthEnd && date.getDayOfMonth() <= dayEnd);
    }
  }

  public static LocalDate toLocalDate(Date date) {

    Instant instant = date.toInstant();

    return instant.atZone(ZoneId.systemDefault()).toLocalDate();
  }

  public static Date toDate(LocalDate date) {
    return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }

  public static LocalDate getTodayDate(String timeZone) {
    return LocalDateTimeHelper.getTodayDateTime(timeZone).toLocalDate();
  }

  /**
   * True if the dates are in the same month of the same year.
   *
   * @param date1 First date to check
   * @param date2 Second date to check
   * @return True if the dates are in the same month of the same year
   */
  public static boolean isInTheSameMonth(LocalDate date1, LocalDate date2) {
    return date1.getMonth().equals(date2.getMonth()) && date1.getYear() == date2.getYear();
  }

  /**
   * @param intervals list of LocalDateInterval
   * @param day not null
   * @return a LocalDateInterval object representing the biggest continuous period containing the
   *     given LocalDate day &amp; with each day of the interval contained into at least one of the
   *     given intervals returns null if there is no given intervals.
   *     <p>Or null if the given date is before the startDate of the firstInterval or after the
   *     endDate of the lastInterval.
   * @throws IllegalArgumentException if the given LocalDate is null
   */
  public static LocalDateInterval getMergedIntervalContainingDay(
      Collection<LocalDateInterval> intervals, LocalDate day) throws IllegalArgumentException {

    if (day == null) {
      throw new IllegalArgumentException("day cannot be null");
    }

    if (ObjectUtils.isEmpty(intervals)) {
      return null;
    }

    Deque<LocalDateInterval> mergedIntervals = new ArrayDeque<>();
    List<LocalDateInterval> sortedIntervals = intervals.stream().sorted().toList();

    for (LocalDateInterval interval : sortedIntervals) {
      if (mergedIntervals.isEmpty()) {
        mergedIntervals.addFirst(interval);
        continue;
      }

      LocalDateInterval lastInterval = mergedIntervals.peekFirst();
      // lastInterval.getEndDate().plusDays(2) because we want to merge as following
      // [12 May : 13 May] & [14 May : 16 May] --> [12 May : 16 May]
      if (interval.isContinuousAtStartWith(lastInterval)) {
        if (interval.endsAfter(lastInterval)) {
          mergedIntervals.removeFirst();
          mergedIntervals.addFirst(
              new LocalDateInterval(lastInterval.getStartDate(), interval.getEndDate()));
        }
      } else {
        mergedIntervals.addFirst(interval);
      }
    }

    for (LocalDateInterval interval : mergedIntervals) {
      if (interval.contains(day)) {
        return new LocalDateInterval(interval.getStartDate(), interval.getEndDate());
      }
    }

    return null;
  }

  /**
   * Useful for display in charts for example where the year charge too much the view. E.g. 03/12
   * for the 3rd of december
   *
   * @param day the date to format
   * @return the date without the year in a String
   */
  public static String getFrenchFormatWithoutYear(LocalDate day) {
    // XXX: limited to French way of displaying date with day before month
    // but more efficient than displaying differently regarding the locale
    return day != null
        ? String.format("%02d", day.getDayOfMonth())
            + "/"
            + String.format("%02d", day.getMonthValue())
        : null;
  }

  /**
   * Compute the sum of Saturdays &amp; Sundays in the closed interval defined by the given dates.
   *
   * <p>Example:
   *
   * <ul>
   *   <li><b>startDate:</b> <i>Sunday 2022-12-18</i>
   *   <li><b>endDate:</b> <i>Sunday 2022-12-25</i>
   *   <li><b>result:</b> <i>3</i> (= Sunday 18 + Saturday 24 + Sunday 25)
   * </ul>
   *
   * @param startDate the start date of the interval
   * @param endDate the end date of the interval
   * @return the number of non-business days between 2 dates (both included). If the startDate is
   *     strictly after the endDate, return the opposite value of the same call with inverted
   *     parameters.
   */
  public static long getNumberOfNonBusinessDaysBetweenDates(
      LocalDate startDate, LocalDate endDate) {

    if (endDate.isBefore(startDate)) {
      return -getNumberOfNonBusinessDaysBetweenDates(endDate, startDate);
    }

    long nonBusinessDaysCount = 0;

    DayOfWeek startDateDayOfWeek = startDate.getDayOfWeek();
    DayOfWeek endDateDayOfWeek = endDate.getDayOfWeek();

    LocalDate lastSundayFromStartDate = startDate.minusDays(startDateDayOfWeek.getValue());
    LocalDate lastSundayFromEndDate = endDate.minusDays(endDateDayOfWeek.getValue());
    long fullWeekBetweenDates =
        ChronoUnit.WEEKS.between(lastSundayFromStartDate, lastSundayFromEndDate);

    if (fullWeekBetweenDates == 0) {
      return switch (endDateDayOfWeek) {
        case SUNDAY -> startDateDayOfWeek == DayOfWeek.SUNDAY ? 1L : 2L;
        case SATURDAY -> 1L;
        default -> 0L;
      };
    }

    if (startDateDayOfWeek == DayOfWeek.SUNDAY) {
      nonBusinessDaysCount -= 1;
    }

    switch (endDateDayOfWeek) {
      case SUNDAY:
        nonBusinessDaysCount += 2;
        break;
      case SATURDAY:
        nonBusinessDaysCount += 1;
        break;
      default:
        break;
    }

    nonBusinessDaysCount += 2 * fullWeekBetweenDates;

    return nonBusinessDaysCount;
  }

  /**
   * @param targetMonth: given java.time.Month to search boundaries on
   * @param referenceDate: date from which the result is searched
   * @return a MonthBoundaries object containing the start date and the end date of the first month
   *     in the past from the @param referenceDate exclusive corresponding to the given @param
   *     targetMonth
   */
  public static MonthBoundaries getLastMonthOccurrenceBoundaries(
      Month targetMonth, LocalDate referenceDate) {
    if (targetMonth == null || referenceDate == null) {
      return null;
    }

    MonthBoundaries monthBoundaries = new MonthBoundaries();
    Month referenceMonth = referenceDate.getMonth();
    int targetYear = referenceDate.getYear();
    if (targetMonth.compareTo(referenceMonth) >= 0) {
      targetYear = targetYear - 1;
    }

    monthBoundaries.firstDayOfMonth = LocalDate.of(targetYear, targetMonth, 1);
    monthBoundaries.lastDayOfMonth = monthBoundaries.firstDayOfMonth.plusMonths(1).minusDays(1);

    return monthBoundaries;
  }

  /**
   * @param targetMonth: given java.time.Month to search boundaries on
   * @param referenceDate: date from which the result is searched
   * @return a MonthBoundaries object containing the start date and the end date of the first month
   *     in the past from the @param referenceDate exclusive corresponding to the given @param
   *     targetMonth
   */
  public static MonthBoundaries getNextMonthOccurrenceBoundaries(
      Month targetMonth, LocalDate referenceDate) {
    if (targetMonth == null || referenceDate == null) {
      return null;
    }

    MonthBoundaries monthBoundaries = new MonthBoundaries();
    Month referenceMonth = referenceDate.getMonth();
    int targetYear = referenceDate.getYear();
    if (targetMonth.compareTo(referenceMonth) <= 0) {
      targetYear = targetYear + 1;
    }

    monthBoundaries.firstDayOfMonth = LocalDate.of(targetYear, targetMonth, 1);
    monthBoundaries.lastDayOfMonth = monthBoundaries.firstDayOfMonth.plusMonths(1).minusDays(1);

    return monthBoundaries;
  }
}
