package com.axelor.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.axelor.utils.helpers.ComputeNameHelper;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ComputeNameHelperTest {

  @ParameterizedTest
  @MethodSource("provideDataToComputeSimpleFullName")
  void computeSimpleFullName(String firstName, String lastName, String id, String expected) {
    String result = ComputeNameHelper.computeSimpleFullName(firstName, lastName, id);
    assertEquals(expected, result);
  }

  private static Stream<Arguments> provideDataToComputeSimpleFullName() {
    return Stream.of(
        Arguments.of("John", "Doe", "1", "Doe John"),
        Arguments.of("", "Doe", "1", "Doe"),
        Arguments.of("John", "", "1", "John"),
        Arguments.of("", "", "1", "1"),
        Arguments.of(null, "Doe", "1", "Doe"),
        Arguments.of("John", null, "1", "John"),
        Arguments.of(null, null, "1", "1"));
  }

  @ParameterizedTest
  @MethodSource("provideDataToComputeFullName")
  void computeFullName(
      String firstName, String lastName, String sequence, String id, String expected) {
    String result = ComputeNameHelper.computeFullName(firstName, lastName, sequence, id);
    assertEquals(expected, result);
  }

  private static Stream<Arguments> provideDataToComputeFullName() {
    return Stream.of(
        Arguments.of("John", "Doe", "Seq", "1", "Seq - Doe John"),
        Arguments.of("John", "Doe", "", "1", "Doe John"),
        Arguments.of(null, "Doe", null, "1", "Doe"),
        Arguments.of("John", null, null, "1", "John"),
        Arguments.of(null, null, null, "1", "1"));
  }
}
