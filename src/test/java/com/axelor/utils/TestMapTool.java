package com.axelor.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestMapTool {

  Map<String, List<String>> mapStringListString;

  @BeforeEach
  public void setUp() {

    mapStringListString = new HashMap<>();
    mapStringListString.put("key1", List.of("value1", "value2"));
    mapStringListString.put("key2", List.of("value3", "value4"));
    mapStringListString.put("key3", List.of("value5", "value6"));
    mapStringListString.put("key4", List.of("value7", "value8"));
  }

  @Test
  void testSimplify() {
    Map<String, Object> mapStringObject = MapTools.simplifyMap(mapStringListString);
    Assertions.assertEquals(mapStringObject.size(), mapStringListString.size());
    for (String key : mapStringListString.keySet()) {
      Assertions.assertEquals(mapStringObject.get(key), mapStringListString.get(key));
    }
  }
}
