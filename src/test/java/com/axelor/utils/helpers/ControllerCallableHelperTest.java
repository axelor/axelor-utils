package com.axelor.utils.helpers;

import com.axelor.inject.Beans;
import com.axelor.rpc.ActionResponse;
import com.axelor.utils.junit.BaseTest;
import com.axelor.utils.service.AppSettingsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ControllerCallableHelperTest extends BaseTest {
  @Test
  public void runInSeparateThread_withSimpleProcess() {
    var result = ControllerCallableHelper.runInSeparateThread(() -> 1, new ActionResponse());
    Assertions.assertEquals(1, result);
  }

  @Test
  public void runInSeparateThread_withLongProcess() {
    var timeout = Beans.get(AppSettingsService.class).processTimeout();
    var result =
        ControllerCallableHelper.runInSeparateThread(
            () -> {
              Thread.sleep(timeout * 1000L + 1000);
              return 1;
            },
            new ActionResponse());
    Assertions.assertNull(result);
  }
}
