package com.axelor.utils.date;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.axelor.utils.helpers.date.LocalDateHelper;
import com.axelor.utils.helpers.date.LocalDateTimeHelper;
import com.axelor.utils.helpers.date.MonthBoundaries;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import org.junit.jupiter.api.Test;

class LocalDateHelperLocalDateTimeUtilsGetNearMonthOccurrenceBoundariesTest {

  /**
   * Test the case of having a reference date (time) later on the year calendar than the given
   * target month
   */
  @Test
  void test01() {

    Month targetMonth = Month.JANUARY;
    LocalDate referenceDate = LocalDate.of(2023, 9, 8);
    LocalDateTime referenceDateTime = referenceDate.atTime(23, 19);

    MonthBoundaries actual =
        LocalDateHelper.getLastMonthOccurrenceBoundaries(targetMonth, referenceDate);
    MonthBoundaries expected = new MonthBoundaries();
    expected.firstDayOfMonth = LocalDate.of(2023, 1, 1);
    expected.lastDayOfMonth = LocalDate.of(2023, 1, 31);
    assertEquals(expected, actual);

    actual = LocalDateTimeHelper.getLastMonthOccurrenceBoundaries(targetMonth, referenceDateTime);
    assertEquals(expected, actual);

    actual = LocalDateHelper.getNextMonthOccurrenceBoundaries(targetMonth, referenceDate);
    expected = new MonthBoundaries();
    expected.firstDayOfMonth = LocalDate.of(2024, 1, 1);
    expected.lastDayOfMonth = LocalDate.of(2024, 1, 31);
    assertEquals(expected, actual);

    actual = LocalDateTimeHelper.getNextMonthOccurrenceBoundaries(targetMonth, referenceDateTime);
    assertEquals(expected, actual);
  }

  /**
   * Test the case of having a reference date (time) earlier on the year calendar than the given
   * target month
   */
  @Test
  void test02() {

    Month targetMonth = Month.DECEMBER;
    LocalDate referenceDate = LocalDate.of(2023, 9, 8);
    LocalDateTime referenceDateTime = referenceDate.atTime(23, 19);

    MonthBoundaries actual =
        LocalDateHelper.getLastMonthOccurrenceBoundaries(targetMonth, referenceDate);
    MonthBoundaries expected = new MonthBoundaries();
    expected.firstDayOfMonth = LocalDate.of(2022, 12, 1);
    expected.lastDayOfMonth = LocalDate.of(2022, 12, 31);
    assertEquals(expected, actual);

    actual = LocalDateTimeHelper.getLastMonthOccurrenceBoundaries(targetMonth, referenceDateTime);
    assertEquals(expected, actual);

    actual = LocalDateHelper.getNextMonthOccurrenceBoundaries(targetMonth, referenceDate);
    expected = new MonthBoundaries();
    expected.firstDayOfMonth = LocalDate.of(2023, 12, 1);
    expected.lastDayOfMonth = LocalDate.of(2023, 12, 31);
    assertEquals(expected, actual);

    actual = LocalDateTimeHelper.getNextMonthOccurrenceBoundaries(targetMonth, referenceDateTime);
    assertEquals(expected, actual);
  }

  /**
   * Test the case of having a reference date (time) the same month on the year calendar than the
   * given target month
   */
  @Test
  void test03() {

    Month targetMonth = Month.SEPTEMBER;
    LocalDate referenceDate = LocalDate.of(2023, 9, 8);
    LocalDateTime referenceDateTime = referenceDate.atTime(23, 19);

    MonthBoundaries actual =
        LocalDateHelper.getLastMonthOccurrenceBoundaries(targetMonth, referenceDate);
    MonthBoundaries expected = new MonthBoundaries();
    expected.firstDayOfMonth = LocalDate.of(2022, 9, 1);
    expected.lastDayOfMonth = LocalDate.of(2022, 9, 30);
    assertEquals(expected, actual);

    actual = LocalDateTimeHelper.getLastMonthOccurrenceBoundaries(targetMonth, referenceDateTime);
    assertEquals(expected, actual);

    actual = LocalDateHelper.getNextMonthOccurrenceBoundaries(targetMonth, referenceDate);
    expected = new MonthBoundaries();
    expected.firstDayOfMonth = LocalDate.of(2024, 9, 1);
    expected.lastDayOfMonth = LocalDate.of(2024, 9, 30);
    assertEquals(expected, actual);

    actual = LocalDateTimeHelper.getNextMonthOccurrenceBoundaries(targetMonth, referenceDateTime);
    assertEquals(expected, actual);
  }

  /** Test when the expected result is a boundaries from February in a leap year */
  @Test
  void test04() {

    MonthBoundaries expected = new MonthBoundaries();
    expected.firstDayOfMonth = LocalDate.of(2040, 2, 1);
    expected.lastDayOfMonth = LocalDate.of(2040, 2, 29);

    Month targetMonth = Month.FEBRUARY;
    LocalDate referenceDate = LocalDate.of(2040, 9, 8);
    LocalDateTime referenceDateTime = referenceDate.atTime(23, 19);

    MonthBoundaries actual =
        LocalDateHelper.getLastMonthOccurrenceBoundaries(targetMonth, referenceDate);
    assertEquals(expected, actual);

    actual = LocalDateTimeHelper.getLastMonthOccurrenceBoundaries(targetMonth, referenceDateTime);
    assertEquals(expected, actual);

    referenceDate = referenceDate.minusYears(1);
    referenceDateTime = referenceDateTime.minusYears(1);

    actual = LocalDateHelper.getNextMonthOccurrenceBoundaries(targetMonth, referenceDate);
    assertEquals(expected, actual);

    actual = LocalDateTimeHelper.getNextMonthOccurrenceBoundaries(targetMonth, referenceDateTime);
    assertEquals(expected, actual);
  }
}
