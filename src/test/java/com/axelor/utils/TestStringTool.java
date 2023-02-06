/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2022 Axelor (<http://axelor.com>).
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestStringTool {

  @Test
  public void testToFirstLower() {

    String actual = "Test";
    String result = "test";

    Assertions.assertEquals(StringTool.toFirstLower(actual), result);
  }

  @Test
  public void testToFirstUpper() {

    String actual = "test";
    String result = "Test";

    Assertions.assertEquals(StringTool.toFirstUpper(actual), result);
  }

  @Test
  public void testFillString() {

    String actual = "test";
    String resultRight = "test    ";
    String resultLeft = "    test";

    Assertions.assertEquals(StringTool.fillStringRight(actual, ' ', 8), resultRight);
    Assertions.assertEquals(StringTool.fillStringRight(actual, ' ', 2), "te");

    Assertions.assertEquals(StringTool.fillStringLeft(actual, ' ', 8), resultLeft);
    Assertions.assertEquals(StringTool.fillStringLeft(actual, ' ', 2), "st");

    Assertions.assertEquals(StringTool.fillStringLeft(resultRight, ' ', 4), "    ");
  }

  @Test
  public void testGetIdListString() {

    List<Long> customModelIds = Arrays.asList(null, 1l, 2l, null, 5l, null);
    List<MetaJsonModel> customModelList = new ArrayList<>();
    for (Long id : customModelIds) {
      MetaJsonModel customModel = new MetaJsonModel();
      customModel.setId(id);
      customModelList.add(customModel);
    }

    String expected = "1,2,5";
    Assertions.assertEquals(expected, StringTool.getIdListString(customModelList));

    customModelList = null;
    Assertions.assertEquals("0", StringTool.getIdListString(customModelList));

    customModelList = new ArrayList<>();
    Assertions.assertEquals("0", StringTool.getIdListString(customModelList));
  }

  @Test
  public void reduceLarge_whenStringIsLarge() {
    String largeString =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque enim nisi, rhoncus a sollicitudin sed, pretium vitae massa. Nunc ac urna quis eros cursus efficitur. Vestibulum vel finibus felis. Integer ac nunc nec nisl tincidunt tincidunt. In finibus tellus libero, quis molestie lacus mollis sed. Class aptent taciti sociosqu ad.";
    Assertions.assertEquals(
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque enim nisi, rhoncus a sollicitudin sed, pretium vitae massa. Nunc ac urna quis eros cursus efficitur. Vestibulum vel finibus felis. Integer ac nunc nec nisl tincidunt tincidunt. In f...",
        StringTool.reduceLarge(largeString));
  }
}
