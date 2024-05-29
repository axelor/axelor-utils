package com.axelor.utils;

import com.axelor.utils.utils.StringToStringListArgumentConverter;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.axelor.utils.helpers.ListHelper;
import com.google.common.base.Optional;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
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

  @ParameterizedTest(name = "ListHelper.first({0}) = {1}")
  @CsvSource({
    ",",
    "'',",
    "'element1,element2,element3', 'element1'",
    "',,',",
    "'1,2,3', '1'",
    "',element2,',''"
  })
  void testFirst(
      @ConvertWith(StringToStringListArgumentConverter.class) List<String> list, String expected) {
    Optional<String> first = ListHelper.first(list);
    assertEquals(expected, first.orNull());
  }

}
