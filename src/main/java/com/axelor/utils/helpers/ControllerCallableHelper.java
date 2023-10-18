package com.axelor.utils.helpers;

import com.axelor.i18n.I18n;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionResponse;
import com.axelor.utils.exception.UtilsExceptionMessage;
import com.axelor.utils.service.AppSettingsService;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/** Helper class to call specific callable services in a controller. */
public final class ControllerCallableHelper {

  private ControllerCallableHelper() {
    throw new IllegalStateException("Cannot instantiate helper classes.");
  }

  /**
   * Run the given callable in a separate thread. display any occurring exception in the given
   * response. If the thread is not over before the timeout, send a notification to the user.
   *
   * @param callable service to be executed asynchronously
   * @param response available in a controller to be modified by the process
   * @return what is returned by the service
   */
  public static <V> V runInSeparateThread(Callable<V> callable, ActionResponse response) {
    V result = null;

    // Start thread
    Future<V> future = ForkJoinPool.commonPool().submit(callable);

    // Get timeout from application properties, 10s by default
    int processTimeout = Beans.get(AppSettingsService.class).processTimeout();

    try {
      // Wait processTimeout seconds
      result = future.get(processTimeout, TimeUnit.SECONDS);
    } catch (ExecutionException e) {
      // cause already traced in traceback
      response.setInfo(e.getCause().getMessage());
    } catch (TimeoutException e) {
      response.setNotify(I18n.get(UtilsExceptionMessage.PROCESS_BEING_COMPUTED));
    } catch (InterruptedException e) {
      ExceptionHelper.trace(e);
      Thread.currentThread().interrupt();
    }
    return result;
  }
}
