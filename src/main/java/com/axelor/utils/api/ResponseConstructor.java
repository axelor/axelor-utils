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

import com.axelor.db.Model;
import com.axelor.inject.Beans;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ResponseConstructor {

  private ResponseConstructor() {}

  public static <T> Response build(Response.Status statusCode, String message, T object) {
    return Response.status(statusCode)
        .type(MediaType.APPLICATION_JSON)
        .entity(new ResponseBody<>(statusCode, message, object))
        .build();
  }

  public static Response build(Response.Status statusCode, String message) {
    return Response.status(statusCode)
        .type(MediaType.APPLICATION_JSON)
        .entity(new ResponseBody<>(statusCode, message))
        .build();
  }

  public static Response build(Response.Status statusCode, Object object) {
    return Response.status(statusCode).type(MediaType.APPLICATION_JSON).entity(object).build();
  }

  public static Response buildCreateResponse(Model model) {
    return build(
        Response.Status.CREATED,
        Beans.get(ResponseMessageComputeService.class).computeCreateMessage(model));
  }

  public static Response buildCreateResponse(Model model, ResponseStructure object) {
    return build(
        Response.Status.CREATED,
        Beans.get(ResponseMessageComputeService.class).computeCreateMessage(model),
        object);
  }
}
