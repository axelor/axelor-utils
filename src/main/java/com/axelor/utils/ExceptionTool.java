/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2022 Axelor (<http://axelor.com>).
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axelor.utils;

import com.axelor.i18n.I18n;
import com.axelor.rpc.ActionResponse;
import com.axelor.utils.exception.ToolExceptionMessage;
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
