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

import com.axelor.common.ObjectUtils;
import java.util.regex.Pattern;

/** Utility class for email address validation and manipulation. */
public final class EmailHelper {

  // Local part: allows letters, digits, underscore, dash, plus, and dots between segments
  // Using possessive quantifiers (++) to prevent catastrophic backtracking
  private static final String LOCAL_PART = "[_A-Za-z0-9+-]++(?:\\.[_A-Za-z0-9-]++)*+";

  // Domain: first segment + optional intermediate segments + TLD
  private static final String DOMAIN = "[A-Za-z0-9-]+(?:\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}";

  private static final String EMAIL_PATTERN = "^" + LOCAL_PART + "@" + DOMAIN + "$";

  private static final Pattern COMPILED_PATTERN = Pattern.compile(EMAIL_PATTERN);

  private EmailHelper() {}

  /**
   * Validates if the given string is a valid email address.
   *
   * @param email the email address to validate
   * @return {@code true} if the email address is valid, {@code false} otherwise
   */
  public static boolean isValidEmailAddress(String email) {
    if (ObjectUtils.isEmpty(email)) {
      return false;
    }
    return COMPILED_PATTERN.matcher(email).matches();
  }
}
