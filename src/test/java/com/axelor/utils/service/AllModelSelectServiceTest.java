package com.axelor.utils.service;

import static org.junit.jupiter.api.Assertions.*;

import com.axelor.events.StartupEvent;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.MetaSelect;
import com.axelor.meta.db.MetaSelectItem;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.axelor.meta.db.repo.MetaSelectRepository;
import com.axelor.meta.loader.LoaderHelper;
import com.axelor.utils.junit.BaseTest;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AllModelSelectServiceTest extends BaseTest {

  private final MetaSelectRepository metaSelectRepo;
  private final MetaModelRepository metaModelRepo;
  private final LoaderHelper loaderHelper;
  private final AllModelSelectService allModelSelectService;

  @Inject
  AllModelSelectServiceTest(
      MetaSelectRepository metaSelectRepo,
      MetaModelRepository metaModelRepo,
      LoaderHelper loaderHelper,
      AllModelSelectService allModelSelectService) {
    this.metaSelectRepo = metaSelectRepo;
    this.metaModelRepo = metaModelRepo;
    this.loaderHelper = loaderHelper;
    this.allModelSelectService = allModelSelectService;
  }

  @BeforeEach
  void setUp() {
    loaderHelper.importCsv("data/metamodel-input.xml");
    loaderHelper.importCsv("data/metaselects-input.xml");
    loaderHelper.importCsv("data/metaselectitems-input.xml");
  }

  @Test
  @Transactional
  void testCreateSelectionOfAllModels_NewSelect() {
    List<MetaSelect> existingMetaSelect =
        metaSelectRepo.all().filter("self.name = ?1", "all.model.reference.select").fetch();
    if (existingMetaSelect != null) {
      for (MetaSelect metaSelect : existingMetaSelect) {
        metaSelectRepo.remove(metaSelect);
      }
    }
    assertNull(metaSelectRepo.findByName("all.model.reference.select"));

    allModelSelectService.createSelectionOfAllModels(new StartupEvent());

    MetaSelect metaSelectAfterAction = metaSelectRepo.findByName("all.model.reference.select");

    assertNotNull(metaSelectAfterAction);
    assertEquals(metaModelRepo.all().fetch().size(), metaSelectAfterAction.getItems().size());
  }

  @Test
  void testCreateSelectionOfAllModels_ExistingSelect() {
    allModelSelectService.createSelectionOfAllModels(new StartupEvent());

    List<MetaModel> metaModelList = metaModelRepo.all().fetch();
    MetaSelect metaSelect = metaSelectRepo.findByName("all.model.reference.select");

    assertNotNull(metaSelect);

    Set<String> metaModelNames =
        metaModelList.stream().map(MetaModel::getFullName).collect(Collectors.toSet());

    Set<String> metaSelectItemValues =
        metaSelect.getItems().stream().map(MetaSelectItem::getValue).collect(Collectors.toSet());

    assertEquals(metaModelList.size(), metaSelect.getItems().size());

    assertEquals(metaModelNames, metaSelectItemValues);
  }
}
