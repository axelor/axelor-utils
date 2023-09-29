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

import com.axelor.common.StringUtils;
import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaAction;
import com.axelor.meta.db.MetaMenu;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaMenuRepository;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.axelor.utils.api.HttpExceptionHandler;
import com.axelor.utils.api.ResponseConstructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.shiro.util.CollectionUtils;

@Path("/fetch")
@Produces(MediaType.APPLICATION_JSON)
public class UtilsRestController {

  private static final String RESPONSE_SUCCESS = "Request successfully completed";

  private Set<String> split(String menuNames) {
    return menuNames == null ? new HashSet<>() : Set.of(menuNames.split(","));
  }

  private Set<MetaMenu> getAllParents(
      MetaMenuRepository metaMenuRepository, Set<String> menuNames) {
    var parentMenus =
        metaMenuRepository.all().filter("self.name in (:names)").bind("names", menuNames).fetch()
            .stream()
            .map(MetaMenu::getParent)
            .collect(Collectors.toSet());
    if (CollectionUtils.isEmpty(parentMenus) || parentMenus.stream().allMatch(Objects::isNull)) {
      return new HashSet<>();
    }
    parentMenus.addAll(
        getAllParents(
            metaMenuRepository,
            parentMenus.stream().map(MetaMenu::getName).collect(Collectors.toSet())));
    return parentMenus;
  }

  @GET
  @Path("/parent-menus")
  @HttpExceptionHandler
  public Response getAllParentMenus(@QueryParam("menus") String menuNames) {
    var menuRepo = Beans.get(MetaMenuRepository.class);
    var parentMenus =
        StringUtils.notBlank(menuNames)
            ? getAllParents(menuRepo, split(menuNames))
            : new HashSet<MetaMenu>();

    return ResponseConstructor.build(Response.Status.OK, RESPONSE_SUCCESS, parentMenus);
  }

  public Set<MetaMenu> getAllChildren(
      MetaMenuRepository metaMenuRepository, Set<String> menuNames) {
    var childMenus =
        new HashSet<>(
            metaMenuRepository
                .all()
                .filter("self.parent.name in (:names) ")
                .bind("names", menuNames)
                .fetch());
    if (CollectionUtils.isEmpty(childMenus) || childMenus.stream().allMatch(Objects::isNull)) {
      return new HashSet<>();
    }
    childMenus.addAll(
        getAllChildren(
            metaMenuRepository,
            childMenus.stream().map(MetaMenu::getName).collect(Collectors.toSet())));
    return childMenus;
  }

  @GET
  @Path("/child-menus")
  @HttpExceptionHandler
  public Response getAllChildMenus(@QueryParam("menus") String menuNames) {
    var menuRepo = Beans.get(MetaMenuRepository.class);
    var childMenus =
        StringUtils.notBlank(menuNames)
            ? getAllChildren(menuRepo, split(menuNames))
            : new HashSet<MetaMenu>();

    return ResponseConstructor.build(Response.Status.OK, RESPONSE_SUCCESS, childMenus);
  }

  @GET
  @Path("/related-model")
  @HttpExceptionHandler
  public Response getRelatedMetaModel(@QueryParam("menus") String menuNames) {
    var map = new HashMap<String, MetaModel>();
    var menuRepo = Beans.get(MetaMenuRepository.class);
    var utilsRestService = Beans.get(UtilsRestService.class);

    for (String menuName : split(menuNames)) {
      MetaMenu menu = menuRepo.findByName(menuName);
      MetaAction view = menu.getAction();
      if (view != null && view.getModel() != null) {
        map.put(menuName, utilsRestService.getModel(view.getModel()));
      }
    }

    return ResponseConstructor.build(Response.Status.OK, RESPONSE_SUCCESS, map);
  }

  @GET
  @Path("/direct-references/{id}")
  @HttpExceptionHandler
  public Response getDirectReferences(@PathParam("id") Long modelId) {
    MetaModel model = Beans.get(MetaModelRepository.class).find(modelId);
    List<MetaModel> listOfRef = new ArrayList<>();
    Beans.get(UtilsRestService.class).addReferences(model, listOfRef, "ONE_TO_ONE", "ONE_TO_MANY");

    return ResponseConstructor.build(Response.Status.OK, RESPONSE_SUCCESS, listOfRef);
  }

  @GET
  @Path("/indirect-references/{id}")
  @HttpExceptionHandler
  public Response getIndirectReferences(@PathParam("id") Long modelId) {
    MetaModel model = Beans.get(MetaModelRepository.class).find(modelId);
    List<MetaModel> listOfRef = new ArrayList<>();
    Beans.get(UtilsRestService.class)
        .addReferences(model, listOfRef, "MANY_TO_ONE", "MANY_TO_MANY");

    return ResponseConstructor.build(Response.Status.OK, RESPONSE_SUCCESS, listOfRef);
  }
}
