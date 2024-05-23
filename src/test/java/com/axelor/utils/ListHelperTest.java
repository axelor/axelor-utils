package com.axelor.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.axelor.utils.helpers.ListHelper;
import com.google.common.base.Optional;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ListHelperTest {

  @ParameterizedTest
  @MethodSource("provideDataToTestIntersection")
  <T> void testIntersection(List<T> list1, List<T> list2, List<T> expected) {
    assertEquals(expected, ListHelper.intersection(list1, list2));
    assertEquals(expected, ListHelper.intersection(list2, list1));
  }

  private static Stream<Arguments> provideDataToTestIntersection() {
    return Stream.of(
        Arguments.of(
            Arrays.asList("element1", "element2", "element3"),
            Arrays.asList("element2", "element3", "element4"),
            Arrays.asList("element2", "element3")),
        Arguments.of(
            Collections.emptyList(),
            Arrays.asList("element2", "element3", "element4"),
            Collections.emptyList()),
        Arguments.of(
            Arrays.asList("element1", "element2", "element3"), null, Collections.emptyList()),
        Arguments.of(
            Arrays.asList("element1", "element2", "element3"),
            Arrays.asList("element4", "element5", "element6"),
            Collections.emptyList()),
        Arguments.of(Arrays.asList(1, 2, 3), Arrays.asList(2, 3, 4), Arrays.asList(2, 3)),
        Arguments.of(Collections.emptyList(), Arrays.asList(2, 3, 4), Collections.emptyList()),
        Arguments.of(Arrays.asList(1, 2, 3), null, Collections.emptyList()),
        Arguments.of(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6), Collections.emptyList()));
  }

  @ParameterizedTest
  @MethodSource("provideDataToTestFirst")
  <T> void testFirst(List<T> list, Optional<T> expected) {
    assertEquals(expected, ListHelper.first(list));
  }

  private static Stream<Arguments> provideDataToTestFirst() {
    return Stream.of(
        Arguments.of(null, Optional.absent()),
        Arguments.of(Collections.emptyList(), Optional.absent()),
        Arguments.of(Arrays.asList("element1", "element2", "element3"), Optional.of("element1")),
        Arguments.of(Arrays.asList(null, null, null), Optional.absent()));
  }
}
