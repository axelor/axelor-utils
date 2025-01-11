package com.axelor.utils.context;

import com.axelor.auth.db.User;
import com.axelor.meta.loader.LoaderHelper;
import com.axelor.meta.schema.ObjectViews;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.meta.schema.actions.ActionView.ActionViewBuilder;
import com.axelor.utils.helpers.context.ActionViewHelper;
import com.axelor.utils.junit.BaseTest;
import com.axelor.utils.utils.TestingHelper;
import com.google.inject.Inject;
import javax.xml.bind.JAXBException;
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
    ActionView action =
        (ActionView)
            objectViews.getActions().stream()
                .filter(it -> it.getName().equals("test.user.action"))
                .findFirst()
                .orElseThrow();
    ActionViewBuilder builder =
        ActionViewHelper.build(User.class, "self.id IS NOT NULL", "user-grid", "user-form");
    Assertions.assertEquals(action.getTitle(), builder.get().getTitle());
    Assertions.assertEquals(action.getModel(), builder.get().getModel());
    for (int i = 0; i < 2; i++) {
      Assertions.assertEquals(
          action.getViews().get(i).getType(), builder.get().getViews().get(i).getType());
      Assertions.assertEquals(
          action.getViews().get(i).getName(), builder.get().getViews().get(i).getName());
    }
    Assertions.assertEquals(action.getDomain(), builder.get().getDomain());
  }
}
