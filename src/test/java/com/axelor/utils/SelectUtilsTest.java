package com.axelor.utils;

import static com.axelor.utils.SelectUtils.getIntegerValueFromTitle;
import static com.axelor.utils.SelectUtils.getOptionalIntegerValueFromTitle;
import static com.axelor.utils.SelectUtils.getOptionalStringValueFromTitle;
import static com.axelor.utils.SelectUtils.getOptionalTitleFromIntegerValue;
import static com.axelor.utils.SelectUtils.getOptionalTitleFromStringValue;
import static com.axelor.utils.SelectUtils.getStringValueFromTitle;
import static com.axelor.utils.SelectUtils.getTitleFromIntegerValue;
import static com.axelor.utils.SelectUtils.getTitleFromStringValue;
import static com.axelor.utils.exception.ToolExceptionMessage.ERROR_WHILE_SEARCHING_NULL_TITLE_ON_SELECTION;
import static com.axelor.utils.exception.ToolExceptionMessage.NULL_GIVEN_VALUE;
import static com.axelor.utils.exception.ToolExceptionMessage.UNKNOWN_SELECTION;
import static com.axelor.utils.exception.ToolExceptionMessage.UNKNOWN_SELECTION_AND_TITLE_PAIR;
import static com.axelor.utils.exception.ToolExceptionMessage.UNKNOWN_SELECTION_AND_VALUE_PAIR;
import static com.axelor.utils.exception.ToolExceptionMessage.formatAndTranslate;

