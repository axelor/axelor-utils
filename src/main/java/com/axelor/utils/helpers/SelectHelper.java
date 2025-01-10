package com.axelor.utils.helpers;

import static com.axelor.utils.exception.UtilsExceptionMessage.ERROR_WHILE_SEARCHING_NULL_TITLE_ON_SELECTION;
import static com.axelor.utils.exception.UtilsExceptionMessage.GIVEN_COUPLE_S_S_MATCH_A_VALUE_WHICH_IS_NOT_AN_INTEGER;
import static com.axelor.utils.exception.UtilsExceptionMessage.NULL_GIVEN_VALUE;
import static com.axelor.utils.exception.UtilsExceptionMessage.SELECTION_S_IS_NOT_AN_INTEGER_ONE_OR_IS_WRONGLY_DEFINED;
import static com.axelor.utils.exception.UtilsExceptionMessage.UNKNOWN_SELECTION;
import static com.axelor.utils.exception.UtilsExceptionMessage.UNKNOWN_SELECTION_AND_TITLE_PAIR;
import static com.axelor.utils.exception.UtilsExceptionMessage.UNKNOWN_SELECTION_AND_VALUE_PAIR;
import static com.axelor.utils.exception.UtilsExceptionMessage.formatAndTranslate;

import com.axelor.common.ObjectUtils;
import com.axelor.i18n.I18n;
import com.axelor.meta.MetaStore;
import com.axelor.meta.schema.views.Selection.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.math.NumberUtils;

public class SelectHelper {

  private SelectHelper() {
    throw new IllegalStateException("Utility class");
  }

  // STRING SELECTIONS ------------------------------------------------------------------

  /**
   * Method to search value from a couple (selectName / title) when it may not match any value.
   * Otherwise, prefer using {@link #getStringValueFromTitle(String, String)}.
   *
   * @param selectName: String representing the name of a string selection.
   * @param title: String representing the title (as displayed before translation in views) of a
   *     value among the possible ones from to selection named by selectName.
   * @return an Optional String containing the value as stored in database corresponding to the
   *     given couple (selectName / title).
   */
  public static Optional<String> getOptionalStringValueFromTitle(String selectName, String title) {
    final List<Option> options = MetaStore.getSelectionList(selectName);
    if (ObjectUtils.isEmpty(options) || title == null) {
      return Optional.empty();
    }
    final Option option =
        options.stream().filter(x -> title.equalsIgnoreCase(x.getTitle())).findFirst().orElse(null);
    if (option == null) {
      return Optional.empty();
    }

    return Optional.of(option.getValue());
  }

