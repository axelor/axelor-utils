package com.axelor.utils.date;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class DateToolsGetNumberOfNonBusinessDayBetweenDatesTest {

  private final LocalDate friday22_12_16 = LocalDate.of(2022, 12, 16);
  private final LocalDate saturday22_12_17 = LocalDate.of(2022, 12, 17);
  private final LocalDate sunday22_12_18 = LocalDate.of(2022, 12, 18);
  private final LocalDate monday22_12_19 = LocalDate.of(2022, 12, 19);
  private final LocalDate wednesday22_12_21 = LocalDate.of(2022, 12, 21);
  private final LocalDate friday22_12_23 = LocalDate.of(2022, 12, 23);
  private final LocalDate saturday22_12_24 = LocalDate.of(2022, 12, 24);
  private final LocalDate sunday22_12_25 = LocalDate.of(2022, 12, 25);
  private final LocalDate monday22_12_26 = LocalDate.of(2022, 12, 26);

  private final LocalDate saturday23_01_14 = LocalDate.of(2023, 1, 14);
  private final LocalDate sunday23_01_15 = LocalDate.of(2023, 1, 15);
  private final LocalDate monday23_01_16 = LocalDate.of(2023, 1, 16);
  private final LocalDate wednesday23_01_18 = LocalDate.of(2023, 1, 18);
  private final LocalDate friday23_01_20 = LocalDate.of(2023, 1, 20);
  private final LocalDate sunday23_01_22 = LocalDate.of(2023, 1, 22);

  private final LocalDate thursday1970_01_01 = LocalDate.of(1970, 1, 1);
  private final LocalDate thursday1970_01_08 = LocalDate.of(1970, 1, 8);

  @Test
  void test01() {

    long actual = DateTool.getNumberOfNonBusinessDaysBetweenDates(friday22_12_16, monday22_12_19);
    long expected = 2L;

    assertEquals(expected, actual);
  }

  @Test
  void test02() {

    long actual =
        DateTool.getNumberOfNonBusinessDaysBetweenDates(thursday1970_01_01, monday22_12_19);
    long expected = 5528L;

    assertEquals(expected, actual);
  }

  @Test
  void test03() {

    long actual =
        DateTool.getNumberOfNonBusinessDaysBetweenDates(thursday1970_01_01, thursday1970_01_08);
    long expected = 2L;

    assertEquals(expected, actual);
  }

  @Test
  void test04() {

    long actual =
        DateTool.getNumberOfNonBusinessDaysBetweenDates(sunday22_12_18, wednesday22_12_21);
    long expected = 1L;

    assertEquals(expected, actual);
  }

  @Test
  void test05() {

    long actual = DateTool.getNumberOfNonBusinessDaysBetweenDates(sunday22_12_18, sunday22_12_25);
    long expected = 3L;

    assertEquals(expected, actual);
  }

  @Test
  void test06() {

    long actual = DateTool.getNumberOfNonBusinessDaysBetweenDates(saturday22_12_17, friday22_12_23);
    long expected = 2L;
    assertEquals(expected, actual);

    actual = DateTool.getNumberOfNonBusinessDaysBetweenDates(friday22_12_23, saturday22_12_17);
    assertEquals(-expected, actual);
  }

  @Test
  void test07() {

    long actual = DateTool.getNumberOfNonBusinessDaysBetweenDates(saturday22_12_17, sunday22_12_25);
    long expected = 4L;
    assertEquals(expected, actual);

    actual = DateTool.getNumberOfNonBusinessDaysBetweenDates(sunday22_12_25, saturday22_12_17);
    assertEquals(-expected, actual);
  }

  @Test
  void test08() {

    long actual = DateTool.getNumberOfNonBusinessDaysBetweenDates(monday22_12_26, monday23_01_16);
    long expected = 6L;
    assertEquals(expected, actual);

    actual = DateTool.getNumberOfNonBusinessDaysBetweenDates(monday23_01_16, monday22_12_26);
    assertEquals(-expected, actual);
  }

  @Test
  void test09() {

    long actual = DateTool.getNumberOfNonBusinessDaysBetweenDates(monday22_12_26, friday23_01_20);
    long expected = 6L;
    assertEquals(expected, actual);

    actual = DateTool.getNumberOfNonBusinessDaysBetweenDates(friday23_01_20, monday22_12_26);
    assertEquals(-expected, actual);
  }

  @Test
  void test10() {

    long actual =
        DateTool.getNumberOfNonBusinessDaysBetweenDates(friday22_12_23, wednesday23_01_18);
    long expected = 8L;
    assertEquals(expected, actual);

    actual = DateTool.getNumberOfNonBusinessDaysBetweenDates(wednesday23_01_18, friday22_12_23);
    assertEquals(-expected, actual);
  }

  @Test
  void test11() {

    long actual = DateTool.getNumberOfNonBusinessDaysBetweenDates(friday22_12_23, saturday23_01_14);
    long expected = 7L;
    assertEquals(expected, actual);

    actual = DateTool.getNumberOfNonBusinessDaysBetweenDates(saturday23_01_14, friday22_12_23);
    assertEquals(-expected, actual);
  }

  @Test
  void test12() {

    long actual = DateTool.getNumberOfNonBusinessDaysBetweenDates(friday22_12_23, sunday23_01_22);
    long expected = 10L;
    assertEquals(expected, actual);

    actual = DateTool.getNumberOfNonBusinessDaysBetweenDates(sunday23_01_22, friday22_12_23);
    assertEquals(-expected, actual);
  }

  @Test
  void test13() {

    long actual = DateTool.getNumberOfNonBusinessDaysBetweenDates(sunday22_12_25, sunday23_01_15);
    long expected = 7L;
    assertEquals(expected, actual);

    actual = DateTool.getNumberOfNonBusinessDaysBetweenDates(sunday23_01_15, sunday22_12_25);
    assertEquals(-expected, actual);
  }

  @Test
  void test14() {

    long actual =
        DateTool.getNumberOfNonBusinessDaysBetweenDates(monday22_12_19, wednesday22_12_21);
    long expected = 0L;
    assertEquals(expected, actual);

    actual = DateTool.getNumberOfNonBusinessDaysBetweenDates(wednesday22_12_21, monday22_12_19);
    assertEquals(-expected, actual);
  }

  @Test
  void test15() {

    long actual = DateTool.getNumberOfNonBusinessDaysBetweenDates(monday22_12_19, monday22_12_19);
    long expected = 0L;
    assertEquals(expected, actual);

    actual = DateTool.getNumberOfNonBusinessDaysBetweenDates(monday22_12_19, monday22_12_19);
    assertEquals(-expected, actual);
  }

  @Test
  void test16() {

    long actual = DateTool.getNumberOfNonBusinessDaysBetweenDates(saturday22_12_24, sunday22_12_25);
    long expected = 2L;
    assertEquals(expected, actual);

    actual = DateTool.getNumberOfNonBusinessDaysBetweenDates(sunday22_12_25, saturday22_12_24);
    assertEquals(-expected, actual);
  }

  @Test
  void test17() {

    long actual =
        DateTool.getNumberOfNonBusinessDaysBetweenDates(saturday22_12_24, saturday22_12_24);
    long expected = 1L;
    assertEquals(expected, actual);
  }

  @Test
  void test18() {

    long actual = DateTool.getNumberOfNonBusinessDaysBetweenDates(sunday22_12_25, sunday22_12_25);
    long expected = 1L;
    assertEquals(expected, actual);
  }
}
