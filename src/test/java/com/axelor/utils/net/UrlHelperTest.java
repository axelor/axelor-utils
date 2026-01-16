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
package com.axelor.utils.net;

import com.axelor.i18n.I18n;
import com.axelor.utils.exception.UtilsExceptionMessage;
import com.axelor.utils.helpers.net.UrlHelper;
import com.axelor.utils.junit.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UrlHelperTest extends BaseTest {

  @Test
  void testNotExist() {
    String url = "https://www.google.com";
    Assertions.assertNull(UrlHelper.notExist(url));
  }

  @Test
  void testThrowsException2() {
    String url = "www.google.com";
    String expected = String.format(I18n.get(UtilsExceptionMessage.URL_SERVICE_2), url);
    Assertions.assertEquals(expected, UrlHelper.notExist(url));
  }

  @Test
  void testThrowsException3() {
    String url = "https://testnotfound.axelor.com/";
    String expected = String.format(I18n.get(UtilsExceptionMessage.URL_SERVICE_3), url);
    Assertions.assertEquals(expected, UrlHelper.notExist(url));
  }
}
