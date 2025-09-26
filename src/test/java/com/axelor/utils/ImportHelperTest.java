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
package com.axelor.utils;

import com.axelor.db.JPA;
import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaMenu;
import com.axelor.meta.loader.LoaderHelper;
import com.axelor.utils.helpers.ImportHelper;
import com.axelor.utils.junit.BaseTest;
import com.google.inject.persist.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ImportHelperTest extends BaseTest {

  @BeforeAll
  static void setUp() {
    LoaderHelper loaderHelper = Beans.get(LoaderHelper.class);
    loaderHelper.importCsv("data/meta-actions-input.xml");
    loaderHelper.importCsv("data/meta-menus-input.xml");
    loaderHelper.importCsv("data/dms-permission-selects-input.xml");
    loaderHelper.importCsv("data/dms-permission-select-items-input.xml");
  }

  @Test
  void isValuePresent_whenNullOrEmpty() {
    Assertions.assertFalse(ImportHelper.isValuePresent(null));
    Assertions.assertFalse(ImportHelper.isValuePresent(""));
    Assertions.assertFalse(ImportHelper.isValuePresent("   "));
    Assertions.assertFalse(ImportHelper.isValuePresent(new ArrayList<>()));
    Assertions.assertFalse(ImportHelper.isValuePresent(new HashMap<>()));
    Assertions.assertFalse(ImportHelper.isValuePresent(new int[0]));
    Assertions.assertFalse(ImportHelper.isValuePresent(Optional.empty()));
  }

  @Test
  void isValuePresent_whenValidValue() {
    Assertions.assertTrue(ImportHelper.isValuePresent("value"));
    Assertions.assertTrue(ImportHelper.isValuePresent(123));
    Assertions.assertTrue(ImportHelper.isValuePresent(true));
    Assertions.assertTrue(ImportHelper.isValuePresent(Arrays.asList(1, 2, 3)));
    Map<String, String> map = new HashMap<>();
    map.put("key", "value");
    Assertions.assertTrue(ImportHelper.isValuePresent(map));
    Assertions.assertTrue(ImportHelper.isValuePresent(new int[] {1, 2, 3}));
    Assertions.assertTrue(ImportHelper.isValuePresent(Optional.of("test")));
  }

  @Test
  void isRecordPresent_whenRecordExists() {
    Assertions.assertTrue(
        ImportHelper.isRecordPresent("com.axelor.meta.db.MetaMenu", "name", "TestParentMenu"));
  }

  @Test
  void isRecordPresent_whenRecordMissing() {
    Assertions.assertFalse(
        ImportHelper.isRecordPresent("com.axelor.meta.db.MetaMenu", "name", "TestMenu"));
  }

  @Test
  void isRecordPresent_withTwoFields() {
    Assertions.assertTrue(
        ImportHelper.isRecordPresent(
            "com.axelor.meta.db.MetaMenu",
            "name, action.name",
            "TestParentMenu",
            "action.meta.model.view"));
  }

  @Test
  void isRecordPresent_withMultipleFields() {
    Assertions.assertTrue(
        ImportHelper.isRecordPresent(
            "com.axelor.meta.db.MetaMenu",
            "name, title, action.name",
            "TestParentMenu",
            "Test Parent Menu",
            "action.meta.model.view"));
  }

  @Test
  void checkUniqueConstraint_whenValidRecord() {
    Assertions.assertTrue(
        ImportHelper.checkUniqueConstraint(
            "com.axelor.meta.db.MetaMenu", "name", "TestParentMenu", "title", "Test Parent Menu"));
  }

  @Test
  @Transactional
  void checkUniqueConstraint_whenDuplicateExists() {
    createMetaMenu();
    Assertions.assertFalse(
        ImportHelper.checkUniqueConstraint(
            "com.axelor.meta.db.MetaMenu", "name", "TestChildMenu", "title", "Test Child Menu"));
  }

  @Test
  void checkUniqueConstraint_withTwoFields() {
    Assertions.assertTrue(
        ImportHelper.checkUniqueConstraint(
            "com.axelor.meta.db.MetaMenu",
            "name",
            "TestParentMenu",
            "title, action.name",
            "Test Parent Menu",
            "action.meta.model.view"));
  }

  @Test
  void checkUniqueConstraint_withMultipleFields() {
    Assertions.assertTrue(
        ImportHelper.checkUniqueConstraint(
            "com.axelor.meta.db.MetaMenu",
            "name",
            "TestChildTwoMenu",
            "title, action.name, parent.name",
            "Test Child two Menu",
            "action.meta.model.view",
            "TestChildMenu"));
  }

  @Test
  void checkSelection_withValidSingleValue() {
    Assertions.assertTrue(
        ImportHelper.checkSelection("com.axelor.dms.db.DMSPermission", "value", "READ"));
  }

  @Test
  void checkSelection_withValidCommaSeparatedValues() {
    Assertions.assertTrue(
        ImportHelper.checkSelection("com.axelor.dms.db.DMSPermission", "value", "READ,WRITE"));
  }

  @Test
  void checkSelection_withInvalidValue() {
    Assertions.assertFalse(
        ImportHelper.checkSelection("com.axelor.dms.db.DMSPermission", "value", "SPEAK"));
  }

  @Test
  void checkSelection_whenValueIsNull() {
    Assertions.assertFalse(
        ImportHelper.checkSelection("com.axelor.dms.db.DMSPermission", "value", null));
    Assertions.assertTrue(
        ImportHelper.checkSelection("com.axelor.dms.db.DMSFileTag", "style", null));
  }

  protected void createMetaMenu() {
    MetaMenu menu = JPA.all(MetaMenu.class).filter("self.name = ?", "TestChildMenu").fetchOne();
    MetaMenu copy = JPA.copy(menu, false);
    JPA.save(copy);
  }
}
