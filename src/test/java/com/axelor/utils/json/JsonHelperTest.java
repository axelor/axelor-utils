package com.axelor.utils.json;

import com.axelor.auth.db.User;
import com.axelor.meta.loader.LoaderHelper;
import com.axelor.utils.db.Move;
import com.axelor.utils.db.MoveLine;
import com.axelor.utils.helpers.json.JsonHelper;
import com.axelor.utils.junit.BaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.io.IOException;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import org.apache.groovy.util.Maps;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonHelperTest extends BaseTest {

  protected final LoaderHelper loaderHelper;

  @Inject
  JsonHelperTest(LoaderHelper loaderHelper) {
    this.loaderHelper = loaderHelper;
  }

  @BeforeEach
  void setUp() {
    loaderHelper.importCsv("data/users-input.xml");
    loaderHelper.importCsv("data/move-references-input.xml");
    loaderHelper.importCsv("data/moves-input.xml");
    loaderHelper.importCsv("data/move-lines-input.xml");
  }

  @AfterEach
  @Transactional(rollbackOn = Exception.class)
  void tearDown() {
    all(MoveLine.class).delete();
  }

  @Test
  void toJson_map() throws JsonProcessingException {
    var map = Maps.of("a", 1, "b", 2);
    var json = JsonHelper.toJson(map);
    Assertions.assertEquals("{\"a\":1,\"b\":2}", json);
  }

  @Test
  void parse_map() throws IOException {
    var json = "{\"a\":1,\"b\":2}";
    var map = JsonHelper.parse(json, Map.class);
    Assertions.assertEquals(Maps.of("a", 1, "b", 2), map);
  }

  @Test
  void toJson_user() throws IOException {
    var user = all(User.class).fetchOne();
    var json = JsonHelper.toJson(user);

    Assertions.assertNotNull(json);
    var map = JsonHelper.parse(json, Map.class);
    Assertions.assertEquals(user.getId().intValue(), map.get("id"));
    Assertions.assertEquals(user.getCode(), map.get("code"));
    Assertions.assertEquals(user.getName(), map.get("name"));
    Assertions.assertEquals(user.getPassword(), map.get("password"));
    Assertions.assertEquals(
        user.getCreatedOn().format(DateTimeFormatter.ISO_DATE_TIME), map.get("createdOn"));
  }

  @Test
  void toJson_move() throws IOException {
    var move = all(Move.class).fetchOne();
    var json = JsonHelper.toJson(move);
    System.out.println(json);

    Assertions.assertNotNull(json);
    var map = JsonHelper.parse(json, Map.class);
    Assertions.assertEquals(move.getId().intValue(), map.get("id"));
    Assertions.assertEquals(move.getCode(), map.get("code"));
    Assertions.assertNotNull(map.get("moveLines"));
    Assertions.assertInstanceOf(List.class, map.get("moveLines"));
    var moveLines = (List<?>) map.get("moveLines");
    for (int i = 0; i < moveLines.size(); i++) {
      var moveLine = moveLines.get(i);
      Assertions.assertNotNull(moveLine);
      Assertions.assertInstanceOf(Map.class, moveLine);
      var lineMap = (Map<?, ?>) moveLine;
      Assertions.assertEquals(move.getMoveLines().get(i).getId().intValue(), lineMap.get("id"));
      Assertions.assertEquals(
          move.getMoveLines().get(i).getDate().format(DateTimeFormatter.ISO_DATE),
          lineMap.get("date"));
      Assertions.assertEquals(
          move.getMoveLines().get(i).getDueDate().format(DateTimeFormatter.ISO_DATE),
          lineMap.get("dueDate"));
      Assertions.assertEquals(
          move.getMoveLines().get(i).getCredit().setScale(2, RoundingMode.HALF_UP).doubleValue(),
          lineMap.get("credit"));
      Assertions.assertEquals(
          move.getMoveLines().get(i).getDebit().setScale(2, RoundingMode.HALF_UP).doubleValue(),
          lineMap.get("debit"));
    }
  }
}
