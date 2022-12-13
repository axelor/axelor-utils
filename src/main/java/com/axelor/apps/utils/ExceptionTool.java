package com.axelor.apps.utils;

import com.axelor.apps.utils.exception.ToolExceptionMessage;
import com.axelor.i18n.I18n;
import com.axelor.rpc.ActionResponse;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionTool {

  protected static final Logger log = LoggerFactory.getLogger(ExceptionTool.class);

  private ExceptionTool() {
    throw new IllegalStateException("Cannot instantiate utility classes.");
  }

  /**
   * Traces an exception corresponding to a bug in the logs and displays the exception message on
   * the screen via a popup.
   *
   * @param response the ActionResponse to send the error message to.
   * @param e the Throwable to log.
   */
  public static void trace(@Nonnull ActionResponse response, @Nonnull Throwable e) {
    log.error(e.getMessage(), e);
    response.setError(
        String.format(I18n.get(ToolExceptionMessage.EXCEPTION_OCCURRED), e.getMessage()));
  }
}
