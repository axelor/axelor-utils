package com.axelor.utils.date;

import com.axelor.utils.helpers.date.LocalDateHelper;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LocalDateHelperTest {

  @Test
  void testGetNbDay() {
    Assertions.assertEquals(
        1, LocalDateHelper.daysBetween(LocalDate.now(), LocalDate.now(), false));
    Assertions.assertEquals(
        30,
        LocalDateHelper.daysBetween(LocalDate.of(2011, 9, 1), LocalDate.of(2011, 9, 30), false));
    Assertions.assertEquals(
        26, LocalDateHelper.daysBetween(LocalDate.of(2011, 2, 2), LocalDate.of(2011, 2, 27), true));
    Assertions.assertEquals(
        26,
        LocalDateHelper.daysBetween(LocalDate.of(2011, 2, 2), LocalDate.of(2011, 2, 27), false));
    Assertions.assertEquals(
        -26,
        LocalDateHelper.daysBetween(LocalDate.of(2011, 2, 27), LocalDate.of(2011, 2, 2), false));
    Assertions.assertEquals(
        -26,
        LocalDateHelper.daysBetween(LocalDate.of(2011, 2, 27), LocalDate.of(2011, 2, 2), true));
    Assertions.assertEquals(
        30, LocalDateHelper.daysBetween(LocalDate.of(2011, 2, 1), LocalDate.of(2011, 2, 28), true));
    Assertions.assertEquals(
        1, LocalDateHelper.daysBetween(LocalDate.of(2011, 7, 30), LocalDate.of(2011, 7, 31), true));
    Assertions.assertEquals(
        54, LocalDateHelper.daysBetween(LocalDate.of(2011, 7, 12), LocalDate.of(2011, 9, 5), true));
    Assertions.assertEquals(
        30,
        LocalDateHelper.daysBetween(LocalDate.of(2011, 7, 15), LocalDate.of(2011, 8, 14), true));
    Assertions.assertEquals(
        30, LocalDateHelper.daysBetween(LocalDate.of(2011, 7, 1), LocalDate.of(2011, 7, 31), true));
    Assertions.assertEquals(
        31,
        LocalDateHelper.daysBetween(LocalDate.of(2012, 2, 29), LocalDate.of(2012, 3, 30), true));
    Assertions.assertEquals(
        31,
        LocalDateHelper.daysBetween(LocalDate.of(2011, 2, 28), LocalDate.of(2011, 3, 30), true));
    Assertions.assertEquals(
        33,
        LocalDateHelper.daysBetween(LocalDate.of(2012, 2, 28), LocalDate.of(2012, 3, 30), true));
    Assertions.assertEquals(
        181,
        LocalDateHelper.daysBetween(LocalDate.of(2011, 12, 31), LocalDate.of(2012, 6, 30), true));
    Assertions.assertEquals(
        -68,
        LocalDateHelper.daysBetween(LocalDate.of(2011, 12, 9), LocalDate.of(2011, 10, 2), true));
  }

  @Test
  void testIsProrata() {

    // dateFrame1<date1<dateFrame2
    Assertions.assertTrue(
        LocalDateHelper.isProrata(
            LocalDate.of(2011, 7, 1),
            LocalDate.of(2011, 7, 15),
            LocalDate.of(2011, 7, 10),
            LocalDate.of(2011, 7, 30)));
    // dateFrame1<date2<dateFrame2
    Assertions.assertTrue(
        LocalDateHelper.isProrata(
            LocalDate.of(2011, 7, 1),
            LocalDate.of(2011, 7, 15),
            LocalDate.of(2011, 6, 1),
            LocalDate.of(2011, 7, 10)));
    // date1<dateFrame1 and dateFrame2<date2
    Assertions.assertTrue(
        LocalDateHelper.isProrata(
            LocalDate.of(2011, 7, 1),
            LocalDate.of(2011, 7, 15),
            LocalDate.of(2011, 6, 1),
            LocalDate.of(2011, 7, 30)));
    // dateFrame1=date1 and dateFrame2=date2
    Assertions.assertTrue(
        LocalDateHelper.isProrata(
            LocalDate.of(2011, 7, 1),
            LocalDate.of(2011, 7, 15),
            LocalDate.of(2011, 7, 1),
            LocalDate.of(2011, 7, 15)));
    // date1=dateFrame1
    Assertions.assertTrue(
        LocalDateHelper.isProrata(
            LocalDate.of(2011, 7, 1),
            LocalDate.of(2011, 7, 15),
            LocalDate.of(2011, 7, 1),
            LocalDate.of(2011, 7, 30)));
    // date1=dateFrame2
    Assertions.assertTrue(
        LocalDateHelper.isProrata(
            LocalDate.of(2011, 7, 1),
            LocalDate.of(2011, 7, 15),
            LocalDate.of(2011, 7, 15),
            LocalDate.of(2011, 7, 30)));
    // date2=dateFrame1
    Assertions.assertTrue(
        LocalDateHelper.isProrata(
            LocalDate.of(2011, 7, 1),
            LocalDate.of(2011, 7, 15),
            LocalDate.of(2011, 6, 1),
            LocalDate.of(2011, 7, 1)));
    // date2=dateFrame2
    Assertions.assertTrue(
        LocalDateHelper.isProrata(
            LocalDate.of(2011, 7, 1),
            LocalDate.of(2011, 7, 15),
            LocalDate.of(2011, 6, 1),
            LocalDate.of(2011, 7, 15)));
    // date2=null and date1<dateFrame1
    Assertions.assertTrue(
        LocalDateHelper.isProrata(
            LocalDate.of(2011, 7, 1), LocalDate.of(2011, 7, 15), LocalDate.of(2011, 6, 1), null));
    // date2=null and date1=dateFrame1
    Assertions.assertTrue(
        LocalDateHelper.isProrata(
            LocalDate.of(2011, 7, 1), LocalDate.of(2011, 7, 15), LocalDate.of(2011, 7, 1), null));
    // date2=null and date1>dateFrame1 and date1<dateFrame2
    Assertions.assertTrue(
        LocalDateHelper.isProrata(
            LocalDate.of(2011, 7, 1), LocalDate.of(2011, 7, 15), LocalDate.of(2011, 7, 10), null));
    // date2=null and date1=dateFrame2
    Assertions.assertTrue(
        LocalDateHelper.isProrata(
            LocalDate.of(2011, 7, 1), LocalDate.of(2011, 7, 15), LocalDate.of(2011, 7, 15), null));
    // date2=null and date1<dateFrame1
    Assertions.assertTrue(
        LocalDateHelper.isProrata(
            LocalDate.of(2011, 7, 1), LocalDate.of(2011, 7, 15), LocalDate.of(2011, 6, 1), null));
    // date2=null and date1>dateFrame2
    Assertions.assertFalse(
        LocalDateHelper.isProrata(
            LocalDate.of(2011, 7, 1), LocalDate.of(2011, 7, 15), LocalDate.of(2011, 8, 1), null));
    // date2<dateFrame1
    Assertions.assertFalse(
        LocalDateHelper.isProrata(
            LocalDate.of(2011, 7, 1),
            LocalDate.of(2011, 7, 15),
            LocalDate.of(2011, 6, 1),
            LocalDate.of(2011, 6, 30)));
    // date1>dateFrame2
    Assertions.assertFalse(
        LocalDateHelper.isProrata(
            LocalDate.of(2011, 7, 1),
            LocalDate.of(2011, 7, 15),
            LocalDate.of(2011, 8, 1),
            LocalDate.of(2011, 8, 30)));
  }

  @Test
  void testGetNbFullMonth() {
    Assertions.assertEquals(
        1,
        LocalDateHelper.days360MonthsBetween(LocalDate.of(2011, 1, 1), LocalDate.of(2011, 2, 5)));
    Assertions.assertEquals(
        0,
        LocalDateHelper.days360MonthsBetween(LocalDate.of(2011, 1, 1), LocalDate.of(2011, 1, 25)));
    Assertions.assertEquals(
        1,
        LocalDateHelper.days360MonthsBetween(
            LocalDate.of(2011, 12, 15), LocalDate.of(2012, 1, 25)));
    Assertions.assertEquals(
        1,
        LocalDateHelper.days360MonthsBetween(
            LocalDate.of(2011, 12, 15), LocalDate.of(2012, 1, 15)));
    Assertions.assertEquals(
        1,
        LocalDateHelper.days360MonthsBetween(
            LocalDate.of(2011, 12, 15), LocalDate.of(2012, 1, 14)));
    Assertions.assertEquals(
        0,
        LocalDateHelper.days360MonthsBetween(
            LocalDate.of(2011, 12, 15), LocalDate.of(2012, 1, 13)));
    Assertions.assertEquals(
        5,
        LocalDateHelper.days360MonthsBetween(LocalDate.of(2011, 10, 7), LocalDate.of(2012, 3, 9)));
    Assertions.assertEquals(
        1,
        LocalDateHelper.days360MonthsBetween(LocalDate.of(2011, 1, 31), LocalDate.of(2011, 2, 28)));
    Assertions.assertEquals(
        1,
        LocalDateHelper.days360MonthsBetween(LocalDate.of(2011, 3, 31), LocalDate.of(2011, 4, 30)));
    Assertions.assertEquals(
        -5,
        LocalDateHelper.days360MonthsBetween(LocalDate.of(2012, 3, 9), LocalDate.of(2011, 10, 7)));
    Assertions.assertEquals(
        -1,
        LocalDateHelper.days360MonthsBetween(LocalDate.of(2011, 4, 30), LocalDate.of(2011, 3, 31)));
  }

  @Test
  void testNextOccurency() {
    Assertions.assertEquals(
        LocalDate.of(2010, 11, 9),
        LocalDateHelper.nextOccurency(LocalDate.of(2010, 10, 7), LocalDate.of(2011, 3, 9), 2));
    Assertions.assertEquals(
        LocalDate.of(2010, 11, 9),
        LocalDateHelper.nextOccurency(LocalDate.of(2010, 10, 7), LocalDate.of(2011, 5, 9), 2));
    Assertions.assertEquals(
        LocalDate.of(2010, 8, 31),
        LocalDateHelper.nextOccurency(LocalDate.of(2010, 8, 7), LocalDate.of(2011, 4, 30), 1));
    Assertions.assertEquals(
        LocalDate.of(2010, 5, 9),
        LocalDateHelper.nextOccurency(LocalDate.of(2010, 3, 9), LocalDate.of(2011, 3, 9), 2));
  }

  @Test
  void testLastOccurency() {
    Assertions.assertEquals(
        LocalDate.of(2011, 3, 9),
        LocalDateHelper.lastOccurency(LocalDate.of(2010, 11, 9), LocalDate.of(2011, 5, 9), 4));
    Assertions.assertEquals(
        LocalDate.of(2011, 7, 9),
        LocalDateHelper.lastOccurency(LocalDate.of(2010, 11, 9), LocalDate.of(2011, 9, 9), 4));
    Assertions.assertEquals(
        LocalDate.of(2011, 7, 9),
        LocalDateHelper.lastOccurency(LocalDate.of(2010, 11, 9), LocalDate.of(2011, 10, 9), 4));
    Assertions.assertEquals(
        LocalDate.of(2011, 1, 9),
        LocalDateHelper.lastOccurency(LocalDate.of(2010, 11, 9), LocalDate.of(2011, 1, 9), 2));
    Assertions.assertEquals(
        LocalDate.of(2011, 7, 31),
        LocalDateHelper.lastOccurency(LocalDate.of(2007, 4, 30), LocalDate.of(2011, 8, 6), 1));
  }

  @Test
  void testCompareMonthDifferentMonth() {
    Assertions.assertFalse(
        LocalDateHelper.isInTheSameMonth(LocalDate.of(2022, 2, 28), LocalDate.of(2022, 3, 3)));
  }

  @Test
  void testCompareMonthSameMonthDifferentYear() {
    Assertions.assertFalse(
        LocalDateHelper.isInTheSameMonth(LocalDate.of(2022, 3, 13), LocalDate.of(2023, 3, 3)));
  }

  @Test
  void testCompareMonthSameMonthSameYear() {
    Assertions.assertTrue(
        LocalDateHelper.isInTheSameMonth(LocalDate.of(2022, 3, 13), LocalDate.of(2022, 3, 3)));
  }
}
