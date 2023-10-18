package com.axelor.utils.date;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.axelor.utils.helpers.date.LocalDateTimeHelper;
import com.axelor.utils.helpers.date.LocalDateTimeInterval;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LocalDateTimeHelperGetMergedIntervalContainingInstantTest {

  private LocalDateTime T = LocalDateTime.now();

  // i: ------------T------------
  // i: --------A________B-------
  // i: ----------C________D-----
  // o: --------A__________D-----
  @Test
  void getMergedIntervalContainingInstant_twoOverlappingBoundedIntervalsContainingTheInstant() {
    LocalDateTime A = T.plusDays(-4);
    LocalDateTime B = T.plusDays(5);
    LocalDateTime C = T.plusDays(-2);
    LocalDateTime D = T.plusDays(7);

    List<LocalDateTimeInterval> intervals = new ArrayList<>();
    intervals.add(new LocalDateTimeInterval(A, B));
    intervals.add(new LocalDateTimeInterval(C, D));

    LocalDateTimeInterval actual =
        LocalDateTimeHelper.getMergedIntervalContainingInstant(intervals, T);
    LocalDateTimeInterval expected = new LocalDateTimeInterval(A, D);

    assertEquals(expected.getStartDateT(), actual.getStartDateT());
    assertEquals(expected.getEndDateT(), actual.getEndDateT());
  }

  // i: ------------T------------
  // i: --------A________B-------
  // i: --------C________D-------
  // o: --------A________B-------
  @Test
  void getMergedIntervalContainingInstant_duplicatedIntervalsContainingTheInstantOnTheList() {
    LocalDateTime A = T.plusDays(-4);
    LocalDateTime B = T.plusDays(5);
    LocalDateTime C = A;
    LocalDateTime D = B;

    List<LocalDateTimeInterval> intervals = new ArrayList<>();
    intervals.add(new LocalDateTimeInterval(A, B));
    intervals.add(new LocalDateTimeInterval(C, D));

    LocalDateTimeInterval actual =
        LocalDateTimeHelper.getMergedIntervalContainingInstant(intervals, T);
    LocalDateTimeInterval expected = new LocalDateTimeInterval(A, B);

    assertEquals(expected.getStartDateT(), actual.getStartDateT());
    assertEquals(expected.getEndDateT(), actual.getEndDateT());
  }

  // i: ------------T------------
  // i: --------A________B-------
  // i: -------------------CD----
  // o: --------A________B-------
  @Test
  void test06() {
    LocalDateTime A = T.plusDays(-4);
    LocalDateTime B = T.plusDays(5);
    LocalDateTime C = T.plusDays(7);
    LocalDateTime D = T.plusDays(8);

    List<LocalDateTimeInterval> intervals = new ArrayList<>();
    intervals.add(new LocalDateTimeInterval(A, B));
    intervals.add(new LocalDateTimeInterval(C, D));

    LocalDateTimeInterval actual =
        LocalDateTimeHelper.getMergedIntervalContainingInstant(intervals, T);
    LocalDateTimeInterval expected = new LocalDateTimeInterval(A, B);

    assertEquals(expected.getStartDateT(), actual.getStartDateT());
    assertEquals(expected.getEndDateT(), actual.getEndDateT());
  }

  // i: ------------T------------
  // i: --------A________________
  // o: --------A________________
  @Test
  void getMergedIntervalContainingInstant_onlyOneIntervalContainingTheInstant() {
    LocalDateTime A = T.plusDays(-4);
    LocalDateTime B = null;

    List<LocalDateTimeInterval> intervals = new ArrayList<>();
    intervals.add(new LocalDateTimeInterval(A, B));

    LocalDateTimeInterval actual =
        LocalDateTimeHelper.getMergedIntervalContainingInstant(intervals, T);
    LocalDateTimeInterval expected = new LocalDateTimeInterval(A, null);

    assertEquals(expected.getStartDateT(), actual.getStartDateT());
    assertEquals(expected.getEndDateT(), actual.getEndDateT());
  }

  // i: ------------T------------
  // i: --------A________B-------
  // i: --------C________________
  // o: --------A________________
  @Test
  void test09() {
    LocalDateTime A = T.plusDays(-4);
    LocalDateTime B = T.plusDays(5);
    LocalDateTime C = A;
    LocalDateTime D = null;

    List<LocalDateTimeInterval> intervals = new ArrayList<>();
    intervals.add(new LocalDateTimeInterval(A, B));
    intervals.add(new LocalDateTimeInterval(C, D));

    LocalDateTimeInterval actual =
        LocalDateTimeHelper.getMergedIntervalContainingInstant(intervals, T);
    LocalDateTimeInterval expected = new LocalDateTimeInterval(A, null);

    assertEquals(expected.getStartDateT(), actual.getStartDateT());
    assertEquals(expected.getEndDateT(), actual.getEndDateT());
  }

  // i: ------------T------------
  // i: --------A________________
  // i: --------C________________
  // o: --------A________________
  @Test
  void test10() {
    LocalDateTime A = T.plusDays(-4);
    LocalDateTime B = null;
    LocalDateTime C = A;
    LocalDateTime D = null;

    List<LocalDateTimeInterval> intervals = new ArrayList<>();
    intervals.add(new LocalDateTimeInterval(A, B));
    intervals.add(new LocalDateTimeInterval(C, D));

    LocalDateTimeInterval actual =
        LocalDateTimeHelper.getMergedIntervalContainingInstant(intervals, T);
    LocalDateTimeInterval expected = new LocalDateTimeInterval(A, null);

    assertEquals(expected.getStartDateT(), actual.getStartDateT());
    assertEquals(expected.getEndDateT(), actual.getEndDateT());
  }

  // i: ----T--------------------
  // i: --------A----------------
  // i: ------------B------------
  // i: ----------------C__D-----
  // o: null (no matter the continuoustolerance)
  @Test
  void getMergedIntervalContainingInstant_manyIntervalAndTheInstantIsBeforeTheStartOfAnyOfThem() {
    LocalDateTime A = T.plusDays(3);
    LocalDateTime B = A.plusDays(3);
    LocalDateTime C = B.plusDays(3);
    LocalDateTime D = C.plusDays(3);

    // tolerance = 0
    List<LocalDateTimeInterval> intervals = new ArrayList<>();
    intervals.add(new LocalDateTimeInterval(A, A, ChronoUnit.NANOS, 0));
    intervals.add(new LocalDateTimeInterval(B, D, ChronoUnit.NANOS, 0));
    intervals.add(new LocalDateTimeInterval(C, D, ChronoUnit.NANOS, 0));

    LocalDateTimeInterval actual =
        LocalDateTimeHelper.getMergedIntervalContainingInstant(intervals, T);
    LocalDateTimeInterval expected = null;

    assertEquals(actual, expected);

    // tolerance inferior to the gap between T and the intervals
    intervals = new ArrayList<>();
    intervals.add(new LocalDateTimeInterval(A, A, ChronoUnit.SECONDS, 12));
    intervals.add(new LocalDateTimeInterval(B, D, ChronoUnit.MINUTES, 12));
    intervals.add(new LocalDateTimeInterval(C, D, ChronoUnit.HOURS, 12));

    actual = LocalDateTimeHelper.getMergedIntervalContainingInstant(intervals, T);
    expected = null;

    assertEquals(actual, expected);

    // tolerance superior than the gap between T and the intervals
    intervals = new ArrayList<>();
    intervals.add(new LocalDateTimeInterval(A, A, ChronoUnit.DAYS, 50));
    intervals.add(new LocalDateTimeInterval(B, D, ChronoUnit.CENTURIES, 50));
    intervals.add(new LocalDateTimeInterval(C, D, ChronoUnit.MILLENNIA, 50));

    actual = LocalDateTimeHelper.getMergedIntervalContainingInstant(intervals, T);
    expected = null;

    assertEquals(actual, expected);

    // Infinite tolerance
    intervals = new ArrayList<>();
    intervals.add(new LocalDateTimeInterval(A, A, ChronoUnit.FOREVER, 1));
    intervals.add(new LocalDateTimeInterval(B, D, ChronoUnit.FOREVER, 2));
    intervals.add(new LocalDateTimeInterval(C, D, ChronoUnit.FOREVER, 503));

    actual = LocalDateTimeHelper.getMergedIntervalContainingInstant(intervals, T);
    expected = null;

    assertEquals(actual, expected);
  }

  // i: ------------T------------
  // i: -----A__B----------------
  // i: ----------------C__D-----
  // o: depends on the tolerance at the end of AB
  @Test
  void
      getMergedIntervalContainingInstant_twoDisontinousIntervalsAndTheInstantBetweenThe2Intervals() {
    LocalDateTime A = T.plusDays(-8);
    LocalDateTime B = T.plusDays(-4);
    LocalDateTime C = T.plusDays(4);
    LocalDateTime D = T.plusDays(8);

    // tolerance = 0 after AB
    List<LocalDateTimeInterval> intervals = new ArrayList<>();
    intervals.add(new LocalDateTimeInterval(A, B, ChronoUnit.NANOS, 0));
    intervals.add(new LocalDateTimeInterval(C, D));

    LocalDateTimeInterval actual =
        LocalDateTimeHelper.getMergedIntervalContainingInstant(intervals, T);
    LocalDateTimeInterval expected = null;

    assertEquals(actual, expected);

    // tolerance inferior to the gap between B and C
    intervals = new ArrayList<>();
    intervals.add(new LocalDateTimeInterval(A, B, ChronoUnit.HOURS, 1));
    intervals.add(new LocalDateTimeInterval(C, D));

    actual = LocalDateTimeHelper.getMergedIntervalContainingInstant(intervals, T);
    expected = null;

    assertEquals(actual, expected);

    // tolerance equal to the gap between B and C
    intervals = new ArrayList<>();
    intervals.add(new LocalDateTimeInterval(A, B, ChronoUnit.DAYS, 8));
    intervals.add(new LocalDateTimeInterval(C, D));

    actual = LocalDateTimeHelper.getMergedIntervalContainingInstant(intervals, T);
    expected = new LocalDateTimeInterval(A, D);

    assertEquals(expected.getStartDateT(), actual.getStartDateT());
    assertEquals(expected.getEndDateT(), actual.getEndDateT());

    // tolerance superior to the gap between B and C
    intervals = new ArrayList<>();
    intervals.add(new LocalDateTimeInterval(A, B, ChronoUnit.DAYS, 10));
    intervals.add(new LocalDateTimeInterval(C, D));

    actual = LocalDateTimeHelper.getMergedIntervalContainingInstant(intervals, T);
    expected = new LocalDateTimeInterval(A, D);

    assertEquals(expected.getStartDateT(), actual.getStartDateT());
    assertEquals(expected.getEndDateT(), actual.getEndDateT());

    // tolerance superior to the gap between B and D
    intervals = new ArrayList<>();
    intervals.add(new LocalDateTimeInterval(A, B, ChronoUnit.YEARS, 1));
    intervals.add(new LocalDateTimeInterval(C, D));

    actual = LocalDateTimeHelper.getMergedIntervalContainingInstant(intervals, T);
    expected = new LocalDateTimeInterval(A, D);

    assertEquals(expected.getStartDateT(), actual.getStartDateT());
    assertEquals(expected.getEndDateT(), actual.getEndDateT());
  }

  // i: ------------T------------
  // i: --------A________________
  // i: ----C_______________D----
  // o: ----C____________________
  @Test
  void getMergedIntervalContainingInstant_twoContinousIntervalsAndOneBothCOntainingTheInstant() {
    LocalDateTime A = T.plusDays(-4);
    LocalDateTime B = null;
    LocalDateTime C = T.plusDays(-8);
    LocalDateTime D = T.plusDays(8);

    List<LocalDateTimeInterval> intervals = new ArrayList<>();
    intervals.add(new LocalDateTimeInterval(A, B));
    intervals.add(new LocalDateTimeInterval(C, D));

    LocalDateTimeInterval actual =
        LocalDateTimeHelper.getMergedIntervalContainingInstant(intervals, T);
    LocalDateTimeInterval expected = new LocalDateTimeInterval(C, null);

    assertEquals(expected.getStartDateT(), actual.getStartDateT());
    assertEquals(expected.getEndDateT(), actual.getEndDateT());
  }

  // i: ------------T------------
  // i: ________B---------------
  // i: ----------C______________
  // o: depends on the tolerance at the end of AB
  @Test
  void getMergedIntervalContainingInstant_twoDiscontinuousIntervalsAndOneContainingTheInstant() {
    LocalDateTime A = null;
    LocalDateTime B = T.plusDays(-3);
    LocalDateTime C = T.plusDays(-2);
    LocalDateTime D = null;

    // tolerance = 0 after AB
    List<LocalDateTimeInterval> intervals = new ArrayList<>();
    intervals.add(new LocalDateTimeInterval(A, B, ChronoUnit.NANOS, 0));
    intervals.add(new LocalDateTimeInterval(C, D));

    LocalDateTimeInterval actual =
        LocalDateTimeHelper.getMergedIntervalContainingInstant(intervals, T);
    LocalDateTimeInterval expected = new LocalDateTimeInterval(C, null);

    assertEquals(expected.getStartDateT(), actual.getStartDateT());
    assertEquals(expected.getEndDateT(), actual.getEndDateT());

    // tolerance inferior to the gap between B and C
    intervals = new ArrayList<>();
    intervals.add(new LocalDateTimeInterval(A, B, ChronoUnit.HOURS, 1));
    intervals.add(new LocalDateTimeInterval(C, D));

    actual = LocalDateTimeHelper.getMergedIntervalContainingInstant(intervals, T);
    expected = new LocalDateTimeInterval(C, null);

    assertEquals(expected.getStartDateT(), actual.getStartDateT());
    assertEquals(expected.getEndDateT(), actual.getEndDateT());

    // tolerance equal to the gap between B and C
    intervals = new ArrayList<>();
    intervals.add(new LocalDateTimeInterval(A, B, ChronoUnit.DAYS, 1));
    intervals.add(new LocalDateTimeInterval(C, D));

    actual = LocalDateTimeHelper.getMergedIntervalContainingInstant(intervals, T);
    expected = new LocalDateTimeInterval(null, null);

    assertEquals(expected.getStartDateT(), actual.getStartDateT());
    assertEquals(expected.getEndDateT(), actual.getEndDateT());

    // tolerance superior to the gap between B and C
    intervals = new ArrayList<>();
    intervals.add(new LocalDateTimeInterval(A, B, ChronoUnit.DAYS, 2));
    intervals.add(new LocalDateTimeInterval(C, D));

    actual = LocalDateTimeHelper.getMergedIntervalContainingInstant(intervals, T);
    expected = new LocalDateTimeInterval(null, null);

    assertEquals(expected.getStartDateT(), actual.getStartDateT());
    assertEquals(expected.getEndDateT(), actual.getEndDateT());

    // tolerance superior to the gap between B and D
    intervals = new ArrayList<>();
    intervals.add(new LocalDateTimeInterval(A, B, ChronoUnit.YEARS, 1));
    intervals.add(new LocalDateTimeInterval(C, D));

    actual = LocalDateTimeHelper.getMergedIntervalContainingInstant(intervals, T);
    expected = new LocalDateTimeInterval(null, null);

    assertEquals(expected.getStartDateT(), actual.getStartDateT());
    assertEquals(expected.getEndDateT(), actual.getEndDateT());
  }

  // i: ------------T------------
  // i: ------------A_B----------
  // i: ----------C_D------------
  // i: ----E_____F--------------
  // i: --------------G__H-------
  // i: --------------------I__J-
  // i: _L-----------------------
  // o: ----E____________H-------
  @Test
  void getMergedIntervalContainingInstant_manyIntervalsComplexExample() {
    LocalDateTime A = T;
    LocalDateTime B = A.plusDays(2);
    LocalDateTime C = A.plusDays(-2);
    // [DA] < continuousTolerance
    LocalDateTime D = A.plusNanos(-1);
    LocalDateTime E = C.minusDays(0);
    LocalDateTime F = C;
    // [BG] = continuousTolerance
    LocalDateTime G = B.plusMinutes(1);
    LocalDateTime H = T.plusDays(5);
    // [IH] > continuousTolerance
    LocalDateTime I = H.plusMinutes(2);
    LocalDateTime J = T.plusDays(10);
    LocalDateTime K = null;
    // [LE] > continuousTolerance
    LocalDateTime L = T.plusDays(-10);

    List<LocalDateTimeInterval> intervals = new ArrayList<>();
    // Default continuousTolerance is 1 minute
    intervals.add(new LocalDateTimeInterval(A, B));
    intervals.add(new LocalDateTimeInterval(C, D));
    intervals.add(new LocalDateTimeInterval(E, F));
    intervals.add(new LocalDateTimeInterval(G, H));
    intervals.add(new LocalDateTimeInterval(I, J));
    intervals.add(new LocalDateTimeInterval(K, L));

    LocalDateTimeInterval actual =
        LocalDateTimeHelper.getMergedIntervalContainingInstant(intervals, T);
    LocalDateTimeInterval expected = new LocalDateTimeInterval(E, H);

    assertEquals(expected.getStartDateT(), actual.getStartDateT());
    assertEquals(expected.getEndDateT(), actual.getEndDateT());
  }

  // i: ------------T------------
  // i: ------------AB-----------
  // i: ----------CD-------------
  // i: ----E_____F--------------
  // i: --------------G__H-------
  // i: -------------------I__J--
  // i: __L----------------------
  // i: _________________________
  // o: _________________________
  @Test
  void getMergedIntervalContainingInstant_manyIntervalsAndOneInfinite() {
    LocalDateTime A = T;
    LocalDateTime B = T.plusDays(1);
    LocalDateTime C = T.plusDays(-2);
    LocalDateTime D = T.plusDays(-1);
    LocalDateTime E = T.plusDays(-8);
    LocalDateTime F = T.plusDays(-2);
    LocalDateTime G = T.plusDays(2);
    LocalDateTime H = T.plusDays(5);
    LocalDateTime I = T.plusDays(7);
    LocalDateTime J = T.plusDays(10);
    LocalDateTime K = null;
    LocalDateTime L = T.plusDays(-10);
    LocalDateTime M = null;
    LocalDateTime N = null;

    List<LocalDateTimeInterval> intervals = new ArrayList<>();
    intervals.add(new LocalDateTimeInterval(A, B));
    intervals.add(new LocalDateTimeInterval(C, D));
    intervals.add(new LocalDateTimeInterval(E, F));
    intervals.add(new LocalDateTimeInterval(G, H));
    intervals.add(new LocalDateTimeInterval(I, J));
    intervals.add(new LocalDateTimeInterval(K, L));
    intervals.add(new LocalDateTimeInterval(M, N));

    LocalDateTimeInterval actual =
        LocalDateTimeHelper.getMergedIntervalContainingInstant(intervals, T);
    LocalDateTimeInterval expected = new LocalDateTimeInterval(null, null);

    assertEquals(expected.getStartDateT(), actual.getStartDateT());
    assertEquals(expected.getEndDateT(), actual.getEndDateT());
  }

  // i: {}
  // i: ------------T------------
  // o: null
  @Test
  void getMergedIntervalContainingInstant_emptyIntervalCollection() {

    LocalDateTimeInterval actual =
        LocalDateTimeHelper.getMergedIntervalContainingInstant(Collections.emptyList(), T);
    LocalDateTimeInterval expected = null;

    assertEquals(actual, expected);
  }

  // i: intervals = null
  // i: ------------T------------
  // o: null
  @Test
  void getMergedIntervalContainingInstant_nullIntervalCollection() {

    LocalDateTimeInterval actual = LocalDateTimeHelper.getMergedIntervalContainingInstant(null, T);
    LocalDateTimeInterval expected = null;

    assertEquals(actual, expected);
  }

  // i: -----------A_B-----------
  // i: T = null
  // o: IllegalArgumentException
  @Test()
  void getMergedIntervalContainingInstant_nullInstant() {

    LocalDateTime A = T.plusDays(-1);
    LocalDateTime B = T.plusDays(1);

    List<LocalDateTimeInterval> intervals = new ArrayList<>();
    intervals.add(new LocalDateTimeInterval(A, B));

    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> LocalDateTimeHelper.getMergedIntervalContainingInstant(intervals, null));
  }
}
