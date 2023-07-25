package com.axelor.utils.action;

import com.axelor.auth.db.User;
import com.axelor.auth.db.repo.UserRepository;
import com.axelor.meta.loader.LoaderHelper;
import com.axelor.utils.junit.BaseTest;
import com.axelor.utils.service.ActionService;
import com.google.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ActionServiceTest extends BaseTest {
  protected final LoaderHelper loaderHelper;
  protected final ActionService actionService;
  protected final UserRepository userRepository;

  @Inject
  public ActionServiceTest(
      LoaderHelper loaderHelper, ActionService actionService, UserRepository userRepository) {
    this.actionService = actionService;
    this.userRepository = userRepository;
    this.loaderHelper = loaderHelper;
  }

  @BeforeEach
  void setUp() {
    loaderHelper.importCsv("data/groups-input.xml");
    loaderHelper.importCsv("data/users-input.xml");
    loaderHelper.loadViewFile("views/Actions.xml");
  }

  @Test
  public void modifyUserCode() {
    // Null object
    actionService.applyActions("test.user.action.modify.code.to.name", null);
    var user = userRepository.find(1L);

    // No action,
    user = (User) actionService.applyActions("", user);
    var user2 = userRepository.find(2L);
    Assertions.assertNotEquals(user.getCode(), user2.getCode());
    // Same action for 2 different users
    user = (User) actionService.applyActions("test.user.action.modify.code.to.test", user);
    user2 = (User) actionService.applyActions("test.user.action.modify.code.to.test", user2);
    Assertions.assertEquals(user.getCode(), user2.getCode());
    actionService.applyActions("test.user.action.modify.code.to.name", user);
    actionService.applyActions("test.user.action.modify.code.to.name", user2);
    Assertions.assertNotEquals(user.getCode(), user2.getCode());
    // Multiple actions
    user =
        (User)
            actionService.applyActions(
                "test.user.action.modify.code.to.test,test.user.action.modify.name.to.test", user);
    user2 =
        (User)
            actionService.applyActions(
                "test.user.action.modify.code.to.test,test.user.action.modify.name.to.test", user2);
    Assertions.assertEquals(user.getCode(), user2.getCode());
    Assertions.assertEquals(user.getName(), user2.getName());
    user = (User) actionService.applyActions("test.user.action.set.name.from.group", user);
    Assertions.assertEquals(user.getName(), user.getGroup().getName());
    user = userRepository.find(3L);
    user.setGroup(null);
    user = (User) actionService.applyActions("test.user.action.set.name.from.group", user);
    Assertions.assertEquals(user.getName(), "Supplier");
    // Non existing action
    user = (User) actionService.applyActions("test.not.existing.action", user);
  }
}
