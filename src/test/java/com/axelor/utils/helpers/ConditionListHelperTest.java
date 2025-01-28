package com.axelor.utils.helpers;

import com.axelor.auth.db.User;
import com.axelor.db.Query;
import com.axelor.meta.loader.LoaderHelper;
import com.axelor.meta.service.MetaModelService;
import com.axelor.utils.db.Move;
import com.axelor.utils.db.MoveLine;
import com.axelor.utils.junit.BaseTest;
import com.google.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConditionListHelperTest extends BaseTest {

  protected final LoaderHelper loaderHelper;
  protected final MetaModelService metaModelService;

  @Inject
  public ConditionListHelperTest(LoaderHelper loaderHelper, MetaModelService metaModelService) {
    this.loaderHelper = loaderHelper;
    this.metaModelService = metaModelService;
  }

  @BeforeEach
  void setUp() {
    loaderHelper.importCsv("data/users-input.xml");
    loaderHelper.importCsv("data/moves-input.xml");
    loaderHelper.importCsv("data/move-lines-input.xml");
    metaModelService.process(User.class);
    metaModelService.process(Move.class);
    metaModelService.process(MoveLine.class);
  }

  @Test
  void html() {
    var cl = ConditionList.html();
    Assertions.assertNotNull(cl);
    Assertions.assertTrue(cl.isEmpty());
  }

  @Test
  void plain() {
    var cl = ConditionList.plain();
    Assertions.assertNotNull(cl);
    Assertions.assertTrue(cl.isEmpty());
  }

  @Test
  void checkRequiredFields_withNotValidMove() {
    var move = new Move();
    Assertions.assertThrows(
        IllegalStateException.class,
        () -> ConditionList.checkRequiredFields(move, ConditionList::plain));
  }

  @Test
  void checkRequiredFields_withValidMove() {
    var move = new Move();
    move.setCode("code");
    Assertions.assertDoesNotThrow(
        () -> ConditionList.checkRequiredFields(move, ConditionList::plain));
  }

  @Test
  void checkReferences_withMoveLine() {
    var moveLine = Query.of(MoveLine.class).fetchOne();
    Assertions.assertThrows(
        IllegalStateException.class,
        () -> ConditionList.checkReferences(moveLine, ConditionList::plain));
  }

  @Test
  void checkReferences_withUser() {
    var move = Query.of(User.class).fetchOne();
    Assertions.assertDoesNotThrow(() -> ConditionList.checkReferences(move, ConditionList::plain));
  }
}
