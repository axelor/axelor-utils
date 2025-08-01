/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2025 Axelor (<http://axelor.com>).
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
package com.axelor.utils.helpers;

import java.util.ArrayList;
import java.util.List;

/** Class to create a html list as string */
public class StringHtmlListBuilder {

  private final List<String> listElements;

  public StringHtmlListBuilder() {
    listElements = new ArrayList<>();
  }

  protected StringHtmlListBuilder(List<String> listElements) {
    this.listElements = listElements;
  }

  public void append(String s) {
    listElements.add(s);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("<ul>");
    for (String s : listElements) {
      sb.append("<li>");
      sb.append(s);
      sb.append("</li>");
    }
    sb.append("</ul>");
    return sb.toString();
  }

  public static String formatMessage(String title, List<String> listElements) {
    String message = formatMessage(listElements);
    if (title != null && !title.isEmpty()) {
      message = String.format("<b>%s</b><br/>", title) + message;
    }
    return message;
  }

  public static String formatMessage(List<String> listElements) {
    StringHtmlListBuilder sb = new StringHtmlListBuilder(listElements);
    return sb.toString();
  }
}
