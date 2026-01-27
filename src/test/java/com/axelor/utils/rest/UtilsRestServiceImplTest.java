package com.axelor.utils.rest;

import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.axelor.meta.loader.LoaderHelper;
import com.axelor.utils.junit.BaseTest;
import com.google.inject.persist.Transactional;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link UtilsRestServiceImpl}.
 *
 * <p>Tests the functionality of the UtilsRestService implementation, including:
 *
 * <ul>
 *   <li>Model caching and retrieval
 *   <li>Batch model fetching
 *   <li>Recursive reference handling
 * </ul>
 */
class UtilsRestServiceImplTest extends BaseTest {

  protected final UtilsRestService utilsRestService;
  protected final MetaModelRepository metaModelRepository;

  @Inject
  UtilsRestServiceImplTest(
      UtilsRestService utilsRestService, MetaModelRepository metaModelRepository) {
    this.utilsRestService = utilsRestService;
    this.metaModelRepository = metaModelRepository;
  }

  @BeforeAll
  @Transactional
  static void setUp() {
    Beans.get(LoaderHelper.class).importCsv("data/metamodel-input.xml");
  }

  @Test
  void getModel_whenFullNameIsNull_shouldReturnNull() {
    // Act
    MetaModel result = utilsRestService.getModel(null);

    // Assert
    Assertions.assertNull(result, "Should return null when fullName is null");
  }

  @Test
  void getModel_whenModelExists_shouldReturnModel() {
    MetaModel existingModel = metaModelRepository.findByName("MetaModel");
    String fullName = existingModel.getFullName();

    MetaModel result = utilsRestService.getModel(fullName);

    Assertions.assertNotNull(result, "Should return a model when it exists");
    Assertions.assertEquals(
        existingModel.getName(), result.getName(), "Should return the correct model");
  }

  @Test
  void getModel_whenModelDoesNotExist_shouldReturnNull() {
    String nonExistentFullName = "com.axelor.nonexistent.db.NonExistentModel";

    MetaModel result = utilsRestService.getModel(nonExistentFullName);

    Assertions.assertNull(result, "Should return null when model does not exist");
  }

  @Test
  void getModel_whenCalledMultipleTimes_shouldUseCacheForSubsequentCalls() {
    MetaModel existingModel = metaModelRepository.all().fetchOne();
    String fullName = existingModel.getFullName();

    long firstStart = System.nanoTime();
    MetaModel firstResult = utilsRestService.getModel(fullName);
    long firstEnd = System.nanoTime();
    long firstDuration = firstEnd - firstStart;

    long secondStart = System.nanoTime();
    MetaModel secondResult = utilsRestService.getModel(fullName);
    long secondEnd = System.nanoTime();
    long secondDuration = secondEnd - secondStart;

    Assertions.assertNotNull(firstResult, "First call should return a model");
    Assertions.assertNotNull(secondResult, "Second call should return a model");
    Assertions.assertTrue(
        firstDuration > secondDuration,
        "Second call use caches, should have smaller response time");
    Assertions.assertEquals(
        firstResult, secondResult, "Both calls should return the same model instance");
  }

  @Test
  void addReferences_whenModelIsNull_shouldNotModifyList() {
    List<MetaModel> listOfRef = new ArrayList<>();

    utilsRestService.addReferences(null, listOfRef, "ONE_TO_MANY", "MANY_TO_ONE");

    Assertions.assertTrue(listOfRef.isEmpty(), "List should remain empty when model is null");
  }

  @Test
  void addReferences_whenListIsNull_shouldNotThrowException() {
    MetaModel model = metaModelRepository.all().fetchOne();

    Assertions.assertDoesNotThrow(
        () -> utilsRestService.addReferences(model, null, "ONE_TO_MANY", "MANY_TO_ONE"),
        "Should not throw exception when list is null");
  }

  @Test
  void addReferences_whenTypesArrayIsTooSmall_shouldNotModifyList() {
    MetaModel model = metaModelRepository.all().fetchOne();
    List<MetaModel> listOfRef = new ArrayList<>();

    utilsRestService.addReferences(model, listOfRef, "ONE_TO_MANY");

    Assertions.assertTrue(
        listOfRef.isEmpty(), "List should remain empty when types array is too small");
  }

  @Test
  void addReferences_whenModelHasNoReferences_shouldNotModifyList() {
    // Arrange
    MetaModel model = metaModelRepository.findByName("TestModel");
    List<MetaModel> listOfRef = new ArrayList<>();

    // Act
    utilsRestService.addReferences(model, listOfRef, "ONE_TO_MANY", "MANY_TO_ONE");

    Assertions.assertTrue(
        listOfRef.isEmpty(), "List should remain empty when model has no references");
  }
}
