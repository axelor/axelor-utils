package com.axelor.utils.date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;

class LocalDateTimeIntervalTest {
  //  On this class the tests names does not match the convention for clarity purposes
  // (because words makes unclear the tested cases).
  // Instead, the test cases are represented with comments above each test method and the method
  // name only refer to which method is tested.

  private LocalDateTime T = LocalDate.of(2000, 1, 1).atTime(12, 12);

  // i: ------------T------------
  // i: --------A________B------- (AB)
  // i: ________D---------------- (CD)
  // i: ------------E____________ (EF)
  // i: ----------G-------------- (GH)
  // i: _________________________ (IJ)
  @Test
  void equals_LocalDateTimeInterval() {

    LocalDateTime A = T.plusDays(-4);
    LocalDateTime B = T.plusDays(5);
    LocalDateTime C = null;
    LocalDateTime D = T.plusDays(-4);
    LocalDateTime E = T;
    LocalDateTime F = null;
    LocalDateTime G = T.plusDays(-2);
    LocalDateTime H = T.plusDays(-2);
    LocalDateTime I = null;
    LocalDateTime J = null;

    LocalDateTimeInterval AB = new LocalDateTimeInterval(A, B);
    LocalDateTimeInterval CD = new LocalDateTimeInterval(C, D);
    LocalDateTimeInterval EF = new LocalDateTimeInterval(E, F);
    LocalDateTimeInterval GH = new LocalDateTimeInterval(G, H);
    LocalDateTimeInterval IJ = new LocalDateTimeInterval(I, J);

    LocalDateTimeInterval AB2 = new LocalDateTimeInterval(A, B);
    LocalDateTimeInterval CD2 = new LocalDateTimeInterval(C, D);
    LocalDateTimeInterval EF2 = new LocalDateTimeInterval(E, F);
    LocalDateTimeInterval GH2 = new LocalDateTimeInterval(G, H);
    LocalDateTimeInterval IJ2 = new LocalDateTimeInterval(I, J);

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
  void compareTo_contains_startsOrEndBeforeOrAfter_testCase01() {
    LocalDateTime A = T.plusDays(-4);
    LocalDateTime B = T.plusDays(5);
    LocalDateTime C = T.plusDays(-6);
    LocalDateTime D = T.plusDays(-2);

    LocalDateTimeInterval AB = new LocalDateTimeInterval(A, B);
    LocalDateTimeInterval CD = new LocalDateTimeInterval(C, D);

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
  }

  // i: --------A________________
  // i: ____________D------------
  // i: ------------T------------
  @Test
  void compareTo_contains_startsOrEndBeforeOrAfter_testCase02() {
    LocalDateTime A = T.plusDays(-4);
    LocalDateTime B = null;
    LocalDateTime C = null;
    LocalDateTime D = T;

    LocalDateTimeInterval AB = new LocalDateTimeInterval(A, B);
    LocalDateTimeInterval CD = new LocalDateTimeInterval(C, D);

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
  }

  // i: --------A_B--------------
  // i: _______D-----------------
  // i: ------------T------------
  @Test
  void compareTo_contains_startsOrEndBeforeOrAfter_testCase03() {
    LocalDateTime A = T.plusDays(-4);
    LocalDateTime B = T.plusDays(-2);
    LocalDateTime C = null;
    LocalDateTime D = T.plusDays(-5);

    LocalDateTimeInterval AB = new LocalDateTimeInterval(A, B);
    LocalDateTimeInterval CD = new LocalDateTimeInterval(C, D);

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
  }

  // i: ------------A____________
  // i: ------------C------------
  // i: ------------T------------
  @Test
  void compareTo_contains_startsOrEndBeforeOrAfter_testCase04() {
    LocalDateTime A = T;
    LocalDateTime B = null;
    LocalDateTime C = T;
    LocalDateTime D = C;

    LocalDateTimeInterval AB = new LocalDateTimeInterval(A, B);
    LocalDateTimeInterval CD = new LocalDateTimeInterval(C, D);

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
  }

  // i: -----------------A_______
  // i: ------------------C______
  // i: ------------T------------
  @Test
  void compareTo_contains_startsOrEndBeforeOrAfter_testCase05() {
    LocalDateTime A = T.plusDays(5);
    LocalDateTime B = null;
    LocalDateTime C = T.plusDays(6);
    LocalDateTime D = null;

    LocalDateTimeInterval AB = new LocalDateTimeInterval(A, B);
    LocalDateTimeInterval CD = new LocalDateTimeInterval(C, D);

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
  }

  // i: -------------A___________
  // i: ___________D-------------
  // i: ------------T------------
  @Test
  void compareTo_contains_startsOrEndBeforeOrAfter_testCase06() {
    LocalDateTime A = T.plusDays(1);
    LocalDateTime B = null;
    LocalDateTime C = null;
    LocalDateTime D = T.plusDays(-1);

    LocalDateTimeInterval AB = new LocalDateTimeInterval(A, B);
    LocalDateTimeInterval CD = new LocalDateTimeInterval(C, D);

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
  }

  // i: ------------------A______
  // i: _______D-----------------
  @Test
  void overlapsOrIsContinuousWith_isContinuousAtStart_isContinuousAtEnd_test01() {
    LocalDateTime A = LocalDateTime.now();
    LocalDateTime B = null;
    LocalDateTime C = null;
    LocalDateTime D = A.minusDays(1);

    LocalDateTimeInterval AB = new LocalDateTimeInterval(A, B);
    LocalDateTimeInterval CD = new LocalDateTimeInterval(C, D);

    // 0 tolerance (the chronoUnit does not matter)
    AB = new LocalDateTimeInterval(A, B, ChronoUnit.FOREVER, 0);
    CD = new LocalDateTimeInterval(C, D, ChronoUnit.NANOS, 0);
    assertFalse(AB.overlapsOrIsContinuousWith(CD));
    assertFalse(CD.overlapsOrIsContinuousWith(AB));

    assertFalse(AB.isContinuousAtStartWith(CD));
    assertFalse(CD.isContinuousAtEndWith(AB));
    assertTrue(
        CD.isContinuousAtStartWith(
            AB)); // see warning in method javadoc, functionally strange to do such call
    assertTrue(
        AB.isContinuousAtEndWith(
            CD)); // see warning in method javadoc, functionally strange to do such call

    // Tolerance way smaller than the gap
    AB = new LocalDateTimeInterval(A, B, ChronoUnit.MINUTES, 10);
    CD = new LocalDateTimeInterval(C, D, ChronoUnit.HOURS, 8);
    assertFalse(AB.overlapsOrIsContinuousWith(CD));
    assertFalse(CD.overlapsOrIsContinuousWith(AB));

    assertFalse(AB.isContinuousAtStartWith(CD));
    assertFalse(CD.isContinuousAtEndWith(AB));
    assertTrue(
        CD.isContinuousAtStartWith(
            AB)); // see warning in method javadoc, functionally strange to do such call
    assertTrue(
        AB.isContinuousAtEndWith(
            CD)); // see warning in method javadoc, functionally strange to do such call

    // Gap slightly larger than the tolerance
    AB = new LocalDateTimeInterval(A.plusNanos(1), B, ChronoUnit.DAYS, 1);
    CD = new LocalDateTimeInterval(C, D, ChronoUnit.DAYS, 1);
    assertFalse(AB.overlapsOrIsContinuousWith(CD));
    assertFalse(CD.overlapsOrIsContinuousWith(AB));

    assertFalse(AB.isContinuousAtStartWith(CD));
    assertFalse(CD.isContinuousAtEndWith(AB));
    assertTrue(
        CD.isContinuousAtStartWith(
            AB)); // see warning in method javadoc, functionally strange to do such call
    assertTrue(
        AB.isContinuousAtEndWith(
            CD)); // see warning in method javadoc, functionally strange to do such call

    // Tolerance equal to the gap
    AB = new LocalDateTimeInterval(A, B, ChronoUnit.DAYS, 1);
    CD = new LocalDateTimeInterval(C, D, ChronoUnit.DAYS, 1);
    assertTrue(AB.overlapsOrIsContinuousWith(CD));
    assertTrue(CD.overlapsOrIsContinuousWith(AB));

    assertTrue(AB.isContinuousAtStartWith(CD));
    assertTrue(CD.isContinuousAtEndWith(AB));
    assertTrue(
        CD.isContinuousAtStartWith(
            AB)); // see warning in method javadoc, functionally strange to do such call
    assertTrue(
        AB.isContinuousAtEndWith(
            CD)); // see warning in method javadoc, functionally strange to do such call

    // Tolerance larger than the gap
    AB = new LocalDateTimeInterval(A, B, ChronoUnit.MONTHS, 1);
    CD = new LocalDateTimeInterval(C, D, ChronoUnit.YEARS, 1);
    assertTrue(AB.overlapsOrIsContinuousWith(CD));
    assertTrue(CD.overlapsOrIsContinuousWith(AB));

    assertTrue(AB.isContinuousAtStartWith(CD));
    assertTrue(CD.isContinuousAtEndWith(AB));
    assertTrue(
        CD.isContinuousAtStartWith(
            AB)); // see warning in method javadoc, functionally strange to do such call
    assertTrue(
        AB.isContinuousAtEndWith(
            CD)); // see warning in method javadoc, functionally strange to do such call
  }

  // i: ------A_____B------------
  // i: ----------C_____D--------
  @Test
  void overlapsOrIsContinuousWith_isContinuousAtStart_isContinuousAtEnd_test02() {
    LocalDateTime A = LocalDateTime.now();
    LocalDateTime B = A.plusHours(5);
    LocalDateTime C = A.plusHours(4);
    LocalDateTime D = A.plusHours(7);

    LocalDateTimeInterval AB = new LocalDateTimeInterval(A, B);
    LocalDateTimeInterval CD = new LocalDateTimeInterval(C, D);

    // 0 tolerance (the chronoUnit does not matter)
    AB = new LocalDateTimeInterval(A, B, ChronoUnit.FOREVER, 0);
    CD = new LocalDateTimeInterval(C, D, ChronoUnit.NANOS, 0);
    assertTrue(AB.overlapsOrIsContinuousWith(CD));
    assertTrue(CD.overlapsOrIsContinuousWith(AB));

    assertTrue(
        AB.isContinuousAtStartWith(
            CD)); // see warning in method javadoc, functionally strange to do such call
    assertTrue(
        CD.isContinuousAtEndWith(
            AB)); // see warning in method javadoc, functionally strange to do such call
    assertTrue(CD.isContinuousAtStartWith(AB));
    assertTrue(AB.isContinuousAtEndWith(CD));

    // tolerance > 0
    AB = new LocalDateTimeInterval(A, B, ChronoUnit.CENTURIES, 2);
    CD = new LocalDateTimeInterval(C, D, ChronoUnit.HOURS, 1);
    assertTrue(AB.overlapsOrIsContinuousWith(CD));
    assertTrue(CD.overlapsOrIsContinuousWith(AB));

    assertTrue(
        AB.isContinuousAtStartWith(
            CD)); // see warning in method javadoc, functionally strange to do such call
    assertTrue(
        CD.isContinuousAtEndWith(
            AB)); // see warning in method javadoc, functionally strange to do such call
    assertTrue(CD.isContinuousAtStartWith(AB));
    assertTrue(AB.isContinuousAtEndWith(CD));
  }

  // i: _________________________
  // i: ----------C_____D--------
  @Test
  void overlapsOrIsContinuousWith_isContinuousAtStart_isContinuousAtEnd_test03() {
    LocalDateTime A = null;
    LocalDateTime B = null;
    LocalDateTime C = LocalDateTime.now();
    LocalDateTime D = C.plusYears(1);

    LocalDateTimeInterval AB = new LocalDateTimeInterval(A, B);
    LocalDateTimeInterval CD = new LocalDateTimeInterval(C, D);

    // 0 tolerance (the chronoUnit does not matter)
    AB = new LocalDateTimeInterval(A, B, ChronoUnit.FOREVER, 0);
    CD = new LocalDateTimeInterval(C, D, ChronoUnit.NANOS, 0);
    assertTrue(AB.overlapsOrIsContinuousWith(CD));
    assertTrue(CD.overlapsOrIsContinuousWith(AB));

    assertTrue(AB.isContinuousAtStartWith(CD));
    assertTrue(CD.isContinuousAtEndWith(AB));
    assertTrue(CD.isContinuousAtStartWith(AB));
    assertTrue(AB.isContinuousAtEndWith(CD));

    // tolerance > 0
    AB = new LocalDateTimeInterval(A, B, ChronoUnit.NANOS, 898);
    CD = new LocalDateTimeInterval(C, D, ChronoUnit.YEARS, 2);
    assertTrue(AB.overlapsOrIsContinuousWith(CD));
    assertTrue(CD.overlapsOrIsContinuousWith(AB));

    assertTrue(AB.isContinuousAtStartWith(CD));
    assertTrue(CD.isContinuousAtEndWith(AB));
    assertTrue(CD.isContinuousAtStartWith(AB));
    assertTrue(AB.isContinuousAtEndWith(CD));
  }

  // i: __________B
  // i: ----------B____C--------
  @Test
  void overlapsOrIsContinuousWith_isContinuousAtStart_isContinuousAtEnd_test04() {
    LocalDateTime A = LocalDateTime.now();
    LocalDateTime B = A;
    LocalDateTime C = A.plusDays(21);

    LocalDateTimeInterval AB = new LocalDateTimeInterval(A, B);
    LocalDateTimeInterval BC = new LocalDateTimeInterval(B, C);

    // 0 tolerance (the chronoUnit does not matter)
    AB = new LocalDateTimeInterval(A, B, ChronoUnit.FOREVER, 0);
    BC = new LocalDateTimeInterval(B, C, ChronoUnit.NANOS, 0);
    assertTrue(AB.overlapsOrIsContinuousWith(BC));
    assertTrue(BC.overlapsOrIsContinuousWith(AB));

    assertTrue(
        AB.isContinuousAtStartWith(
            BC)); // see warning in method javadoc, functionally strange to do such call
    assertTrue(
        BC.isContinuousAtEndWith(
            AB)); // see warning in method javadoc, functionally strange to do such call
    assertTrue(BC.isContinuousAtStartWith(AB));
    assertTrue(AB.isContinuousAtEndWith(BC));

    // tolerance > 0
    AB = new LocalDateTimeInterval(A, B, ChronoUnit.NANOS, 898);
    BC = new LocalDateTimeInterval(B, C, ChronoUnit.YEARS, 2);
    assertTrue(AB.overlapsOrIsContinuousWith(BC));
    assertTrue(BC.overlapsOrIsContinuousWith(AB));

    assertTrue(
        AB.isContinuousAtStartWith(
            BC)); // see warning in method javadoc, functionally strange to do such call
    assertTrue(
        BC.isContinuousAtEndWith(
            AB)); // see warning in method javadoc, functionally strange to do such call
    assertTrue(BC.isContinuousAtStartWith(AB));
    assertTrue(AB.isContinuousAtEndWith(BC));
  }
}
