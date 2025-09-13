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
package com.axelor.utils.api;

import javax.ws.rs.core.Response;
import lombok.Getter;

@Getter
public class ResponseBody<T> {

  private final int codeStatus;
  private final String messageStatus;
  private final T object;

  public ResponseBody(Response.Status codeStatus, String messageStatus) {
    this.codeStatus = codeStatus.getStatusCode();
    this.messageStatus = messageStatus;
    this.object = null;
  }

  public ResponseBody(Response.Status codeStatus, String messageStatus, T object) {
    this.codeStatus = codeStatus.getStatusCode();
    this.messageStatus = messageStatus;
    this.object = object;
  }
}
