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
package com.axelor.utils;

import com.axelor.meta.db.MetaJsonModel;
import com.axelor.utils.helpers.StringHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringHelperTest {

  @Test
  void testToFirstLower() {
    String actual = StringHelper.toFirstLower("Test");
    Assertions.assertEquals("test", actual);
  }

  @Test
  void testToFirstUpper() {
    String actual = StringHelper.toFirstUpper("test");
    Assertions.assertEquals("Test", actual);
  }

  @Test
  void testFillString() {
    String actual = "test";
    String resultRight = "test    ";
    String resultLeft = "    test";

    Assertions.assertEquals(resultRight, StringHelper.fillStringRight(actual, ' ', 8));
    Assertions.assertEquals("te", StringHelper.fillStringRight(actual, ' ', 2));

    Assertions.assertEquals(resultLeft, StringHelper.fillStringLeft(actual, ' ', 8));
    Assertions.assertEquals("st", StringHelper.fillStringLeft(actual, ' ', 2));

    Assertions.assertEquals("    ", StringHelper.fillStringLeft(resultRight, ' ', 4));
  }

  @Test
  void testGetIdListString() {

    List<Long> customModelIds = Arrays.asList(null, 1L, 2L, null, 5L, null);
    List<MetaJsonModel> customModelList = new ArrayList<>();
    for (Long id : customModelIds) {
      MetaJsonModel customModel = new MetaJsonModel();
      customModel.setId(id);
      customModelList.add(customModel);
    }

    String expected = "1,2,5";
    Assertions.assertEquals(expected, StringHelper.getIdListString(customModelList));

    customModelList = null;
    Assertions.assertEquals("0", StringHelper.getIdListString(customModelList));

    customModelList = new ArrayList<>();
    Assertions.assertEquals("0", StringHelper.getIdListString(customModelList));
  }

  @Test
  void reduceLarge_whenStringIsLarge() {
    String largeString =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque enim nisi, rhoncus a sollicitudin sed, pretium vitae massa. Nunc ac urna quis eros cursus efficitur. Vestibulum vel finibus felis. Integer ac nunc nec nisl tincidunt tincidunt. In finibus tellus libero, quis molestie lacus mollis sed. Class aptent taciti sociosqu ad.";
    Assertions.assertEquals(
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque enim nisi, rhoncus a sollicitudin sed, pretium vitae massa. Nunc ac urna quis eros cursus efficitur. Vestibulum vel finibus felis. Integer ac nunc nec nisl tincidunt tincidunt. In f...",
        StringHelper.reduceLarge(largeString));
  }
}
