package com.axelor.utils.rest;

import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaMenu;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaMenuRepository;
import com.axelor.meta.loader.LoaderHelper;
import com.axelor.utils.helpers.json.JsonHelper;
import com.axelor.utils.junit.BaseTest;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class UtilsMenuRestServiceImplTest extends BaseTest {

  protected final UtilsMenuRestService utilsMenuRestService;
  protected final MetaMenuRepository metaMenuRepository;

  @Inject
  UtilsMenuRestServiceImplTest(
      UtilsMenuRestService utilsMenuRestService, MetaMenuRepository metaMenuRepository) {
    this.utilsMenuRestService = utilsMenuRestService;
    this.metaMenuRepository = metaMenuRepository;
  }

  @BeforeAll
  @Transactional
  static void setUp() {
    LoaderHelper loaderHelper = Beans.get(LoaderHelper.class);

    loaderHelper.importCsv("data/metamodel-input.xml");
    loaderHelper.importCsv("data/meta-actions-input.xml");
    loaderHelper.importCsv("data/meta-menus-input.xml");
  }

  @Test
  void getAllParentMenus_whenMenuNamesIsNull_shouldReturnEmptySet() {
    Set<MetaMenu> result = utilsMenuRestService.getAllParentMenus(null);

    Assertions.assertNotNull(result, "The Result should not be null");
    Assertions.assertTrue(result.isEmpty(), "The Result should be an empty set");
  }

  @Test
  void getAllParentMenus_whenMenuNamesIsEmpty_shouldReturnEmptySet() {
    Set<MetaMenu> result = utilsMenuRestService.getAllParentMenus(Collections.emptySet());

    Assertions.assertNotNull(result, "The Result should not be null");
    Assertions.assertTrue(result.isEmpty(), "The Result should be an empty set");
  }

  @Test
  void getAllParentMenus_whenMenuExists_shouldReturnParentMenus() {
    MetaMenu childMenu = metaMenuRepository.findByName("TestChildTwoMenu");
    if (childMenu == null) {
      Assertions.fail("Test requires at least one menu with a parent");
    }

    Set<MetaMenu> result = utilsMenuRestService.getAllParentMenus(Set.of(childMenu.getName()));

    Assertions.assertNotNull(result, "The Result should not be null");
    Assertions.assertFalse(result.isEmpty(), "The Result should not be empty");
    Assertions.assertEquals(2, result.size(), "The Result should contain two parent menus");
  }

  @Test
  void getAllChildMenus_whenMenuNamesIsNull_shouldReturnEmptySet() {
    Set<MetaMenu> result = utilsMenuRestService.getAllChildMenus(null);

    Assertions.assertNotNull(result, "The Result should not be null");
    Assertions.assertTrue(result.isEmpty(), "The Result should be an empty set");
  }

  @Test
  void getAllChildMenus_whenMenuNamesIsEmpty_shouldReturnEmptySet() {
    Set<MetaMenu> result = utilsMenuRestService.getAllChildMenus(Collections.emptySet());

    Assertions.assertNotNull(result, "The Result should not be null");
    Assertions.assertTrue(result.isEmpty(), "The Result should be an empty set");
  }

  @Test
  void getAllChildMenus_whenMenuExists_shouldReturnChildMenus() {
    MetaMenu parentMenu = metaMenuRepository.findByName("TestParentMenu");
    if (parentMenu == null) {
      Assertions.fail("Test requires at least one menu with children");
    }

    Set<MetaMenu> result = utilsMenuRestService.getAllChildMenus(Set.of(parentMenu.getName()));

    Assertions.assertNotNull(result, "The Result should not be null");
    Assertions.assertFalse(result.isEmpty(), "The Result should not be empty");
    Assertions.assertEquals(2, result.size(), "The Result should contain two child menus");
  }

  @Test
  void getRelatedMetaModel_whenMenuNamesIsNull_shouldReturnEmptyMap() {
    Map<String, Object> result = utilsMenuRestService.getRelatedMetaModel(null);

    Assertions.assertNotNull(result, "The Result should not be null");
    Assertions.assertTrue(result.isEmpty(), "The Result should be an empty map");
  }

  @Test
  void getRelatedMetaModel_whenMenuNamesIsEmpty_shouldReturnEmptyMap() {
    Map<String, Object> result = utilsMenuRestService.getRelatedMetaModel(Collections.emptySet());

    Assertions.assertNotNull(result, "The Result should not be null");
    Assertions.assertTrue(result.isEmpty(), "The Result should be an empty map");
  }

  @Test
  void getRelatedMetaModel_whenMenuExists_shouldReturnRelatedMetaModels() {
    MetaMenu menu = metaMenuRepository.all().fetchOne();
    if (menu == null) {
      Assertions.fail("Test requires at least one menu");
    }

    Map<String, Object> result = utilsMenuRestService.getRelatedMetaModel(Set.of(menu.getName()));

    Assertions.assertNotNull(result, "The Result should not be null");
    Assertions.assertEquals(
        1,
        JsonHelper.getMapper()
            .convertValue(result.get(menu.getName()), Result.class)
            .getDirectReferences()
            .size(),
        "Direct references size should be equal to 1");
  }

  @Getter
  @Setter
  protected static class Result {
    MetaModel model;
    List<MetaModel> directReferences;
    List<MetaModel> indirectReferences;
  }
}
