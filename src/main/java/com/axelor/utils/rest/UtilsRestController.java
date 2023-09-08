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

import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaAction;
import com.axelor.meta.db.MetaMenu;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaMenuRepository;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.axelor.utils.api.HttpExceptionHandler;
import com.axelor.utils.api.ResponseConstructor;
import com.axelor.utils.rest.dto.MenuListResponse;
import com.axelor.utils.rest.dto.ModelListResponse;
import com.axelor.utils.rest.dto.ModelResponse;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/fetch")
@Produces(MediaType.APPLICATION_JSON)
public class UtilsRestController {

  @GET
  @Path("/parent-menus/{menu}")
  @HttpExceptionHandler
  public Response getAllParentMenus(@PathParam("menu") String menuName) {
    List<MetaMenu> parentMenus = new ArrayList<>();
    MetaMenu child = Beans.get(MetaMenuRepository.class).findByName(menuName);
    MetaMenu parent = child.getParent();
    while (parent != null) {
      parentMenus.add(parent);
      parent = parent.getParent();
    }
    return ResponseConstructor.build(
        Response.Status.OK,
        "Request successfully completed",
        new MenuListResponse(parentMenus, child));
  }

  @GET
  @Path("/child-menus/{menu}")
  @HttpExceptionHandler
  public Response getAllChildMenus(@PathParam("menu") String menuName) {
    List<MetaMenu> childMenus = new ArrayList<>();
    MetaMenu parent = Beans.get(MetaMenuRepository.class).findByName(menuName);
    if (parent != null) {
      childMenus.addAll(
          Beans.get(MetaMenuRepository.class).all().filter("self.parent = ? ", parent).fetch());
    }

    return ResponseConstructor.build(
        Response.Status.OK,
        "Request successfully completed",
        new MenuListResponse(childMenus, parent));
  }

  @GET
  @Path("/related-model/{menu}")
  @HttpExceptionHandler
  public Response getRelatedMetaModel(@PathParam("menu") String menuName) {
    MetaMenu menu = Beans.get(MetaMenuRepository.class).findByName(menuName);
    MetaAction view = menu.getAction();

    return ResponseConstructor.build(
        Response.Status.OK,
        "Request successfully completed",
        new ModelResponse(Beans.get(UtilsRestService.class).getModel(view.getModel())));
  }

  @GET
  @Path("/direct-references/{id}")
  @HttpExceptionHandler
  public Response getDirectReferences(@PathParam("id") Long modelId) {
    MetaModel model = Beans.get(MetaModelRepository.class).find(modelId);
    List<MetaModel> listOfRef = new ArrayList<>();
    Beans.get(UtilsRestService.class).addReferences(model, listOfRef, "ONE_TO_ONE", "ONE_TO_MANY");

    return ResponseConstructor.build(
        Response.Status.OK,
        "Request successfully completed",
        new ModelListResponse(listOfRef, model));
  }

  @GET
  @Path("/indirect-references/{id}")
  @HttpExceptionHandler
  public Response getIndirectReferences(@PathParam("id") Long modelId) {
    MetaModel model = Beans.get(MetaModelRepository.class).find(modelId);
    List<MetaModel> listOfRef = new ArrayList<>();
    Beans.get(UtilsRestService.class)
        .addReferences(model, listOfRef, "MANY_TO_ONE", "MANY_TO_MANY");

    return ResponseConstructor.build(
        Response.Status.OK,
        "Request successfully completed",
        new ModelListResponse(listOfRef, model));
  }
}
