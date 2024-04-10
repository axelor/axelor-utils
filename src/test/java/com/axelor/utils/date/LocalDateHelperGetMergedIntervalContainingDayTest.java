package com.axelor.utils.date;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.axelor.utils.helpers.date.LocalDateHelper;
import com.axelor.utils.helpers.date.LocalDateInterval;
import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LocalDateHelperGetMergedIntervalContainingDayTest {

  private LocalDate T = LocalDate.of(2021, 10, 5);

  // i: --------A________B-------
  // i: ------C___D--------------
  // i: ------------T------------
  // o: ------C__________B-------
  @Test
  void getMergedIntervalContainingDay_twoOverlappingBoundedIntervalsOneContainingTheGivenDate() {
    LocalDate A = T.plusDays(-4);
    LocalDate B = T.plusDays(5);
    LocalDate C = T.plusDays(-6);
    LocalDate D = T.plusDays(-2);

    List<LocalDateInterval> intervals = new ArrayList<>();
    intervals.add(new LocalDateInterval(A, B));
    intervals.add(new LocalDateInterval(C, D));

    LocalDateInterval actual = LocalDateHelper.getMergedIntervalContainingDay(intervals, T);
    LocalDateInterval expected = new LocalDateInterval(C, B);

    assertEquals(expected.getStartDate(), actual.getStartDate());
    assertEquals(expected.getEndDate(), actual.getEndDate());
  }

  // i: --------A________B-------
  // i: ----------C________D-----
  // i: ------------T------------
  // o: --------A__________D-----
  @Test
  void getMergedIntervalContainingDay_twoOverlappingBoundedIntervalsBothContainingTheGivenDate() {
    LocalDate A = T.plusDays(-4);
    LocalDate B = T.plusDays(5);
    LocalDate C = T.plusDays(-2);
    LocalDate D = T.plusDays(7);

    List<LocalDateInterval> intervals = new ArrayList<>();
    intervals.add(new LocalDateInterval(A, B));
    intervals.add(new LocalDateInterval(C, D));

    LocalDateInterval actual = LocalDateHelper.getMergedIntervalContainingDay(intervals, T);
    LocalDateInterval expected = new LocalDateInterval(A, D);

    assertEquals(expected.getStartDate(), actual.getStartDate());
    assertEquals(expected.getEndDate(), actual.getEndDate());
  }

  // i: --------A________B-------
  // i: --------C________D-------
  // i: ------------T------------
  // o: --------A________B-------
  @Test
  void getMergedIntervalContainingDay_twoSameIntervalsContainingTheGivenDate() {
    LocalDate A = T.plusDays(-4);
    LocalDate B = T.plusDays(5);
    LocalDate C = A;
    LocalDate D = B;

    List<LocalDateInterval> intervals = new ArrayList<>();
    intervals.add(new LocalDateInterval(A, B));
    intervals.add(new LocalDateInterval(C, D));

    LocalDateInterval actual = LocalDateHelper.getMergedIntervalContainingDay(intervals, T);
    LocalDateInterval expected = new LocalDateInterval(A, B);

    assertEquals(expected.getStartDate(), actual.getStartDate());
    assertEquals(expected.getEndDate(), actual.getEndDate());
  }

  // i: --------A________B-------
  // i: ----------C____D---------
  // i: ------------T------------
  // o: --------A________B-------
  @Test
  void getMergedIntervalContainingDay_oneIntervalIncludedInTheOtherAndTheGivenDateIncludedInBoth() {
    LocalDate A = T.plusDays(-4);
    LocalDate B = T.plusDays(5);
    LocalDate C = T.plusDays(-2);
    LocalDate D = T.plusDays(3);

    List<LocalDateInterval> intervals = new ArrayList<>();
    intervals.add(new LocalDateInterval(A, B));
    intervals.add(new LocalDateInterval(C, D));

    LocalDateInterval actual = LocalDateHelper.getMergedIntervalContainingDay(intervals, T);
    LocalDateInterval expected = new LocalDateInterval(A, B);

    assertEquals(expected.getStartDate(), actual.getStartDate());
    assertEquals(expected.getEndDate(), actual.getEndDate());
  }

  // i: --------A________B-------
  // i: ------CD-----------------
  // i: ------------T------------
  // o: ------C__________B-------
  @Test
  void getMergedIntervalContainingDay_twoContinuousButNotOverlappingIntervals() {
    LocalDate A = T.plusDays(-4);
    LocalDate B = T.plusDays(5);
    LocalDate C = T.plusDays(-6);
    LocalDate D = T.plusDays(-5);

    List<LocalDateInterval> intervals = new ArrayList<>();
    intervals.add(new LocalDateInterval(A, B));
    intervals.add(new LocalDateInterval(C, D));

    LocalDateInterval actual = LocalDateHelper.getMergedIntervalContainingDay(intervals, T);
    LocalDateInterval expected = new LocalDateInterval(C, B);

    assertEquals(expected.getStartDate(), actual.getStartDate());
    assertEquals(expected.getEndDate(), actual.getEndDate());
  }

  // i: --------A________B-------
  // i: -------------------CD----
  // i: ------------T------------
  // o: --------A________B-------
  @Test
  void
      getMergedIntervalContainingDay_twoDiscontinuousIntervalsAndTheEarliestOneContainsTheGivenDate() {
    LocalDate A = T.plusDays(-4);
    LocalDate B = T.plusDays(5);
    LocalDate C = T.plusDays(7);
    LocalDate D = T.plusDays(8);

    List<LocalDateInterval> intervals = new ArrayList<>();
    intervals.add(new LocalDateInterval(A, B));
    intervals.add(new LocalDateInterval(C, D));

    LocalDateInterval actual = LocalDateHelper.getMergedIntervalContainingDay(intervals, T);
    LocalDateInterval expected = new LocalDateInterval(A, B);

    assertEquals(expected.getStartDate(), actual.getStartDate());
    assertEquals(expected.getEndDate(), actual.getEndDate());
  }

  // i: --------A________________
  // i: ---C__D------------------
  // i: ------------T------------
  // o: --------A________________
  @Test
  void
      getMergedIntervalContainingDay_twoDiscontinuousIntervalsAndTheLatestOneContainsTheGivenDate() {
    LocalDate A = T.plusDays(-4);
    LocalDate B = null;
    LocalDate C = T.plusDays(-9);
    LocalDate D = T.plusDays(-6);

    List<LocalDateInterval> intervals = new ArrayList<>();
    intervals.add(new LocalDateInterval(A, B));
    intervals.add(new LocalDateInterval(C, D));

    LocalDateInterval actual = LocalDateHelper.getMergedIntervalContainingDay(intervals, T);
    LocalDateInterval expected = new LocalDateInterval(A, null);

    assertEquals(expected.getStartDate(), actual.getStartDate());
    assertEquals(expected.getEndDate(), actual.getEndDate());
  }

  // i: --------A________________
  // i: ----------C__D-----------
  // i: ------------T------------
  // o: --------A________________
  @Test
  void
      getMergedIntervalContainingDay_oneBoundedIntervalIncludedInOneUnboundedIntervalAndTHeGivenDateIncludedInBoth() {
    LocalDate A = T.plusDays(-4);
    LocalDate B = null;
    LocalDate C = T.plusDays(-2);
    LocalDate D = T.plusDays(1);

    List<LocalDateInterval> intervals = new ArrayList<>();
    intervals.add(new LocalDateInterval(A, B));
    intervals.add(new LocalDateInterval(C, D));

    LocalDateInterval actual = LocalDateHelper.getMergedIntervalContainingDay(intervals, T);
    LocalDateInterval expected = new LocalDateInterval(A, null);

    assertEquals(expected.getStartDate(), actual.getStartDate());
    assertEquals(expected.getEndDate(), actual.getEndDate());
  }

  // i: --------A________________
  // i: --------C________________
  // i: ------------T------------
  // o: --------A________________
  @Test
  void test10() {
    LocalDate A = T.plusDays(-4);
    LocalDate B = null;
    LocalDate C = A;
    LocalDate D = null;

    List<LocalDateInterval> intervals = new ArrayList<>();
    intervals.add(new LocalDateInterval(A, B));
    intervals.add(new LocalDateInterval(C, D));

    LocalDateInterval actual = LocalDateHelper.getMergedIntervalContainingDay(intervals, T);
    LocalDateInterval expected = new LocalDateInterval(A, null);

    assertEquals(expected.getStartDate(), actual.getStartDate());
    assertEquals(expected.getEndDate(), actual.getEndDate());
  }

  // i: --------A----------------
  // i: --------C----------------
  // i: ------------T------------
  // o: null
  @Test
  void getMergedIntervalContainingDay_two1DayIntervalsUnequallingTheGivenDate() {
    LocalDate A = T.plusDays(-4);
    LocalDate C = A;

    List<LocalDateInterval> intervals = new ArrayList<>();
    intervals.add(new LocalDateInterval(A, A));
    intervals.add(new LocalDateInterval(C, C));

    LocalDateInterval actual = LocalDateHelper.getMergedIntervalContainingDay(intervals, T);
    LocalDateInterval expected = null;

    assertEquals(actual, expected);
  }

  // i: -----A__B----------------
  // i: ----------------C__D-----
  // i: ------------T------------
  // o: null
  @Test
  void getMergedIntervalContainingDay_twoDiscontinuousIntervalsNonContainingTheGivenDate() {
    LocalDate A = T.plusDays(-8);
    LocalDate B = T.plusDays(-4);
    LocalDate C = T.plusDays(4);
    LocalDate D = T.plusDays(8);

    List<LocalDateInterval> intervals = new ArrayList<>();
    intervals.add(new LocalDateInterval(A, B));
    intervals.add(new LocalDateInterval(C, D));

    LocalDateInterval actual = LocalDateHelper.getMergedIntervalContainingDay(intervals, T);
    LocalDateInterval expected = null;

    assertEquals(actual, expected);
  }

  // i: _________B---------------
  // i: ----------C______________
  // i: ------------T------------
  // o: _________________________
  @Test
  void
      getMergedIntervalContainingDay_twoContinuousButNotOverlappingIntervalsAndOneContainingTheGivenDate() {
    LocalDate A = null;
    LocalDate B = T.plusDays(-3);
    LocalDate C = T.plusDays(-2);
    LocalDate D = null;

    List<LocalDateInterval> intervals = new ArrayList<>();
    intervals.add(new LocalDateInterval(A, B));
    intervals.add(new LocalDateInterval(C, D));

    LocalDateInterval actual = LocalDateHelper.getMergedIntervalContainingDay(intervals, T);
    LocalDateInterval expected = new LocalDateInterval(null, null);

    assertEquals(expected.getStartDate(), actual.getStartDate());
    assertEquals(expected.getEndDate(), actual.getEndDate());
  }

  // i: ------------AB-----------
  // i: ----------CD-------------
  // i: ------------T------------
  // o: ----------C__B-----------
  @Test
  void
      getMergedIntervalContainingDay_twoContinuousButNotOverlappingIntervalsAndOneHavingStartDateEqualingTheGivenDate() {
    LocalDate A = T;
    LocalDate B = T.plusDays(1);
    LocalDate C = T.plusDays(-2);
    LocalDate D = T.plusDays(-1);

    List<LocalDateInterval> intervals = new ArrayList<>();
    intervals.add(new LocalDateInterval(A, B));
    intervals.add(new LocalDateInterval(C, D));

    LocalDateInterval actual = LocalDateHelper.getMergedIntervalContainingDay(intervals, T);
    LocalDateInterval expected = new LocalDateInterval(C, B);

    assertEquals(expected.getStartDate(), actual.getStartDate());
    assertEquals(expected.getEndDate(), actual.getEndDate());
  }

  // i: ------------AB-----------
  // i: ----------CD-------------
  // i: ----E_____F--------------
  // i: --------------G__H-------
  // i: -------------------I__J--
  // i: __L----------------------
  // i: ------------T------------
  // o: ----E____________H-------
  @Test
  void
      getMergedIntervalContainingDay_complexExampleWithDifferentTypesOfCollectionOfIntervalsAndOneContainingTheDate() {
    this.runComplexExampleWithManyIntervalsAndOneContainingTheDate(new ArrayList<>());
    this.runComplexExampleWithManyIntervalsAndOneContainingTheDate(new HashSet<>());
    this.runComplexExampleWithManyIntervalsAndOneContainingTheDate(new TreeSet<>());
    this.runComplexExampleWithManyIntervalsAndOneContainingTheDate(new LinkedList<>());
    this.runComplexExampleWithManyIntervalsAndOneContainingTheDate(new PriorityQueue<>());
    this.runComplexExampleWithManyIntervalsAndOneContainingTheDate(new ArrayDeque<>());
  }

  protected void runComplexExampleWithManyIntervalsAndOneContainingTheDate(
      Collection<LocalDateInterval> instanciedCollection) {
    LocalDate A = T;
    LocalDate B = T.plusDays(1);
    LocalDate C = T.plusDays(-2);
    LocalDate D = T.plusDays(-1);
    LocalDate E = T.plusDays(-8);
    LocalDate F = T.plusDays(-2);
    LocalDate G = T.plusDays(2);
    LocalDate H = T.plusDays(5);
    LocalDate I = T.plusDays(7);
    LocalDate J = T.plusDays(10);
    LocalDate K = null;
    LocalDate L = T.plusDays(-10);

    instanciedCollection.add(new LocalDateInterval(A, B));
    instanciedCollection.add(new LocalDateInterval(C, D));
    instanciedCollection.add(new LocalDateInterval(E, F));
    instanciedCollection.add(new LocalDateInterval(G, H));
    instanciedCollection.add(new LocalDateInterval(I, J));
    instanciedCollection.add(new LocalDateInterval(K, L));

    LocalDateInterval actual =
        LocalDateHelper.getMergedIntervalContainingDay(instanciedCollection, T);
    LocalDateInterval expected = new LocalDateInterval(E, H);

    assertEquals(expected.getStartDate(), actual.getStartDate());
    assertEquals(expected.getEndDate(), actual.getEndDate());
  }

  // i: ------------AB-----------
  // i: ----------CD-------------
  // i: ----E_____F--------------
  // i: --------------G__H-------
  // i: -------------------I__J--
  // i: __L----------------------
  // i: _________________________
  // i: ------------T------------
  // o: _________________________
  @Test
  void getMergedIntervalContainingDay_manyIntervalsAndOneInfiniteOne() {
    LocalDate A = T;
    LocalDate B = T.plusDays(1);
    LocalDate C = T.plusDays(-2);
    LocalDate D = T.plusDays(-1);
    LocalDate E = T.plusDays(-8);
    LocalDate F = T.plusDays(-2);
    LocalDate G = T.plusDays(2);
    LocalDate H = T.plusDays(5);
    LocalDate I = T.plusDays(7);
    LocalDate J = T.plusDays(10);
    LocalDate K = null;
    LocalDate L = T.plusDays(-10);
    LocalDate M = null;
    LocalDate N = null;

    List<LocalDateInterval> intervals = new ArrayList<>();
    intervals.add(new LocalDateInterval(A, B));
    intervals.add(new LocalDateInterval(C, D));
    intervals.add(new LocalDateInterval(E, F));
    intervals.add(new LocalDateInterval(G, H));
    intervals.add(new LocalDateInterval(I, J));
    intervals.add(new LocalDateInterval(K, L));
    intervals.add(new LocalDateInterval(M, N));

    LocalDateInterval actual = LocalDateHelper.getMergedIntervalContainingDay(intervals, T);
    LocalDateInterval expected = new LocalDateInterval(null, null);

    assertEquals(expected.getStartDate(), actual.getStartDate());
    assertEquals(expected.getEndDate(), actual.getEndDate());
  }

  // i: intervals = null || emptyList
  // i: ------------T------------
  // o: null
  @Test
  void getMergedIntervalContainingDay_nullGivenIntervalCollection() {

    LocalDateInterval actual = LocalDateHelper.getMergedIntervalContainingDay(null, T);
    LocalDateInterval expected = null;

    assertEquals(actual, expected);
  }

  // i: intervals = null || emptyList
  // i: ------------T------------
  // o: null
  @Test
  void getMergedIntervalContainingDay_emptyGivenIntervalCollection() {

    LocalDateInterval actual = LocalDateHelper.getMergedIntervalContainingDay(null, T);
    LocalDateInterval expected = null;

    assertEquals(actual, expected);
  }

  // i: -----------A_B-----------
  // i: T = null
  // o: IllegalArgumentException
  @Test()
  void getMergedIntervalContainingDay_nullGivenDate() {

    LocalDate A = T.plusDays(-1);
    LocalDate B = T.plusDays(1);

    List<LocalDateInterval> intervals = new ArrayList<>();
    intervals.add(new LocalDateInterval(A, B));

    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> LocalDateHelper.getMergedIntervalContainingDay(intervals, null));
  }
}