import com.axelor.i18n.I18n;
import com.axelor.meta.loader.LoaderHelper;
import com.axelor.utils.junit.BaseTest;
import com.google.inject.Inject;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SelectUtilsTest extends BaseTest {
  protected final LoaderHelper loaderHelper;

  private static final String STRING_SELECT = "utils.string.select";
  private static final String INTEGER_SELECT = "utils.integer.select";
  private static final String UNKWOWN_SELECT = "utils.non.existing.select";

  @Inject
  public SelectUtilsTest(LoaderHelper loaderHelper) {
    this.loaderHelper = loaderHelper;
  }

  @BeforeEach
  void before() {
    loaderHelper.loadViewFile("views/Selects.xml");
  }

  // ____________________________________________________________________________________________
  // ____________________________________________________________________________________________
  // getOptionalStringValueFromTitle

  @Test
  void getOptionalStringValueFromTitle_exitingPair() {
    Optional<String> result = getOptionalStringValueFromTitle(STRING_SELECT, "String option title");
    Assertions.assertEquals(result, Optional.of("one"));
  }

  @Test
  void getOptionalStringValueFromTitle_valueHavingBlankTitle() {
    Optional<String> result = getOptionalStringValueFromTitle(STRING_SELECT, "");
    Assertions.assertEquals(result, Optional.of("two"));
  }

  @Test
  void getOptionalStringValueFromTitle_extistingSelectNonExistingTitle() {
    Optional<String> result = getOptionalStringValueFromTitle(STRING_SELECT, "Non exiting title");
    Assertions.assertEquals(result, Optional.empty());
  }

  @Test
  void getOptionalStringValueFromTitle_nonExistingSelect() {
    Optional<String> result = getOptionalStringValueFromTitle(UNKWOWN_SELECT, "Non blank title");
    Assertions.assertEquals(result, Optional.empty());
  }

  @Test
  void getOptionalStringValueFromTitle_nullParameter() {
    Optional<String> result = getOptionalStringValueFromTitle(STRING_SELECT, null);
    Assertions.assertEquals(result, Optional.empty());
    result = getOptionalStringValueFromTitle(null, "String option title");
    Assertions.assertEquals(result, Optional.empty());
    result = getOptionalStringValueFromTitle(null, null);
    Assertions.assertEquals(result, Optional.empty());
  }

  // ____________________________________________________________________________________________
  // ____________________________________________________________________________________________
  // getStringValueFromTitle

  @Test
  void getStringValueFromTitle_exitingPair() {
    String result = getStringValueFromTitle(STRING_SELECT, "String option title");
    Assertions.assertEquals("one", result);
  }

  @Test
  void getStringValueFromTitle_exitingPairButBlankTitle() {
    String result = getStringValueFromTitle(STRING_SELECT, "");
    Assertions.assertEquals("two", result);
  }

  @Test
  void getStringValueFromTitle_extistingSelectNonExistingTitle() {
    IllegalArgumentException e =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> {
              getStringValueFromTitle(STRING_SELECT, "Non exiting title");
            });
    String expectedMessage =
        formatAndTranslate(UNKNOWN_SELECTION_AND_TITLE_PAIR, STRING_SELECT, "Non exiting title");
    Assertions.assertEquals(expectedMessage, e.getMessage());
  }

  @Test
  void getStringValueFromTitle_nonExistingSelect() {
    IllegalArgumentException e =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> {
              getStringValueFromTitle(UNKWOWN_SELECT, "Non blank title");
            });
    String expectedMessage = formatAndTranslate(UNKNOWN_SELECTION, UNKWOWN_SELECT);
    Assertions.assertEquals(expectedMessage, e.getMessage());
  }

  @Test
  void getStringValueFromTitle_nullParameter() {
    IllegalArgumentException e =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> {
              getStringValueFromTitle(STRING_SELECT, null);
            });
    String expectedMessage =
        formatAndTranslate(ERROR_WHILE_SEARCHING_NULL_TITLE_ON_SELECTION, STRING_SELECT);
    Assertions.assertEquals(expectedMessage, e.getMessage());

    e =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> {
              getStringValueFromTitle(null, "String option title");
            });
    expectedMessage = formatAndTranslate(UNKNOWN_SELECTION, "null");
    Assertions.assertEquals(expectedMessage, e.getMessage());

    e =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> {
              getStringValueFromTitle(null, null);
            });
    expectedMessage = formatAndTranslate(ERROR_WHILE_SEARCHING_NULL_TITLE_ON_SELECTION, "null");
    Assertions.assertEquals(expectedMessage, e.getMessage());
  }

  // ____________________________________________________________________________________________
  // ____________________________________________________________________________________________
  // getOptionalTitleFromStringValue

  @Test
  void getOptionalTitleFromStringValue_exitingPair() {
    Optional<String> result = getOptionalTitleFromStringValue(STRING_SELECT, "one");
    Assertions.assertEquals(result, Optional.of("String option title"));
  }

  @Test
  void getOptionalTitleFromStringValue_exitingPairButBlankTitle() {
    Optional<String> result = getOptionalTitleFromStringValue(STRING_SELECT, "two");
    Assertions.assertEquals(result, Optional.of(""));
  }

  @Test
  void getOptionalTitleFromStringValue_extistingSelectNonExistingValue() {
    Optional<String> result = getOptionalTitleFromStringValue(STRING_SELECT, "Non exiting value");
    Assertions.assertEquals(result, Optional.empty());
  }

  @Test
  void getOptionalTitleFromStringValue_nonExistingSelect() {
    Optional<String> result = getOptionalTitleFromStringValue(UNKWOWN_SELECT, "Non blank value");
    Assertions.assertEquals(result, Optional.empty());
  }

  @Test
  void getOptionalTitleFromStringValue_nullParameter() {
    Optional<String> result = getOptionalTitleFromStringValue(STRING_SELECT, null);
    Assertions.assertEquals(result, Optional.empty());
    result = getOptionalTitleFromStringValue(null, "one");
    Assertions.assertEquals(result, Optional.empty());
    result = getOptionalTitleFromStringValue(null, null);
    Assertions.assertEquals(result, Optional.empty());
  }
  // ____________________________________________________________________________________________
  // ____________________________________________________________________________________________
  // getTitleFromStringValue

  @Test
  void getTitleFromStringValue_exitingPair() {
    String result = getTitleFromStringValue(STRING_SELECT, "one");
    Assertions.assertEquals("String option title", result);
  }

  @Test
  void getTitleFromStringValue_exitingPairButBlankTitle() {
    String result = getTitleFromStringValue(STRING_SELECT, "two");
    Assertions.assertEquals("", result);
  }

  @Test
  void getTitleFromStringValue_extistingSelectNonExistingTitle() {
    IllegalArgumentException e =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> {
              getTitleFromStringValue(STRING_SELECT, "Non exiting value");
            });
    String expectedMessage =
        formatAndTranslate(UNKNOWN_SELECTION_AND_VALUE_PAIR, STRING_SELECT, "Non exiting value");
    Assertions.assertEquals(expectedMessage, e.getMessage());
  }

  @Test
  void getTitleFromStringValue_nonExistingSelect() {
    IllegalArgumentException e =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> {
              getTitleFromStringValue(UNKWOWN_SELECT, "Non blank value");
            });
    String expectedMessage = formatAndTranslate(UNKNOWN_SELECTION, UNKWOWN_SELECT);
    Assertions.assertEquals(expectedMessage, e.getMessage());
  }

  @Test
  void getTitleFromStringValue_nullParameter() {
    IllegalArgumentException e =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> {
              getTitleFromStringValue(STRING_SELECT, null);
            });
    String expectedMessage = I18n.get(NULL_GIVEN_VALUE);
    Assertions.assertEquals(expectedMessage, e.getMessage());

    e =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> {
              getTitleFromStringValue(null, "one");
            });
    expectedMessage = formatAndTranslate(UNKNOWN_SELECTION, "null");
    Assertions.assertEquals(expectedMessage, e.getMessage());

    e =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> {
              getTitleFromStringValue(null, null);
            });
    expectedMessage = I18n.get(NULL_GIVEN_VALUE);
    Assertions.assertEquals(expectedMessage, e.getMessage());
  }

  // ____________________________________________________________________________________________
  // ____________________________________________________________________________________________
  // getOptionalIntegerValueFromTitle

  @Test
  void getOptionalIntegerValueFromTitle_exitingPair() {
    Optional<Integer> result =
        getOptionalIntegerValueFromTitle(INTEGER_SELECT, "Integer option title");
    Assertions.assertEquals(result, Optional.of(1));
  }

  @Test
  void getOptionalIntegerValueFromTitle_valueHavingBlankTitle() {
    Optional<Integer> result = getOptionalIntegerValueFromTitle(INTEGER_SELECT, "");
    Assertions.assertEquals(result, Optional.of(2));
  }

  @Test
  void getOptionalIntegerValueFromTitle_extistingSelectNonExistingTitle() {
    Optional<Integer> result =
        getOptionalIntegerValueFromTitle(INTEGER_SELECT, "Non exiting title");
    Assertions.assertEquals(result, Optional.empty());
  }

  @Test
  void getOptionalIntegerValueFromTitle_nonExistingSelect() {
    Optional<Integer> result = getOptionalIntegerValueFromTitle(UNKWOWN_SELECT, "Non blank title");
    Assertions.assertEquals(result, Optional.empty());
  }

  @Test
  void getOptionalIntegerValueFromTitle_nullParameter() {
    Optional<Integer> result = getOptionalIntegerValueFromTitle(INTEGER_SELECT, null);
    Assertions.assertEquals(result, Optional.empty());
    result = getOptionalIntegerValueFromTitle(null, "Integer option title");
    Assertions.assertEquals(result, Optional.empty());
    result = getOptionalIntegerValueFromTitle(null, null);
    Assertions.assertEquals(result, Optional.empty());
  }

  // ____________________________________________________________________________________________
  // ____________________________________________________________________________________________
  // getIntegerValueFromTitle

  @Test
  void getIntegerValueFromTitle_exitingPair() {
    Integer result = getIntegerValueFromTitle(INTEGER_SELECT, "Integer option title");
    Assertions.assertEquals(1, result);
  }

  @Test
  void getIntegerValueFromTitle_exitingPairButBlankTitle() {
    Integer result = getIntegerValueFromTitle(INTEGER_SELECT, "");
    Assertions.assertEquals(2, result);
  }

  @Test
  void getIntegerValueFromTitle_extistingSelectNonExistingTitle() {
    IllegalArgumentException e =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> {
              getIntegerValueFromTitle(INTEGER_SELECT, "Non exiting title");
            });
    String expectedMessage =
        formatAndTranslate(UNKNOWN_SELECTION_AND_TITLE_PAIR, INTEGER_SELECT, "Non exiting title");
    Assertions.assertEquals(expectedMessage, e.getMessage());
  }

  @Test
  void getIntegerValueFromTitle_nonExistingSelect() {
    IllegalArgumentException e =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> {
              getIntegerValueFromTitle(UNKWOWN_SELECT, "Non blank title");
            });
    String expectedMessage = formatAndTranslate(UNKNOWN_SELECTION, UNKWOWN_SELECT);
    Assertions.assertEquals(expectedMessage, e.getMessage());
  }

  @Test
  void getIntegerValueFromTitle_nullParameter() {
    IllegalArgumentException e =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> {
              getIntegerValueFromTitle(INTEGER_SELECT, null);
            });
    String expectedMessage =
        formatAndTranslate(ERROR_WHILE_SEARCHING_NULL_TITLE_ON_SELECTION, INTEGER_SELECT);
    Assertions.assertEquals(expectedMessage, e.getMessage());

    e =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> {
              getIntegerValueFromTitle(null, "Integer option title");
            });
    expectedMessage = formatAndTranslate(UNKNOWN_SELECTION, "null");
    Assertions.assertEquals(expectedMessage, e.getMessage());

    e =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> {
              getIntegerValueFromTitle(null, null);
            });
    expectedMessage = formatAndTranslate(ERROR_WHILE_SEARCHING_NULL_TITLE_ON_SELECTION, "null");
    Assertions.assertEquals(expectedMessage, e.getMessage());
  }

  // ____________________________________________________________________________________________
  // ____________________________________________________________________________________________
  // getOptionalTitleFromIntegerValue

  @Test
  void getOptionalTitleFromIntegerValue_exitingPair() {
    Optional<String> result = getOptionalTitleFromIntegerValue(INTEGER_SELECT, 1);
    Assertions.assertEquals(result, Optional.of("Integer option title"));
  }

  @Test
  void getOptionalTitleFromIntegerValue_exitingPairButBlankTitle() {
    Optional<String> result = getOptionalTitleFromIntegerValue(INTEGER_SELECT, 2);
    Assertions.assertEquals(result, Optional.of(""));
  }

  @Test
  void getOptionalTitleFromIntegerValue_extistingSelectNonExistingValue() {
    Integer nonExistingValue = 999;
    Optional<String> result = getOptionalTitleFromIntegerValue(INTEGER_SELECT, nonExistingValue);
    Assertions.assertEquals(result, Optional.empty());
  }

  @Test
  void getOptionalTitleFromIntegerValue_nonExistingSelect() {
    Integer nonNullValue = 999;
    Optional<String> result = getOptionalTitleFromIntegerValue(UNKWOWN_SELECT, nonNullValue);
    Assertions.assertEquals(result, Optional.empty());
  }

  @Test
  void getOptionalTitleFromIntegerValue_nullParameter() {
    Optional<String> result = getOptionalTitleFromIntegerValue(INTEGER_SELECT, null);
    Assertions.assertEquals(result, Optional.empty());
    result = getOptionalTitleFromIntegerValue(null, 1);
    Assertions.assertEquals(result, Optional.empty());
    result = getOptionalTitleFromIntegerValue(null, null);
    Assertions.assertEquals(result, Optional.empty());
  }
  // ____________________________________________________________________________________________
  // ____________________________________________________________________________________________
  // getTitleFromIntegerValue

  @Test
  void getTitleFromIntegerValue_exitingPair() {
    String result = getTitleFromIntegerValue(INTEGER_SELECT, 1);
    Assertions.assertEquals("Integer option title", result);
  }

  @Test
  void getTitleFromIntegerValue_exitingPairButBlankTitle() {
    String result = getTitleFromIntegerValue(INTEGER_SELECT, 2);
    Assertions.assertEquals("", result);
  }

  @Test
  void getTitleFromIntegerValue_extistingSelectNonExistingTitle() {
    Integer nonExitingValue = 999;
    IllegalArgumentException e =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> {
              getTitleFromIntegerValue(INTEGER_SELECT, nonExitingValue);
            });
    String expectedMessage =
        formatAndTranslate(UNKNOWN_SELECTION_AND_VALUE_PAIR, INTEGER_SELECT, nonExitingValue);
    Assertions.assertEquals(expectedMessage, e.getMessage());
  }

  @Test
  void getTitleFromIntegerValue_nonExistingSelect() {
    Integer nonNullValue = 999;
    IllegalArgumentException e =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> {
              getTitleFromIntegerValue(UNKWOWN_SELECT, nonNullValue);
            });
    String expectedMessage = formatAndTranslate(UNKNOWN_SELECTION, UNKWOWN_SELECT);
    Assertions.assertEquals(expectedMessage, e.getMessage());
  }

  @Test
  void getTitleFromIntegerValue_nullParameter() {
    IllegalArgumentException e =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> {
              getTitleFromIntegerValue(INTEGER_SELECT, null);
            });
    String expectedMessage = I18n.get(NULL_GIVEN_VALUE);
    Assertions.assertEquals(expectedMessage, e.getMessage());

    e =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> {
              getTitleFromIntegerValue(null, 1);
            });
    expectedMessage = formatAndTranslate(UNKNOWN_SELECTION, "null");
    Assertions.assertEquals(expectedMessage, e.getMessage());

    e =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> {
              getTitleFromIntegerValue(null, null);
            });
    expectedMessage = I18n.get(NULL_GIVEN_VALUE);
    Assertions.assertEquals(expectedMessage, e.getMessage());
  }
}
