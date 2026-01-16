/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2026 Axelor (<http://axelor.com>).
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
package com.axelor.utils.rest;

import com.axelor.i18n.I18n;
import com.axelor.inject.Beans;
import com.axelor.utils.api.HttpExceptionHandler;
import com.axelor.utils.api.ResponseConstructor;
import com.axelor.utils.exception.UtilsExceptionMessage;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST controller for utility operations. Provides endpoints for retrieving parent and child menus,
 * and related meta models.
 */
@Path("/fetch")
@Produces(MediaType.APPLICATION_JSON)
public class UtilsRestController {

  /**
   * Gets all parent menus for the given menu names.
   *
   * @param menuNames comma-separated list of menu names
   * @return Response containing parent menus
   */
  @GET
  @Path("/parent-menus")
  @HttpExceptionHandler
  public Response getAllParentMenus(@QueryParam("menus") String menuNames) {
    var parentMenus = Beans.get(UtilsMenuRestService.class).getAllParentMenus(menuNames);
    return ResponseConstructor.build(
        Response.Status.OK, I18n.get(UtilsExceptionMessage.RESPONSE_SUCCESS), parentMenus);
  }

  /**
   * Gets all child menus for the given menu names.
   *
   * @param menuNames comma-separated list of menu names
   * @return Response containing child menus
   */
  @GET
  @Path("/child-menus")
  @HttpExceptionHandler
  public Response getAllChildMenus(@QueryParam("menus") String menuNames) {
    var childMenus = Beans.get(UtilsMenuRestService.class).getAllChildMenus(menuNames);
    return ResponseConstructor.build(
        Response.Status.OK, I18n.get(UtilsExceptionMessage.RESPONSE_SUCCESS), childMenus);
  }

  /**
   * Gets related meta models for the given menu names.
   *
   * @param menuNames comma-separated list of menu names
   * @return Response containing related meta models
   */
  @GET
  @Path("/related-model")
  @HttpExceptionHandler
  public Response getRelatedMetaModel(@QueryParam("menus") String menuNames) {
    var relatedMetaModels = Beans.get(UtilsMenuRestService.class).getRelatedMetaModel(menuNames);
    return ResponseConstructor.build(
        Response.Status.OK, I18n.get(UtilsExceptionMessage.RESPONSE_SUCCESS), relatedMetaModels);
  }
}
