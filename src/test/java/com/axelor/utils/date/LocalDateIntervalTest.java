package com.axelor.utils.date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class LocalDateIntervalTest {

  private LocalDate T = LocalDate.of(2000, 1, 1);

  // i: ------------T------------
  // i: --------A________B------- (AB)
  // i: ________D---------------- (CD)
  // i: ------------E____________ (EF)
  // i: ----------G-------------- (GH)
  // i: _________________________ (IJ)
  @Test
  void test_equals() {

    LocalDate A = T.plusDays(-4);
    LocalDate B = T.plusDays(5);
    LocalDate C = null;
    LocalDate D = T.plusDays(-4);
    LocalDate E = T;
    LocalDate F = null;
    LocalDate G = T.plusDays(-2);
    LocalDate H = T.plusDays(-2);
    LocalDate I = null;
    LocalDate J = null;

    LocalDateInterval AB = new LocalDateInterval(A, B);
    LocalDateInterval CD = new LocalDateInterval(C, D);
    LocalDateInterval EF = new LocalDateInterval(E, F);
    LocalDateInterval GH = new LocalDateInterval(G, H);
    LocalDateInterval IJ = new LocalDateInterval(I, J);

    LocalDateInterval AB2 = new LocalDateInterval(A, B);
    LocalDateInterval CD2 = new LocalDateInterval(C, D);
    LocalDateInterval EF2 = new LocalDateInterval(E, F);
    LocalDateInterval GH2 = new LocalDateInterval(G, H);
    LocalDateInterval IJ2 = new LocalDateInterval(I, J);

    assertEquals(AB, AB);
    assertEquals(CD, CD);
    assertEquals(EF, EF);
    assertEquals(GH, GH);
    assertEquals(IJ, IJ);

    assertEquals(AB, AB2);
    assertEquals(CD, CD2);
    assertEquals(EF, EF2);
    assertEquals(GH, GH2);
    assertEquals(IJ, IJ2);

    assertNotEquals(AB, CD);
    assertNotEquals(AB, EF);
    assertNotEquals(AB, GH);
    assertNotEquals(AB, IJ);

    assertNotEquals(CD, AB);
    assertNotEquals(CD, EF);
    assertNotEquals(CD, GH);
    assertNotEquals(CD, IJ);

    assertNotEquals(EF, AB);
    assertNotEquals(EF, CD);
    assertNotEquals(EF, GH);
    assertNotEquals(EF, IJ);

    assertNotEquals(GH, AB);
    assertNotEquals(GH, CD);
    assertNotEquals(GH, EF);
    assertNotEquals(GH, IJ);

    assertNotEquals(IJ, AB);
    assertNotEquals(IJ, CD);
    assertNotEquals(IJ, EF);
    assertNotEquals(IJ, GH);
  }

  // i: --------A________B-------
  // i: ------C___D--------------
  // i: ------------T------------
  @Test
  void test01() {
    LocalDate A = T.plusDays(-4);
    LocalDate B = T.plusDays(5);
    LocalDate C = T.plusDays(-6);
    LocalDate D = T.plusDays(-2);

    LocalDateInterval AB = new LocalDateInterval(A, B);
    LocalDateInterval CD = new LocalDateInterval(C, D);

    assertTrue(AB.compareTo(CD) > 0);
    assertTrue(CD.compareTo(AB) < 0);

    assertTrue(AB.contains(T));
    assertTrue(AB.startsBeforeOrAt(T));
    assertTrue(AB.startsBefore(T));
    assertTrue(AB.endsAfterOrAt(T));
    assertTrue(AB.endsAfter(T));

    assertFalse(CD.contains(T));
    assertTrue(CD.startsBeforeOrAt(T));
    assertTrue(CD.startsBefore(T));
    assertFalse(CD.endsAfterOrAt(T));
    assertFalse(CD.endsAfter(T));

    assertFalse(AB.startsBefore(AB));
    assertFalse(AB.startsBefore(CD));
    assertTrue(CD.startsBefore(AB));

    assertFalse(AB.startsAfter(AB));
    assertTrue(AB.startsAfter(CD));
    assertFalse(CD.startsAfter(AB));

    assertFalse(AB.endsBefore(AB));
    assertFalse(AB.endsBefore(CD));
    assertTrue(CD.endsBefore(AB));

    assertFalse(AB.endsAfter(AB));
    assertTrue(AB.endsAfter(CD));
    assertFalse(CD.endsAfter(AB));

    assertTrue(AB.overlapsOrIsContinuousWith(CD));
    assertTrue(CD.overlapsOrIsContinuousWith(AB));

    assertTrue(AB.isContinuousAtStartWith(CD));
    assertTrue(CD.isContinuousAtEndWith(AB));
  }

  // i: --------A________________
  // i: ____________D------------
  // i: ------------T------------
  @Test
  void test02() {
    LocalDate A = T.plusDays(-4);
    LocalDate B = null;
    LocalDate C = null;
    LocalDate D = T;

    LocalDateInterval AB = new LocalDateInterval(A, B);
    LocalDateInterval CD = new LocalDateInterval(C, D);

    assertTrue(AB.compareTo(CD) > 0);
    assertTrue(CD.compareTo(AB) < 0);

    assertTrue(AB.contains(T));
    assertTrue(AB.startsBeforeOrAt(T));
    assertTrue(AB.startsBefore(T));
    assertTrue(AB.endsAfterOrAt(T));
    assertTrue(AB.endsAfter(T));

    assertTrue(CD.contains(T));
    assertTrue(CD.startsBeforeOrAt(T));
    assertTrue(CD.startsBefore(T));
    assertTrue(CD.endsAfterOrAt(T));
    assertFalse(CD.endsAfter(T));

    assertFalse(AB.startsBefore(AB));
    assertFalse(AB.startsBefore(CD));
    assertTrue(CD.startsBefore(AB));

    assertFalse(AB.startsAfter(AB));
    assertTrue(AB.startsAfter(CD));
    assertFalse(CD.startsAfter(AB));

    assertFalse(AB.endsBefore(AB));
    assertFalse(AB.endsBefore(CD));
    assertTrue(CD.endsBefore(AB));

    assertFalse(AB.endsAfter(AB));
    assertTrue(AB.endsAfter(CD));
    assertFalse(CD.endsAfter(AB));

    assertTrue(AB.overlapsOrIsContinuousWith(CD));
    assertTrue(CD.overlapsOrIsContinuousWith(AB));

    assertTrue(AB.isContinuousAtStartWith(CD));
    assertTrue(CD.isContinuousAtEndWith(AB));
  }

  // i: --------A_B--------------
  // i: _______D-----------------
  // i: ------------T------------
  @Test
  void test03() {
    LocalDate A = T.plusDays(-4);
    LocalDate B = T.plusDays(-2);
    LocalDate C = null;
    LocalDate D = T.plusDays(-5);

    LocalDateInterval AB = new LocalDateInterval(A, B);
    LocalDateInterval CD = new LocalDateInterval(C, D);

    assertTrue(AB.compareTo(CD) > 0);
    assertTrue(CD.compareTo(AB) < 0);

    assertFalse(AB.contains(T));
    assertTrue(AB.startsBeforeOrAt(T));
    assertTrue(AB.startsBefore(T));
    assertFalse(AB.endsAfterOrAt(T));
    assertFalse(AB.endsAfter(T));

    assertFalse(CD.contains(T));
    assertTrue(CD.startsBeforeOrAt(T));
    assertTrue(CD.startsBefore(T));
    assertFalse(CD.endsAfterOrAt(T));
    assertFalse(CD.endsAfter(T));

    assertFalse(AB.startsBefore(AB));
    assertFalse(AB.startsBefore(CD));
    assertTrue(CD.startsBefore(AB));

    assertFalse(AB.startsAfter(AB));
    assertTrue(AB.startsAfter(CD));
    assertFalse(CD.startsAfter(AB));

    assertFalse(AB.endsBefore(AB));
    assertFalse(AB.endsBefore(CD));
    assertTrue(CD.endsBefore(AB));

    assertFalse(AB.endsAfter(AB));
    assertTrue(AB.endsAfter(CD));
    assertFalse(CD.endsAfter(AB));

    assertTrue(AB.overlapsOrIsContinuousWith(CD));
    assertTrue(CD.overlapsOrIsContinuousWith(AB));

    assertTrue(AB.isContinuousAtStartWith(CD));
    assertTrue(CD.isContinuousAtEndWith(AB));
  }

  // i: ------------A____________
  // i: ------------C------------
  // i: ------------T------------
  @Test
  void test04() {
    LocalDate A = T;
    LocalDate B = null;
    LocalDate C = T;
    LocalDate D = C;

    LocalDateInterval AB = new LocalDateInterval(A, B);
    LocalDateInterval CD = new LocalDateInterval(C, D);

    assertEquals(0, AB.compareTo(CD));
    assertEquals(0, CD.compareTo(AB));

    assertTrue(AB.contains(T));
    assertTrue(AB.startsBeforeOrAt(T));
    assertFalse(AB.startsBefore(T));
    assertTrue(AB.endsAfterOrAt(T));
    assertTrue(AB.endsAfter(T));

    assertTrue(CD.contains(T));
    assertTrue(CD.startsBeforeOrAt(T));
    assertFalse(CD.startsBefore(T));
    assertTrue(CD.endsAfterOrAt(T));
    assertFalse(CD.endsAfter(T));

    assertFalse(AB.startsBefore(AB));
    assertFalse(AB.startsBefore(CD));
    assertFalse(CD.startsBefore(AB));

    assertFalse(AB.startsAfter(AB));
    assertFalse(AB.startsAfter(CD));
    assertFalse(CD.startsAfter(AB));

    assertFalse(AB.endsBefore(AB));
    assertFalse(AB.endsBefore(CD));
    assertTrue(CD.endsBefore(AB));

    assertFalse(AB.endsAfter(AB));
    assertTrue(AB.endsAfter(CD));
    assertFalse(CD.endsAfter(AB));

    assertTrue(AB.overlapsOrIsContinuousWith(CD));
    assertTrue(CD.overlapsOrIsContinuousWith(AB));

    assertTrue(AB.isContinuousAtStartWith(CD));
    assertTrue(CD.isContinuousAtEndWith(AB));
  }

  // i: -----------------A_______
  // i: ------------------C______
  // i: ------------T------------
  @Test
  void test05() {
    LocalDate A = T.plusDays(5);
    LocalDate B = null;
    LocalDate C = T.plusDays(6);
    LocalDate D = null;

    LocalDateInterval AB = new LocalDateInterval(A, B);
    LocalDateInterval CD = new LocalDateInterval(C, D);

    assertTrue(AB.compareTo(CD) < 0);
    assertTrue(CD.compareTo(AB) > 0);

    assertFalse(AB.contains(T));
    assertFalse(AB.startsBeforeOrAt(T));
    assertFalse(AB.startsBefore(T));
    assertTrue(AB.endsAfterOrAt(T));
    assertTrue(AB.endsAfter(T));

    assertFalse(CD.contains(T));
    assertFalse(CD.startsBeforeOrAt(T));
    assertFalse(CD.startsBefore(T));
    assertTrue(CD.endsAfterOrAt(T));
    assertTrue(CD.endsAfter(T));

    assertFalse(AB.startsBefore(AB));
    assertTrue(AB.startsBefore(CD));
    assertFalse(CD.startsBefore(AB));

    assertFalse(AB.startsAfter(AB));
    assertFalse(AB.startsAfter(CD));
    assertTrue(CD.startsAfter(AB));

    assertFalse(AB.endsBefore(AB));
    assertFalse(AB.endsBefore(CD));
    assertFalse(CD.endsBefore(AB));

    assertFalse(AB.endsAfter(AB));
    assertFalse(AB.endsAfter(CD));
    assertFalse(CD.endsAfter(AB));

    assertTrue(AB.overlapsOrIsContinuousWith(CD));
    assertTrue(CD.overlapsOrIsContinuousWith(AB));

    assertTrue(AB.isContinuousAtEndWith(CD));
    assertTrue(CD.isContinuousAtStartWith(CD));
  }

  // i: -------------A___________
  // i: ___________D-------------
  // i: ------------T------------
  @Test
  void test06() {
    LocalDate A = T.plusDays(1);
    LocalDate B = null;
    LocalDate C = null;
    LocalDate D = T.plusDays(-1);

    LocalDateInterval AB = new LocalDateInterval(A, B);
    LocalDateInterval CD = new LocalDateInterval(C, D);

    assertTrue(AB.compareTo(CD) > 0);
    assertTrue(CD.compareTo(AB) < 0);

    assertFalse(AB.contains(T));
    assertFalse(AB.startsBeforeOrAt(T));
    assertFalse(AB.startsBefore(T));
    assertTrue(AB.endsAfterOrAt(T));
    assertTrue(AB.endsAfter(T));

    assertFalse(CD.contains(T));
    assertTrue(CD.startsBeforeOrAt(T));
    assertTrue(CD.startsBefore(T));
    assertFalse(CD.endsAfterOrAt(T));
    assertFalse(CD.endsAfter(T));

    assertFalse(AB.startsBefore(AB));
    assertFalse(AB.startsBefore(CD));
    assertTrue(CD.startsBefore(AB));

    assertFalse(AB.startsAfter(AB));
    assertTrue(AB.startsAfter(CD));
    assertFalse(CD.startsAfter(AB));

    assertFalse(AB.endsBefore(AB));
    assertFalse(AB.endsBefore(CD));
    assertTrue(CD.endsBefore(AB));

    assertFalse(AB.endsAfter(AB));
    assertTrue(AB.endsAfter(CD));
    assertFalse(CD.endsAfter(AB));

    assertFalse(AB.overlapsOrIsContinuousWith(CD));
    assertFalse(CD.overlapsOrIsContinuousWith(AB));

    assertFalse(AB.isContinuousAtStartWith(CD));
    assertFalse(CD.isContinuousAtEndWith(AB));
  }
}
