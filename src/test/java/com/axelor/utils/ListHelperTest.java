package com.axelor.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.axelor.utils.helpers.ListHelper;
import com.google.common.base.Optional;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ListHelperTest {

  @Test
  public void testFirstWithNullList() {
    Optional<Object> result = ListHelper.first(null);
    assertEquals(result, Optional.absent());
  }

  @Test
  public void testFirstWithEmptyList() {
    List<Object> list = Collections.emptyList();
    Optional<Object> result = ListHelper.first(list);
    assertEquals(result, Optional.absent());
  }

  @Test
  public void testFirstWithNonEmptyList() {
    List<String> list = Arrays.asList("element1", "element2", "element3");
    Optional<String> result = ListHelper.first(list);
    assertTrue(result.isPresent());
    assertEquals("element1", result.get());
  }

  @Test
  public void testFirstWithListOfNullValues() {
    List<String> list = Arrays.asList(null, null, null);
    Optional<String> result = ListHelper.first(list);
    assertFalse(result.isPresent());
  }
}
