package com.axelor.utils.date;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DateToolGetMergedIntervalContainingDayTest {

  private LocalDate T = LocalDate.of(2021, 10, 5);

  // i: --------A________B-------
  // i: ------------T------------
  // i: ------C___D--------------
  // o: ------C__________B-------
  @Test
  public void test01() {
    LocalDate A = T.plusDays(-4);
    LocalDate B = T.plusDays(5);
    LocalDate C = T.plusDays(-6);
    LocalDate D = T.plusDays(-2);

    List<DatesInterval> intervals = new ArrayList<>();
    intervals.add(new DatesInterval(A, B));
    intervals.add(new DatesInterval(C, D));

    DatesInterval actual = DateTool.getMergedIntervalContainingDay(intervals, T);
    DatesInterval expected = new DatesInterval(C, B);

    assertEquals(expected.getStartDate(), actual.getStartDate());
    assertEquals(expected.getEndDate(), actual.getEndDate());
  }

  // i: --------A________B-------
  // i: ------------T------------
  // i: ----------C________D-----
  // o: --------A__________D-----
  @Test
  public void test02() {
    LocalDate A = T.plusDays(-4);
    LocalDate B = T.plusDays(5);
    LocalDate C = T.plusDays(-2);
    LocalDate D = T.plusDays(7);

    List<DatesInterval> intervals = new ArrayList<>();
    intervals.add(new DatesInterval(A, B));
    intervals.add(new DatesInterval(C, D));

    DatesInterval actual = DateTool.getMergedIntervalContainingDay(intervals, T);
    DatesInterval expected = new DatesInterval(A, D);

    assertEquals(expected.getStartDate(), actual.getStartDate());
    assertEquals(expected.getEndDate(), actual.getEndDate());
  }

  // i: --------A________B-------
  // i: ------------T------------
  // i: --------C________D-------
  // o: --------A________B-------
  @Test
  public void test03() {
    LocalDate A = T.plusDays(-4);
    LocalDate B = T.plusDays(5);
    LocalDate C = A;
    LocalDate D = B;

    List<DatesInterval> intervals = new ArrayList<>();
    intervals.add(new DatesInterval(A, B));
    intervals.add(new DatesInterval(C, D));

    DatesInterval actual = DateTool.getMergedIntervalContainingDay(intervals, T);
    DatesInterval expected = new DatesInterval(A, B);

    assertEquals(expected.getStartDate(), actual.getStartDate());
    assertEquals(expected.getEndDate(), actual.getEndDate());
  }

  // i: --------A________B-------
  // i: ------------T------------
  // i: ----------C____D---------
  // o: --------A________B-------
  @Test
  public void test04() {
    LocalDate A = T.plusDays(-4);
    LocalDate B = T.plusDays(5);
    LocalDate C = T.plusDays(-2);
    LocalDate D = T.plusDays(3);

    List<DatesInterval> intervals = new ArrayList<>();
    intervals.add(new DatesInterval(A, B));
    intervals.add(new DatesInterval(C, D));

    DatesInterval actual = DateTool.getMergedIntervalContainingDay(intervals, T);
    DatesInterval expected = new DatesInterval(A, B);

    assertEquals(expected.getStartDate(), actual.getStartDate());
    assertEquals(expected.getEndDate(), actual.getEndDate());
  }

  // i: --------A________B-------
  // i: ------------T------------
  // i: ------CD-----------------
  // o: ------C__________B-------
  @Test
  public void test05() {
    LocalDate A = T.plusDays(-4);
    LocalDate B = T.plusDays(5);
    LocalDate C = T.plusDays(-6);
    LocalDate D = T.plusDays(-5);

    List<DatesInterval> intervals = new ArrayList<>();
    intervals.add(new DatesInterval(A, B));
    intervals.add(new DatesInterval(C, D));

    DatesInterval actual = DateTool.getMergedIntervalContainingDay(intervals, T);
    DatesInterval expected = new DatesInterval(C, B);

    assertEquals(expected.getStartDate(), actual.getStartDate());
    assertEquals(expected.getEndDate(), actual.getEndDate());
  }

  // i: --------A________B-------
  // i: ------------T------------
  // i: -------------------CD----
  // o: --------A________B-------
  @Test
  public void test06() {
    LocalDate A = T.plusDays(-4);
    LocalDate B = T.plusDays(5);
    LocalDate C = T.plusDays(7);
    LocalDate D = T.plusDays(8);

    List<DatesInterval> intervals = new ArrayList<>();
    intervals.add(new DatesInterval(A, B));
    intervals.add(new DatesInterval(C, D));

    DatesInterval actual = DateTool.getMergedIntervalContainingDay(intervals, T);
    DatesInterval expected = new DatesInterval(A, B);

    assertEquals(expected.getStartDate(), actual.getStartDate());
    assertEquals(expected.getEndDate(), actual.getEndDate());
  }

  // i: --------A________________
  // i: ------------T------------
  // i: ---C__D------------------
  // o: --------A________________
  @Test
  public void test07() {
    LocalDate A = T.plusDays(-4);
    LocalDate B = null;
    LocalDate C = T.plusDays(-9);
    LocalDate D = T.plusDays(-6);

    List<DatesInterval> intervals = new ArrayList<>();
    intervals.add(new DatesInterval(A, B));
    intervals.add(new DatesInterval(C, D));

    DatesInterval actual = DateTool.getMergedIntervalContainingDay(intervals, T);
    DatesInterval expected = new DatesInterval(A, null);

    assertEquals(expected.getStartDate(), actual.getStartDate());
    assertEquals(expected.getEndDate(), actual.getEndDate());
  }

  // i: --------A________________
  // i: ------------T------------
  // i: ----------C__D-----------
  // o: --------A________________
  @Test
  public void test08() {
    LocalDate A = T.plusDays(-4);
    LocalDate B = null;
    LocalDate C = T.plusDays(-2);
    LocalDate D = T.plusDays(1);

    List<DatesInterval> intervals = new ArrayList<>();
    intervals.add(new DatesInterval(A, B));
    intervals.add(new DatesInterval(C, D));

    DatesInterval actual = DateTool.getMergedIntervalContainingDay(intervals, T);
    DatesInterval expected = new DatesInterval(A, null);

    assertEquals(expected.getStartDate(), actual.getStartDate());
    assertEquals(expected.getEndDate(), actual.getEndDate());
  }

  // i: --------A________B-------
  // i: ------------T------------
  // i: --------C________________
  // o: --------A________________
  @Test
  public void test09() {
    LocalDate A = T.plusDays(-4);
    LocalDate B = T.plusDays(5);
    LocalDate C = A;
    LocalDate D = null;

    List<DatesInterval> intervals = new ArrayList<>();
    intervals.add(new DatesInterval(A, B));
    intervals.add(new DatesInterval(C, D));

    DatesInterval actual = DateTool.getMergedIntervalContainingDay(intervals, T);
    DatesInterval expected = new DatesInterval(A, null);

    assertEquals(expected.getStartDate(), actual.getStartDate());
    assertEquals(expected.getEndDate(), actual.getEndDate());
  }

  // i: --------A________________
  // i: ------------T------------
  // i: --------C________________
  // o: --------A________________
  @Test
  public void test10() {
    LocalDate A = T.plusDays(-4);
    LocalDate B = null;
    LocalDate C = A;
    LocalDate D = null;

    List<DatesInterval> intervals = new ArrayList<>();
    intervals.add(new DatesInterval(A, B));
    intervals.add(new DatesInterval(C, D));

    DatesInterval actual = DateTool.getMergedIntervalContainingDay(intervals, T);
    DatesInterval expected = new DatesInterval(A, null);

    assertEquals(expected.getStartDate(), actual.getStartDate());
    assertEquals(expected.getEndDate(), actual.getEndDate());
  }

  // i: --------A----------------
  // i: ------------T------------
  // i: --------C----------------
  // o: -------------------------
  @Test
  public void test11() {
    LocalDate A = T.plusDays(-4);
    LocalDate B = A;
    LocalDate C = A;
    LocalDate D = A;

    List<DatesInterval> intervals = new ArrayList<>();
    intervals.add(new DatesInterval(A, B));
    intervals.add(new DatesInterval(C, D));

    DatesInterval actual = DateTool.getMergedIntervalContainingDay(intervals, T);
    DatesInterval expected = new DatesInterval(null, null);

    assertEquals(expected.getStartDate(), actual.getStartDate());
    assertEquals(expected.getEndDate(), actual.getEndDate());
  }

  // i: -----A__B----------------
  // i: ------------T------------
  // i: ----------------C__D-----
  // o: -------------------------
  @Test
  public void test12() {
    LocalDate A = T.plusDays(-8);
    LocalDate B = T.plusDays(-4);
    LocalDate C = T.plusDays(4);
    LocalDate D = T.plusDays(8);

    List<DatesInterval> intervals = new ArrayList<>();
    intervals.add(new DatesInterval(A, B));
    intervals.add(new DatesInterval(C, D));

    DatesInterval actual = DateTool.getMergedIntervalContainingDay(intervals, T);
    DatesInterval expected = new DatesInterval(null, null);

    assertEquals(expected.getStartDate(), actual.getStartDate());
    assertEquals(expected.getEndDate(), actual.getEndDate());
  }

  // i: --------A________________
  // i: ------------T------------
  // i: ----C_______________D----
  // o: ----C____________________
  @Test
  public void test13() {
    LocalDate A = T.plusDays(-4);
    LocalDate B = null;
    LocalDate C = T.plusDays(-8);
    LocalDate D = T.plusDays(8);

    List<DatesInterval> intervals = new ArrayList<>();
    intervals.add(new DatesInterval(A, B));
    intervals.add(new DatesInterval(C, D));

    DatesInterval actual = DateTool.getMergedIntervalContainingDay(intervals, T);
    DatesInterval expected = new DatesInterval(C, null);

    assertEquals(expected.getStartDate(), actual.getStartDate());
    assertEquals(expected.getEndDate(), actual.getEndDate());
  }

  // i: _________B---------------
  // i: ------------T------------
  // i: ----------C______________
  // o: _________________________
  @Test
  public void test14() {
    LocalDate A = null;
    LocalDate B = T.plusDays(-3);
    LocalDate C = T.plusDays(-2);
    LocalDate D = null;

    List<DatesInterval> intervals = new ArrayList<>();
    intervals.add(new DatesInterval(A, B));
    intervals.add(new DatesInterval(C, D));

    DatesInterval actual = DateTool.getMergedIntervalContainingDay(intervals, T);
    DatesInterval expected = new DatesInterval(null, null);

    assertEquals(expected.getStartDate(), actual.getStartDate());
    assertEquals(expected.getEndDate(), actual.getEndDate());
  }

  // i: ------------AB-----------
  // i: ------------T------------
  // i: ----------CD-------------
  // o: ----------C__B-----------
  @Test
  public void test15() {
    LocalDate A = T;
    LocalDate B = T.plusDays(1);
    LocalDate C = T.plusDays(-2);
    LocalDate D = T.plusDays(-1);

    List<DatesInterval> intervals = new ArrayList<>();
    intervals.add(new DatesInterval(A, B));
    intervals.add(new DatesInterval(C, D));

    DatesInterval actual = DateTool.getMergedIntervalContainingDay(intervals, T);
    DatesInterval expected = new DatesInterval(C, B);

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
  public void test16() {
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

    List<DatesInterval> intervals = new ArrayList<>();
    intervals.add(new DatesInterval(A, B));
    intervals.add(new DatesInterval(C, D));
    intervals.add(new DatesInterval(E, F));
    intervals.add(new DatesInterval(G, H));
    intervals.add(new DatesInterval(I, J));
    intervals.add(new DatesInterval(K, L));

    DatesInterval actual = DateTool.getMergedIntervalContainingDay(intervals, T);
    DatesInterval expected = new DatesInterval(E, H);

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
  // i: _________________________
  // o: _________________________
  @Test
  public void test17() {
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

    List<DatesInterval> intervals = new ArrayList<>();
    intervals.add(new DatesInterval(A, B));
    intervals.add(new DatesInterval(C, D));
    intervals.add(new DatesInterval(E, F));
    intervals.add(new DatesInterval(G, H));
    intervals.add(new DatesInterval(I, J));
    intervals.add(new DatesInterval(K, L));
    intervals.add(new DatesInterval(M, N));

    DatesInterval actual = DateTool.getMergedIntervalContainingDay(intervals, T);
    DatesInterval expected = new DatesInterval(null, null);

    assertEquals(expected.getStartDate(), actual.getStartDate());
    assertEquals(expected.getEndDate(), actual.getEndDate());
  }

  // i: intervals = null || emptyList
  // i: ------------T------------
  // o: null
  @Test
  public void test18() {

    DatesInterval fromNull = DateTool.getMergedIntervalContainingDay(null, T);
    DatesInterval fromEmptyList =
        DateTool.getMergedIntervalContainingDay(Collections.emptyList(), T);

    assertEquals(null, fromNull);
    assertEquals(null, fromEmptyList);
  }

  // i: -----------A_B-----------
  // i: T = null
  // o: IllegalArgumentException
  @Test()
  public void test19() {

    LocalDate A = T.plusDays(-1);
    LocalDate B = T.plusDays(1);

    List<DatesInterval> intervals = new ArrayList<>();
    intervals.add(new DatesInterval(A, B));

    Assertions.assertThrows(
        IllegalArgumentException.class, () -> DateTool.getMergedIntervalContainingDay(intervals, null));
  }
}
