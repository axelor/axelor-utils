package com.axelor.utils.date;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LocalDateTimeUtilsGetNearOccurrencesTest {

  private final LocalDateTime monday_2022_12_26_12_26 = LocalDateTime.of(2022, 12, 26, 12, 26);
  private final LocalDateTime monday_2023_01_02_03_04 = LocalDateTime.of(2023, 1, 2, 03, 04);
  private final LocalDateTime thursday_2023_08_31_23_56 = LocalDateTime.of(2023, 8, 31, 23, 56);
  private final LocalDateTime saturday_2023_09_02_00_00 = LocalDateTime.of(2023, 9, 2, 0, 0);
  private final LocalDateTime wednesday_2023_09_06_00_14 = LocalDateTime.of(2023, 9, 6, 0, 14);
  private final LocalDateTime friday_2023_09_08_23_19 = LocalDateTime.of(2023, 9, 8, 23, 19);

  private final LocalTime midnight = LocalTime.of(0, 0);
  private final LocalTime at11_19 = LocalTime.of(11, 19);
  private final LocalTime at12_33 = LocalTime.of(12, 33);
  private final LocalTime at18_44 = LocalTime.of(18, 44);
  private final LocalTime at20_55 = LocalTime.of(18, 55);
  private final LocalTime at23_19 = LocalTime.of(23, 19);
  private final LocalTime at23_59 = LocalTime.of(23, 59);

  /** next occurrence being the next year */
  @Test
  void test01() {

    DayOfWeek occurrenceDayOfWeek = DayOfWeek.MONDAY;
    LocalTime occurrenceTime = midnight;
    LocalDateTime referenceDate = monday_2022_12_26_12_26;

    LocalDateTime lastOccurrence =
        LocalDateTimeUtils.getLastOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);
    LocalDateTime nextOccurrence =
        LocalDateTimeUtils.getNextOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);
    LocalDateTime nearestOccuence =
        LocalDateTimeUtils.getNearestOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);

    LocalDateTime expectedLastOccurrence = LocalDate.of(2022, 12, 26).atTime(occurrenceTime);
    LocalDateTime expectedNextOccurrence = LocalDate.of(2023, 1, 2).atTime(occurrenceTime);
    LocalDateTime expectedNearestOccuence = expectedLastOccurrence;

    Assertions.assertEquals(expectedLastOccurrence, lastOccurrence);
    Assertions.assertEquals(expectedNextOccurrence, nextOccurrence);
    Assertions.assertEquals(expectedNearestOccuence, nearestOccuence);
  }

  /** last occurrence being the precedent year */
  @Test
  void test02() {

    DayOfWeek occurrenceDayOfWeek = DayOfWeek.TUESDAY;
    LocalTime occurrenceTime = at12_33;
    LocalDateTime referenceDate = monday_2023_01_02_03_04;

    LocalDateTime lastOccurrence =
        LocalDateTimeUtils.getLastOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);
    LocalDateTime nextOccurrence =
        LocalDateTimeUtils.getNextOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);
    LocalDateTime nearestOccuence =
        LocalDateTimeUtils.getNearestOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);

    LocalDateTime expectedLastOccurrence = LocalDate.of(2022, 12, 27).atTime(occurrenceTime);
    LocalDateTime expectedNextOccurrence = LocalDate.of(2023, 1, 3).atTime(occurrenceTime);
    LocalDateTime expectedNearestOccuence = expectedNextOccurrence;

    Assertions.assertEquals(expectedLastOccurrence, lastOccurrence);
    Assertions.assertEquals(expectedNextOccurrence, nextOccurrence);
    Assertions.assertEquals(expectedNearestOccuence, nearestOccuence);
  }

  /**
   * occurrence day earlier than reference day on the week, with next occurrence on the next month
   */
  @Test
  void test03() {

    DayOfWeek occurrenceDayOfWeek = DayOfWeek.WEDNESDAY;
    LocalTime occurrenceTime = at18_44;
    LocalDateTime referenceDate = thursday_2023_08_31_23_56;

    LocalDateTime lastOccurrence =
        LocalDateTimeUtils.getLastOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);
    LocalDateTime nextOccurrence =
        LocalDateTimeUtils.getNextOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);
    LocalDateTime nearestOccuence =
        LocalDateTimeUtils.getNearestOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);

    LocalDateTime expectedLastOccurrence = LocalDate.of(2023, 8, 30).atTime(occurrenceTime);
    LocalDateTime expectedNextOccurrence = LocalDate.of(2023, 9, 6).atTime(occurrenceTime);
    LocalDateTime expectedNearestOccuence = expectedLastOccurrence;

    Assertions.assertEquals(expectedLastOccurrence, lastOccurrence);
    Assertions.assertEquals(expectedNextOccurrence, nextOccurrence);
    Assertions.assertEquals(expectedNearestOccuence, nearestOccuence);
  }

  /** occurrence day later than reference day on the week, with next occurrence on the next month */
  @Test
  void test04() {

    DayOfWeek occurrenceDayOfWeek = DayOfWeek.SUNDAY;
    LocalTime occurrenceTime = at18_44;
    LocalDateTime referenceDate = thursday_2023_08_31_23_56;

    LocalDateTime lastOccurrence =
        LocalDateTimeUtils.getLastOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);
    LocalDateTime nextOccurrence =
        LocalDateTimeUtils.getNextOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);
    LocalDateTime nearestOccuence =
        LocalDateTimeUtils.getNearestOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);

    LocalDateTime expectedLastOccurrence = LocalDate.of(2023, 8, 27).atTime(occurrenceTime);
    LocalDateTime expectedNextOccurrence = LocalDate.of(2023, 9, 3).atTime(occurrenceTime);
    LocalDateTime expectedNearestOccuence = expectedNextOccurrence;

    Assertions.assertEquals(expectedLastOccurrence, lastOccurrence);
    Assertions.assertEquals(expectedNextOccurrence, nextOccurrence);
    Assertions.assertEquals(expectedNearestOccuence, nearestOccuence);
  }

  /** occurrence day earlier than reference day, with last occurrence on the precedent month */
  @Test
  void test05() {

    DayOfWeek occurrenceDayOfWeek = DayOfWeek.THURSDAY;
    LocalTime occurrenceTime = at20_55;
    LocalDateTime referenceDate = saturday_2023_09_02_00_00;

    LocalDateTime lastOccurrence =
        LocalDateTimeUtils.getLastOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);
    LocalDateTime nextOccurrence =
        LocalDateTimeUtils.getNextOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);
    LocalDateTime nearestOccuence =
        LocalDateTimeUtils.getNearestOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);

    LocalDateTime expectedLastOccurrence = LocalDate.of(2023, 8, 31).atTime(occurrenceTime);
    LocalDateTime expectedNextOccurrence = LocalDate.of(2023, 9, 7).atTime(occurrenceTime);
    LocalDateTime expectedNearestOccuence = expectedLastOccurrence;

    Assertions.assertEquals(expectedLastOccurrence, lastOccurrence);
    Assertions.assertEquals(expectedNextOccurrence, nextOccurrence);
    Assertions.assertEquals(expectedNearestOccuence, nearestOccuence);
  }

  /** occurrence day later than reference day, with last occurrence on the precedent month */
  @Test
  void test06() {

    DayOfWeek occurrenceDayOfWeek = DayOfWeek.THURSDAY;
    LocalTime occurrenceTime = at20_55;
    LocalDateTime referenceDate = wednesday_2023_09_06_00_14;

    LocalDateTime lastOccurrence =
        LocalDateTimeUtils.getLastOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);
    LocalDateTime nextOccurrence =
        LocalDateTimeUtils.getNextOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);
    LocalDateTime nearestOccuence =
        LocalDateTimeUtils.getNearestOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);

    LocalDateTime expectedLastOccurrence = LocalDate.of(2023, 8, 31).atTime(occurrenceTime);
    LocalDateTime expectedNextOccurrence = LocalDate.of(2023, 9, 7).atTime(occurrenceTime);
    LocalDateTime expectedNearestOccuence = expectedNextOccurrence;

    Assertions.assertEquals(expectedLastOccurrence, lastOccurrence);
    Assertions.assertEquals(expectedNextOccurrence, nextOccurrence);
    Assertions.assertEquals(expectedNearestOccuence, nearestOccuence);
  }

  /** occurrence day equals reference day occurrence hour earlier than reference hour */
  @Test
  void test08() {

    DayOfWeek occurrenceDayOfWeek = DayOfWeek.FRIDAY;
    LocalTime occurrenceTime = at20_55;
    LocalDateTime referenceDate = friday_2023_09_08_23_19;

    LocalDateTime lastOccurrence =
        LocalDateTimeUtils.getLastOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);
    LocalDateTime nextOccurrence =
        LocalDateTimeUtils.getNextOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);
    LocalDateTime nearestOccuence =
        LocalDateTimeUtils.getNearestOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);

    LocalDateTime expectedLastOccurrence = LocalDate.of(2023, 9, 8).atTime(occurrenceTime);
    LocalDateTime expectedNextOccurrence = LocalDate.of(2023, 9, 15).atTime(occurrenceTime);
    LocalDateTime expectedNearestOccuence = expectedLastOccurrence;

    Assertions.assertEquals(expectedLastOccurrence, lastOccurrence);
    Assertions.assertEquals(expectedNextOccurrence, nextOccurrence);
    Assertions.assertEquals(expectedNearestOccuence, nearestOccuence);
  }

  /** occurrence day equals reference day occurrence hour later than reference hour */
  @Test
  void test09() {

    DayOfWeek occurrenceDayOfWeek = DayOfWeek.FRIDAY;
    LocalTime occurrenceTime = at23_59;
    LocalDateTime referenceDate = friday_2023_09_08_23_19;

    LocalDateTime lastOccurrence =
        LocalDateTimeUtils.getLastOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);
    LocalDateTime nextOccurrence =
        LocalDateTimeUtils.getNextOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);
    LocalDateTime nearestOccuence =
        LocalDateTimeUtils.getNearestOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);

    LocalDateTime expectedLastOccurrence = LocalDate.of(2023, 9, 1).atTime(occurrenceTime);
    LocalDateTime expectedNextOccurrence = LocalDate.of(2023, 9, 8).atTime(occurrenceTime);
    LocalDateTime expectedNearestOccuence = expectedNextOccurrence;

    Assertions.assertEquals(expectedLastOccurrence, lastOccurrence);
    Assertions.assertEquals(expectedNextOccurrence, nextOccurrence);
    Assertions.assertEquals(expectedNearestOccuence, nearestOccuence);
  }

  /** occurrence day equals reference day occurrence hour equals reference hour */
  @Test
  void test10() {

    DayOfWeek occurrenceDayOfWeek = DayOfWeek.FRIDAY;
    LocalTime occurrenceTime = at23_19;
    LocalDateTime referenceDate = friday_2023_09_08_23_19;

    LocalDateTime lastOccurrence =
        LocalDateTimeUtils.getLastOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);
    LocalDateTime nextOccurrence =
        LocalDateTimeUtils.getNextOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);
    LocalDateTime nearestOccuence =
        LocalDateTimeUtils.getNearestOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);

    LocalDateTime expectedLastOccurrence = friday_2023_09_08_23_19;
    LocalDateTime expectedNextOccurrence = friday_2023_09_08_23_19;
    LocalDateTime expectedNearestOccuence = friday_2023_09_08_23_19;

    Assertions.assertEquals(expectedLastOccurrence, lastOccurrence);
    Assertions.assertEquals(expectedNextOccurrence, nextOccurrence);
    Assertions.assertEquals(expectedNearestOccuence, nearestOccuence);
  }

  /** At the precise time between the last & next occurrences */
  @Test
  void test11() {

    DayOfWeek occurrenceDayOfWeek = DayOfWeek.TUESDAY;
    LocalTime occurrenceTime = at11_19;
    LocalDateTime referenceDate = friday_2023_09_08_23_19;

    LocalDateTime lastOccurrence =
        LocalDateTimeUtils.getLastOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);
    LocalDateTime nextOccurrence =
        LocalDateTimeUtils.getNextOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);
    LocalDateTime nearestOccuence =
        LocalDateTimeUtils.getNearestOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);

    LocalDateTime expectedLastOccurrence = LocalDate.of(2023, 9, 5).atTime(occurrenceTime);
    LocalDateTime expectedNextOccurrence = LocalDate.of(2023, 9, 12).atTime(occurrenceTime);
    LocalDateTime expectedNearestOccuence = expectedNextOccurrence;

    Assertions.assertEquals(expectedLastOccurrence, lastOccurrence);
    Assertions.assertEquals(expectedNextOccurrence, nextOccurrence);
    Assertions.assertEquals(expectedNearestOccuence, nearestOccuence);
  }

  /** Just 1 minute before the precise time between the last & next occurrences */
  @Test
  void test12() {

    DayOfWeek occurrenceDayOfWeek = DayOfWeek.TUESDAY;
    LocalTime occurrenceTime = at11_19;
    LocalDateTime referenceDate = friday_2023_09_08_23_19.minusMinutes(1);

    LocalDateTime lastOccurrence =
        LocalDateTimeUtils.getLastOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);
    LocalDateTime nextOccurrence =
        LocalDateTimeUtils.getNextOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);
    LocalDateTime nearestOccuence =
        LocalDateTimeUtils.getNearestOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);

    LocalDateTime expectedLastOccurrence = LocalDate.of(2023, 9, 5).atTime(occurrenceTime);
    LocalDateTime expectedNextOccurrence = LocalDate.of(2023, 9, 12).atTime(occurrenceTime);
    LocalDateTime expectedNearestOccuence = expectedLastOccurrence;

    Assertions.assertEquals(expectedLastOccurrence, lastOccurrence);
    Assertions.assertEquals(expectedNextOccurrence, nextOccurrence);
    Assertions.assertEquals(expectedNearestOccuence, nearestOccuence);
  }

  /** Just 1 minute after the precise time between the last & next occurrences */
  @Test
  void test13() {

    DayOfWeek occurrenceDayOfWeek = DayOfWeek.TUESDAY;
    LocalTime occurrenceTime = at11_19;
    LocalDateTime referenceDate = friday_2023_09_08_23_19.plusMinutes(1);

    LocalDateTime lastOccurrence =
        LocalDateTimeUtils.getLastOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);
    LocalDateTime nextOccurrence =
        LocalDateTimeUtils.getNextOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);
    LocalDateTime nearestOccuence =
        LocalDateTimeUtils.getNearestOccurrence(occurrenceDayOfWeek, occurrenceTime, referenceDate);

    LocalDateTime expectedLastOccurrence = LocalDate.of(2023, 9, 5).atTime(occurrenceTime);
    LocalDateTime expectedNextOccurrence = LocalDate.of(2023, 9, 12).atTime(occurrenceTime);
    LocalDateTime expectedNearestOccuence = expectedNextOccurrence;

    Assertions.assertEquals(expectedLastOccurrence, lastOccurrence);
    Assertions.assertEquals(expectedNextOccurrence, nextOccurrence);
    Assertions.assertEquals(expectedNearestOccuence, nearestOccuence);
  }
}
