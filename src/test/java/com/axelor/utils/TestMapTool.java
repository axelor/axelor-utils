package com.axelor.utils;

import com.axelor.auth.db.User;
import com.axelor.auth.db.repo.UserRepository;
import com.axelor.inject.Beans;
import com.axelor.meta.loader.LoaderHelper;
import com.axelor.utils.context.adapters.Processor;
import com.axelor.utils.junit.BaseTest;
import com.google.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestMapTool extends BaseTest {

  protected final LoaderHelper loaderHelper;
  protected final UserRepository userRepository;
  Map<String, List<String>> mapStringListString;
  Map<String, Object> userMap;
  List<Map<String, Object>> userMapList;

  @Inject
  TestMapTool(LoaderHelper loaderHelper, UserRepository userRepository) {
    this.loaderHelper = loaderHelper;
    this.userRepository = userRepository;
  }

  @BeforeEach
  public void setUp() {
    mapStringListString = new HashMap<>();
    mapStringListString.put("key1", List.of("value1", "value2"));
    mapStringListString.put("key2", List.of("value3", "value4"));
    mapStringListString.put("key3", List.of("value5", "value6"));
    mapStringListString.put("key4", List.of("value7", "value8"));
    userMap = Map.of("id", 1L);
    loaderHelper.importCsv("data/users-input.xml");
    userMapList = new ArrayList<>();
    userMapList.add(userMap);
    userMapList.add(Map.of("id", 2L));
    userMapList.add(Map.of("id", 3L));
  }

  @Test
  void testSimplify() {
    Map<String, Object> mapStringObject = MapTools.simplifyMap(mapStringListString);
    Assertions.assertEquals(mapStringObject.size(), mapStringListString.size());
    for (String key : mapStringListString.keySet()) {
      Assertions.assertEquals(mapStringObject.get(key), mapStringListString.get(key));
    }
  }

  @Test
  void testProcessor() {
    Processor processor = Beans.get(Processor.class);
    Assertions.assertEquals(
        LocalDate.of(1996, 1, 1), processor.process(LocalDate.class, "1996-01-01"));
    Assertions.assertEquals(
        LocalDateTime.ofInstant(
            ZonedDateTime.of(1996, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC).toInstant(),
            ZoneId.systemDefault()),
        processor.process(LocalDateTime.class, "1996-01-01T12:00:00.000Z"));
    Assertions.assertEquals("TOTO", processor.process(String.class, "TOTO"));
    Assertions.assertEquals(BigDecimal.TEN, processor.process(BigDecimal.class, BigDecimal.TEN));
    Assertions.assertEquals(BigDecimal.TEN, processor.process(BigDecimal.class, 10));
    Assertions.assertEquals(BigDecimal.TEN, processor.process(BigDecimal.class, "10"));
    Assertions.assertEquals(new BigDecimal("25.6"), processor.process(BigDecimal.class, 25.6));

    List<String> addresses = new ArrayList<>();
    addresses.add("1996-01-01");
    addresses.add("2003-01-01");
    addresses.add("2020-01-01");

    Assertions.assertEquals(
        Arrays.asList(LocalDate.of(1996, 1, 1), LocalDate.of(2003, 1, 1), LocalDate.of(2020, 1, 1)),
        processor.processCollection(LocalDate.class, addresses));

    Assertions.assertEquals(
        userRepository.findByCode("admin"), processor.process(User.class, userMap));

    Assertions.assertEquals(
        Arrays.asList(
            userRepository.findByCode("admin"),
            userRepository.findByCode("client"),
            userRepository.findByCode("supplier")),
        processor.processCollection(User.class, userMapList));
  }
}