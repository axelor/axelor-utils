/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2026 Axelor (<http://axelor.com>).
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axelor.utils.exception;

import com.axelor.i18n.I18n;

public final class UtilsExceptionMessage {

  private UtilsExceptionMessage() {}

  /**
   * Used to factorize code &amp; increase readability by avoiding String.format(I18n.get(...), ...,
   * ...) calls.
   *
   * @param keyMessage the key of the message in the i18n file
   * @param args the arguments to replace in the message
   * @return the formatted and translated message
   */
  public static String formatAndTranslate(String keyMessage, Object... args) {
    return String.format(I18n.get(keyMessage), args);
  }

  /** SelectHelper */
  public static final String UNKNOWN_SELECTION = /*$$(*/ "Unknown selection: '%s'." /*)*/;

  public static final String UNKNOWN_SELECTION_AND_TITLE_PAIR = /*$$(*/
      "Unknown selection and title pair: '%s' / '%s'." /*)*/;
  public static final String UNKNOWN_SELECTION_AND_VALUE_PAIR = /*$$(*/
      "Unknown selection and value pair: '%s' / '%s'." /*)*/;
  public static final String NULL_GIVEN_VALUE = /*$$(*/ "Null given value." /*)*/;
  public static final String ERROR_WHILE_SEARCHING_NULL_TITLE_ON_SELECTION = /*$$(*/
      "Error while searching null title from following selection: '%s'." /*)*/;
  public static final String SELECTION_S_IS_NOT_AN_INTEGER_ONE_OR_IS_WRONGLY_DEFINED = /*$$(*/
      "Selection '%s' is not an integer one or is wrongly defined." /*)*/;
  public static final String GIVEN_COUPLE_S_S_MATCH_A_VALUE_WHICH_IS_NOT_AN_INTEGER = /*$$(*/
      "Given couple %s / %s match a value which is not an integer." /*)*/;

  /** Period service */
  public static final String PERIOD_1 = /*$$(*/ "Years in 360 days" /*)*/;

  /** URL service */
  public static final String URL_SERVICE_1 = /*$$(*/
      "Can not opening the connection to a empty URL." /*)*/;

  public static final String URL_SERVICE_2 = /*$$(*/ "Url %s is malformed." /*)*/;
  public static final String URL_SERVICE_3 = /*$$(*/
      "An error occurs while opening the connection. Please verify the following URL : %s." /*)*/;

  /** Template maker */
  public static final String TEMPLATE_MAKER_1 = /*$$(*/ "No such template" /*)*/;

  public static final String TEMPLATE_MAKER_2 = /*$$(*/ "Templating can not be empty" /*)*/;

  public static final String RECORD_UNIQUE_FIELD = /*$$(*/ "This field needs to be unique." /*)*/;

  /** PdfHelper */
  public static final String BAD_COPY_NUMBER_ARGUMENT = /*$$(*/
      "The parameter copyNumber should be superior to 0." /*)*/;

  /** MetaHelper */
  public static final String ERROR_CONVERT_TYPE_TO_JSON_TYPE = /*$$(*/
      "Type %s could not be converted to json type." /*)*/;

  public static final String ERROR_CONVERT_JSON_TYPE_TO_TYPE = /*$$(*/
      "Meta json field type %s could not be converted to a meta field type." /*)*/;

  /** ControllerCallableHelper */
  public static final String PROCESS_BEING_COMPUTED = /*$$(*/ "Computation in progress..." /*)*/;

  @Deprecated
  public static final String EXCEPTION_OCCURRED = /*$$(*/
      "An error occurred with the following message : %s" /*)*/;

  public static final String API_DISABLED = /*$$(*/
      "AOS API calls are disabled, please contact your administrator. The property utils.api.enable is false in application.properties." /*)*/;

  public static final String ORIGIN_FETCHER_TOO_MANY_ITERATIONS = /*$$(*/
      "The process was stopped because the computation might be stuck in an infinite loop." /*)*/;

  public static final String OBJECT_ALREADY_UPDATED = /*$$(*/
      "Object provided has been updated by another user" /*)*/;
}
