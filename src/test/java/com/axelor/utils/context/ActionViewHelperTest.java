package com.axelor.utils.context;

import com.axelor.auth.db.User;
import com.axelor.meta.loader.LoaderHelper;
import com.axelor.meta.schema.ObjectViews;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.utils.helpers.context.ActionViewHelper;
import com.axelor.utils.junit.BaseTest;
import com.axelor.utils.utils.TestingHelper;
import jakarta.inject.Inject;
import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActionViewHelperTest extends BaseTest {
  protected final TestingHelper testingHelper;
  protected final LoaderHelper loaderHelper;

  @Inject
  public ActionViewHelperTest(TestingHelper testingHelper, LoaderHelper loaderHelper) {
    this.testingHelper = testingHelper;
    this.loaderHelper = loaderHelper;
  }

  @BeforeEach
  void before() {
    loaderHelper.loadViewFile("views/User.xml");
    loaderHelper.loadViewFile("views/Actions.xml");
  }

  @Test
  void build() throws JAXBException {
    ObjectViews objectViews = testingHelper.unmarshal("views/Actions.xml", ObjectViews.class);
    ActionView expectedAction =
        (ActionView)
            objectViews.getActions().stream()
                .filter(it -> it.getName().equals("test.user.action"))
                .findFirst()
                .orElseThrow();

    ActionView actualAction =
        ActionViewHelper.build(User.class, "self.id IS NOT NULL", "user-grid", "user-form").get();

    Assertions.assertEquals(expectedAction.getTitle(), actualAction.getTitle());
    Assertions.assertEquals(expectedAction.getModel(), actualAction.getModel());

    for (int i = 0; i < 2; i++) {
      ActionView.View expectedView = expectedAction.getViews().get(i);
      ActionView.View actualView = actualAction.getViews().get(i);
      Assertions.assertEquals(expectedView.getType(), actualView.getType());
      Assertions.assertEquals(expectedView.getName(), actualView.getName());
    }

    Assertions.assertEquals(expectedAction.getDomain(), actualAction.getDomain());
  }
}
