/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2025 Axelor (<http://axelor.com>).
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
package com.axelor.utils.helpers;

import com.axelor.rpc.ActionResponse;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionHelper {

  protected static final Logger log = LoggerFactory.getLogger(ExceptionHelper.class);

  private ExceptionHelper() {
    throw new IllegalStateException("Cannot instantiate utility classes.");
  }

  /**
   * Deprecated in favor of {@link #error(ActionResponse, Throwable)}.
   *
   * @param response the ActionResponse to send the error message to.
   * @param e the Throwable to log.
   */
  @Deprecated
  public static void trace(@Nonnull ActionResponse response, @Nonnull Throwable e) {
    error(response, e);
  }

  /**
   * Traces an exception corresponding to a bug in the logs and displays the exception message on
   * the screen via a popup.
   *
   * @param response the ActionResponse to send the error message to.
   * @param e the Throwable to log.
   */
  public static void error(@Nonnull ActionResponse response, @Nonnull Throwable e) {
    log.error(e.getMessage(), e);
    response.setError(e.getMessage());
  }

  /**
   * Deprecated in favor of {@link #error(ActionResponse, String)}.
   *
   * @param response the ActionResponse to send the error message to.
   * @param message the message to display.
   */
  @Deprecated
  public static void trace(@Nonnull ActionResponse response, @Nonnull String message) {
    error(response, message);
  }

  /**
   * Displays the exception message on the screen via a popup.
   *
   * @param response the ActionResponse to send the error message to.
   * @param message the message to display.
   */
  public static void error(@Nonnull ActionResponse response, @Nonnull String message) {
    response.setError(message);
  }

  /**
   * Deprecated in favor of {@link #error(String, Throwable)}.
   *
   * @param message the message to log.
   * @param e the Throwable to log.
   */
  @Deprecated
  public static void trace(@Nonnull String message, @Nonnull Throwable e) {
    error(message, e);
  }

  /**
   * Traces an exception with a specific message in the logs.
   *
   * @param message the message to log.
   * @param e the Throwable to log.
   */
  public static void error(@Nonnull String message, @Nonnull Throwable e) {
    log.error(message, e);
  }

  /**
   * Deprecated in favor of {@link #error(Throwable)}.
   *
   * @param e the Throwable to log.
   */
  @Deprecated
  public static void trace(@Nonnull Throwable e) {
    error(e);
  }

  /**
   * Traces an exception corresponding to a bug in the logs.
   *
   * @param e the Throwable to log.
   */
  public static void error(@Nonnull Throwable e) {
    log.error(e.getMessage(), e);
  }
}
