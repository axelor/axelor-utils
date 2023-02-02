package com.axelor.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestMapTool {

  Map<String, List<String>> mapStringListString;

  @Before
  public void setUp() {

    mapStringListString = new HashMap<>();
    mapStringListString.put("key1", List.of("value1", "value2"));
    mapStringListString.put("key2", List.of("value3", "value4"));
    mapStringListString.put("key3", List.of("value5", "value6"));
    mapStringListString.put("key4", List.of("value7", "value8"));
  }

  @Test
  public void testSimplify() {
    Map<String, Object> mapStringObject = MapTools.simplifyMap(mapStringListString);
    Assert.assertEquals(mapStringObject.size(), mapStringListString.size());
    for (String key : mapStringListString.keySet()) {
      Assert.assertEquals(mapStringObject.get(key), mapStringListString.get(key));
    }
  }
}
