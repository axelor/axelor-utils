package com.axelor.utils.helpers;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringHtmlListBuilderTest {

  @Test
  void formatMessage_emptyTitle() {
    String title = "";
    List<String> elementList = new ArrayList<>();
    elementList.add("code1");
    elementList.add("code2");
    String expected = "<ul><li>code1</li><li>code2</li></ul>";
    Assertions.assertEquals(StringHtmlListBuilder.formatMessage(title, elementList), expected);
  }

  @Test
  void formatMessage_nullTitle() {
    String title = null;
    List<String> elementList = new ArrayList<>();
    elementList.add("code1");
    elementList.add("code2");
    String expected = "<ul><li>code1</li><li>code2</li></ul>";
    Assertions.assertEquals(StringHtmlListBuilder.formatMessage(title, elementList), expected);
  }

  @Test
  void formatMessage_noTitle() {
    List<String> elementList = new ArrayList<>();
    elementList.add("code1");
    elementList.add("code2");
    String expected = "<ul><li>code1</li><li>code2</li></ul>";
    Assertions.assertEquals(StringHtmlListBuilder.formatMessage(elementList), expected);
  }

  @Test
  void formatMessage_withTitle() {
    String title = "following products are not configured correctly:";
    List<String> elementList = new ArrayList<>();
    elementList.add("code1");
    elementList.add("code2");
    elementList.add("code3");
    String expected =
        "<b>following products are not configured correctly:</b><br/><ul><li>code1</li><li>code2</li><li>code3</li></ul>";
    Assertions.assertEquals(StringHtmlListBuilder.formatMessage(title, elementList), expected);
  }
}
