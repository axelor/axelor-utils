package com.axelor.utils;

import com.axelor.auth.db.User;
import com.axelor.auth.db.repo.UserRepository;
import com.axelor.db.JpaRepository;
import com.axelor.db.mapper.Mapper;
import com.axelor.meta.loader.LoaderHelper;
import com.axelor.rpc.Context;
import com.axelor.utils.db.Move;
import com.axelor.utils.db.MoveLine;
import com.axelor.utils.helpers.ContextHelper;
import com.axelor.utils.junit.BaseTest;
import com.google.inject.Inject;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContextHelperTest extends BaseTest {

  protected final LoaderHelper loaderHelper;
  protected final UserRepository userRepository;

  @Inject
  ContextHelperTest(LoaderHelper loaderHelper, UserRepository userRepository) {
    this.loaderHelper = loaderHelper;
    this.userRepository = userRepository;
  }

  @BeforeEach
  void setUp() {
    loaderHelper.importCsv("data/users-input.xml");
    loaderHelper.importCsv("data/moves-input.xml");
    loaderHelper.importCsv("data/move-lines-input.xml");
  }

  @Test
  void getFieldFromContextParent_whenObjectInContext() {
    Move move = JpaRepository.of(Move.class).find(1L);
    Map<String, Object> moveValues = Mapper.toMap(move);
    Context moveCtx = new Context(moveValues, Move.class);
    String code = ContextHelper.getFieldFromContextParent(moveCtx, "code", String.class);

    Assertions.assertEquals(code, move.getCode());
  }

  @Test
  void getFieldFromContextParent_whenObjectInParentContext() {
    Move move = JpaRepository.of(Move.class).find(1L);
    Map<String, Object> moveValues = Mapper.toMap(move);
    Context moveCtx = new Context(moveValues, Move.class);
    moveCtx.put("_model", Move.class.getName());

    Context moveLineCtx = new Context(2L, MoveLine.class);
    moveLineCtx.put("_parent", moveCtx);
    String code = ContextHelper.getFieldFromContextParent(moveLineCtx, "code", String.class);

    Assertions.assertEquals(code, move.getCode());
  }

  @Test
  void getFieldFromContextParent_whenObjectInParentParentContext() {
    User user = userRepository.findByCode("admin");
    Map<String, Object> userValues = Mapper.toMap(user);
    Context userCtx = new Context(userValues, User.class);
    userCtx.put("_model", User.class.getName());

    Move move = JpaRepository.of(Move.class).find(1L);
    Map<String, Object> moveValues = Mapper.toMap(move);
    Context moveCtx = new Context(moveValues, Move.class);
    moveCtx.put("_model", Move.class.getName());
    moveCtx.put("_parent", userCtx);

    Context moveLineCtx = new Context(2L, MoveLine.class);
    moveLineCtx.put("_parent", moveCtx);
    String password =
        ContextHelper.getFieldFromContextParent(moveLineCtx, "password", String.class);

    Assertions.assertEquals(password, user.getPassword());
  }
}