  /**
   * Method to search value from a couple (selectName / title) when it is certain to match a value.
   * Otherwise, prefer using {@link #getOptionalStringValueFromTitle(String, String)}.
   *
   * @param selectName: String representing the name of a string selection.
   * @param title: String representing the title (as displayed before translation in views) of a
   *     value among the possible ones from to selection named by selectName.
   * @return a String value as stored in database corresponding to the given couple (selectName /
   *     title).
   * @throws IllegalArgumentException if the couple (selectName / title) doesn't match any existing
   *     selection.
   */
  public static String getStringValueFromTitle(String selectName, String title) {

    if (title == null) {
      String message =
          formatAndTranslate(ERROR_WHILE_SEARCHING_NULL_TITLE_ON_SELECTION, selectName);
      throw new IllegalArgumentException(message);
    }

    List<Option> options = MetaStore.getSelectionList(selectName);
    if (ObjectUtils.isEmpty(options)) {
      String message = formatAndTranslate(UNKNOWN_SELECTION, selectName);
      throw new IllegalArgumentException(message);
    }

    return getOptionalStringValueFromTitle(selectName, title)
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    formatAndTranslate(UNKNOWN_SELECTION_AND_TITLE_PAIR, selectName, title)));
  }

  /**
   * Method to search title corresponding to a couple (selectName / value) when it may not match any
   * result. Otherwise, prefer using {@link #getTitleFromStringValue(String, String)}.
   *
   * @param selectName String representing the name of a string selection.
   * @param value String representing the value among the possible ones from to selection named by
   *     selectName.
   * @return an Optional String containing to the label as displayed (before translation) in views
   *     corresponding to given couple (selectName &amp; value).
   */
  public static Optional<String> getOptionalTitleFromStringValue(String selectName, String value) {
    final List<Option> options = MetaStore.getSelectionList(selectName);
    if (ObjectUtils.isEmpty(options) || value == null) {
      return Optional.empty();
    }
    final Option option =
        options.stream().filter(x -> Objects.equals(x.getValue(), value)).findFirst().orElse(null);
    if (option == null) {
      return Optional.empty();
    }

    return Optional.of(option.getTitle());
  }

  /**
   * Method to search title corresponding to a couple (selectName / value) when it is certain that
   * there is a match. Otherwise, prefer using {@link #getOptionalTitleFromStringValue(String,
   * String)}.
   *
   * @param selectName: String representing the name of a string selection.
   * @param value: String representing the value among the possible ones from to selection named by
   *     selectName.
   * @return the String corresponding to the label as displayed (before translation) in views
   *     corresponding to given couple (selectName &amp; value). Can be null if no title is
   *     associated to the found selection option.
   * @throws IllegalArgumentException if the couple (selectName / value) doesn't match any existing
   *     selection.
   */
  public static String getTitleFromStringValue(String selectName, String value) {
    if (value == null) {
      throw new IllegalArgumentException(I18n.get(NULL_GIVEN_VALUE));
    }
    final List<Option> options = MetaStore.getSelectionList(selectName);
    if (ObjectUtils.isEmpty(options)) {
      String message = formatAndTranslate(UNKNOWN_SELECTION, selectName);
      throw new IllegalArgumentException(message);
    }

    final Option option =
        options.stream().filter(x -> Objects.equals(x.getValue(), value)).findFirst().orElse(null);
    if (option == null) {
      String message = formatAndTranslate(UNKNOWN_SELECTION_AND_VALUE_PAIR, selectName, value);
      throw new IllegalArgumentException(message);
    }

    // May be null
    return option.getTitle();
  }

  /**
   * @param selectName: String representing the name of a string selection.
   * @return a List containing all the existing values associated to the selection.
   * @throws IllegalArgumentException if the selectName doesn't match any selection in the
   *     application.
   */
  public static List<String> getPossibleValuesForStringSelection(String selectName) {

    final List<Option> options = MetaStore.getSelectionList(selectName);
    if (ObjectUtils.isEmpty(options)) {
      String message = formatAndTranslate(UNKNOWN_SELECTION, selectName);
      throw new IllegalArgumentException(message);
    }

    return options.stream().map(Option::getValue).collect(Collectors.toList());
  }

  // INT SELECTIONS ------------------------------------------------------------------

  /**
   * Method to search Integer value from a couple (selectName / title) when it may not match any
   * value. Otherwise, prefer using {@link #getIntegerValueFromTitle(String, String)}.
   *
   * @param selectName String representing the name of an integer selection.
   * @param title String representing the title (as displayed before translation in views) of a
   *     value among the possible ones from to selection named by selectName.
   * @return an Optional Integer containing the value as stored in database corresponding to the
   *     given couple (selectName / title).
   */
  public static Optional<Integer> getOptionalIntegerValueFromTitle(
      String selectName, String title) {
    Optional<String> optionalStringValueFromTitle =
        getOptionalStringValueFromTitle(selectName, title);
    if (!optionalStringValueFromTitle.isPresent()) {
      return Optional.empty();
    }

    String valueAsString = optionalStringValueFromTitle.get();
    if (!NumberUtils.isParsable(valueAsString)) {
      return Optional.empty();
    }

    return Optional.of(Integer.parseInt(valueAsString));
  }

  /**
   * Method to search value from a couple (selectName / title) when it is certain to match a value.
   * Otherwise, prefer using {@link #getOptionalIntegerValueFromTitle(String, String)}.
   *
   * @param selectName: String representing the name of an integer selection.
   * @param title: String representing the title (as displayed before translation in views) of a
   *     value among the possible ones from to selection named by selectName.
   * @return Integer value as stored in database corresponding to the given couple (selectName /
   *     title).
   * @throws IllegalArgumentException if the couple (selectName / title) doesn't match any existing
   *     selection.
   * @throws IllegalArgumentException if the couple (selectName / title) match a selection with
   *     non-numeric values.
   */
  public static Integer getIntegerValueFromTitle(String selectName, String title) {
    String valueAsString = getStringValueFromTitle(selectName, title);
    // We never use decimal numbers in selection options so isParsable is enough
    if (!NumberUtils.isParsable(valueAsString)) {
      String message =
          formatAndTranslate(
              GIVEN_COUPLE_S_S_MATCH_A_VALUE_WHICH_IS_NOT_AN_INTEGER, selectName, title);
      throw new IllegalArgumentException(message);
    }

    return Integer.parseInt(valueAsString);
  }

  /**
   * Method to search title corresponding to a couple (selectName / value) when it may not match any
   * result. Otherwise, prefer using {@link #getTitleFromIntegerValue(String, Integer)}.
   *
   * @param selectName: String representing the name of an integer selection.
   * @param value: Integer representing the value among the possible ones from to selection named by
   *     selectName.
   * @return an Optional String containing to the label as displayed (before translation) in views
   *     corresponding to given couple (selectName &amp; value).
   */
  public static Optional<String> getOptionalTitleFromIntegerValue(
      String selectName, Integer value) {

    if (value == null) {
      return Optional.empty();
    }

    return getOptionalTitleFromStringValue(selectName, value.toString());
  }

  /**
   * Method to search title corresponding to a couple (selectName / value) when it is certain that
   * there is a match. Otherwise, prefer using {@link #getOptionalTitleFromIntegerValue(String,
   * Integer)}
   *
   * @param selectName String representing the name of an integer selection.
   * @param value String representing the value among the possible ones from to selection named by
   *     selectName.
   * @return the String corresponding to the label as displayed (before translation) in views
   *     corresponding to given couple (selectName &amp; value).
   * @throws IllegalArgumentException if the couple (selectName / value) doesn't match any existing
   *     selection.
   */
  public static String getTitleFromIntegerValue(String selectName, Integer value) {

    if (value == null) {
      throw new IllegalArgumentException(I18n.get(NULL_GIVEN_VALUE));
    }

    return getTitleFromStringValue(selectName, value.toString());
  }

  /**
   * @param selectName: String representing the name of an integer selection.
   * @return a List containing all the existing values cast as Integer associated to the selection.
   * @throws IllegalArgumentException if the selectName doesn't match any selection in the
   *     application.
   *     <p>Or if the selectName match a non-integer selection.
   */
  public static List<Integer> getPossibleValuesForIntegerSelection(String selectName) {

    final List<Option> options = MetaStore.getSelectionList(selectName);
    if (ObjectUtils.isEmpty(options)) {
      String message = formatAndTranslate(UNKNOWN_SELECTION, selectName);
      throw new IllegalArgumentException(message);
    }

    List<Integer> possibleValues = new ArrayList<>();
    for (Option option : options) {
      String value = option.getValue();
      // We never use decimal numbers in selection options so isParsable is enough
      if (NumberUtils.isParsable(value)) {
        possibleValues.add(Integer.valueOf(value));
      } else {
        String message =
            formatAndTranslate(SELECTION_S_IS_NOT_AN_INTEGER_ONE_OR_IS_WRONGLY_DEFINED, selectName);
        throw new IllegalArgumentException(message);
      }
    }
    return possibleValues;
  }
}
