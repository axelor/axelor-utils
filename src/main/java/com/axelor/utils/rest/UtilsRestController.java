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
package com.axelor.utils.rest;

import com.axelor.i18n.I18n;
import com.axelor.inject.Beans;
import com.axelor.utils.api.HttpExceptionHandler;
import com.axelor.utils.api.ResponseConstructor;
import com.axelor.utils.dtos.MenuApiRequest;
import com.axelor.utils.exception.UtilsExceptionMessage;
import java.util.Collections;
import java.util.Set;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.collections.CollectionUtils;

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
   * @param request: the payload of the request
   * @return Response containing parent menus
   */
  @POST
  @Path("/parent-menus")
  @HttpExceptionHandler
  public Response getAllParentMenus(MenuApiRequest request) {
    Set<String> menus = request.getMenus();
    if (CollectionUtils.isEmpty(menus)) {
      return ResponseConstructor.build(
          Response.Status.OK,
          I18n.get(UtilsExceptionMessage.NO_MENU_PROVIDED),
          Collections.emptyList());
    }
    var parentMenus = Beans.get(UtilsMenuRestService.class).getAllParentMenus(menus);
    return ResponseConstructor.build(
        Response.Status.OK, I18n.get(UtilsExceptionMessage.RESPONSE_SUCCESS), parentMenus);
  }

  /**
   * Gets all child menus for the given menu names.
   *
   * @param request: the payload of the request
   * @return Response containing child menus
   */
  @POST
  @Path("/child-menus")
  @HttpExceptionHandler
  public Response getAllChildMenus(MenuApiRequest request) {
    Set<String> menus = request.getMenus();
    if (CollectionUtils.isEmpty(menus)) {
      return ResponseConstructor.build(
          Response.Status.OK,
          I18n.get(UtilsExceptionMessage.NO_MENU_PROVIDED),
          Collections.emptyList());
    }

    var childMenus = Beans.get(UtilsMenuRestService.class).getAllChildMenus(menus);
    return ResponseConstructor.build(
        Response.Status.OK, I18n.get(UtilsExceptionMessage.RESPONSE_SUCCESS), childMenus);
  }

  /**
   * Gets related metamodels for the given menu names.
   *
   * @param request: the payload of the request
   * @return Response containing related meta models
   */
  @POST
  @Path("/related-model")
  @HttpExceptionHandler
  public Response getRelatedMetaModel(MenuApiRequest request) {
    Set<String> menuNames = request.getMenus();
    if (menuNames == null || menuNames.isEmpty()) {
      return ResponseConstructor.build(
          Response.Status.OK,
          I18n.get(UtilsExceptionMessage.NO_MENU_PROVIDED),
          Collections.emptyList());
    }

    var relatedMetaModels = Beans.get(UtilsMenuRestService.class).getRelatedMetaModel(menuNames);
    return ResponseConstructor.build(
        Response.Status.OK, I18n.get(UtilsExceptionMessage.RESPONSE_SUCCESS), relatedMetaModels);
  }
}
