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
import com.axelor.meta.db.MetaAction;
import com.axelor.meta.db.MetaMenu;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaMenuRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class UtilsMenuRestServiceImpl implements UtilsMenuRestService {

  protected final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  protected final MetaMenuRepository metaMenuRepository;
  protected final UtilsRestService utilsRestService;

  @Inject
  public UtilsMenuRestServiceImpl(
      MetaMenuRepository metaMenuRepository, UtilsRestService utilsRestService) {
    this.metaMenuRepository = metaMenuRepository;
    this.utilsRestService = utilsRestService;
  }

  @Override
  public Set<MetaMenu> getAllParentMenus(String menuNames) {
    if (StringUtils.isBlank(menuNames)) {
      return Collections.emptySet();
    }

    return getAllParentsRecursive(split(menuNames));
  }

  protected Set<MetaMenu> getAllParentsRecursive(Set<String> menuNames) {
    if (menuNames.isEmpty()) {
      logger.warn("No menu names provided");
      return Collections.emptySet();
    }

    List<MetaMenu> menus =
        metaMenuRepository.all().filter("self.name in (:names)").bind("names", menuNames).fetch();

    Set<MetaMenu> parentMenus =
        menus.stream()
            .map(MetaMenu::getParent)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

    if (parentMenus.isEmpty()) {
      logger.warn("No parent menus found for menu names: {}", menuNames);
      return Collections.emptySet();
    }

    Set<String> parentNames =
        parentMenus.stream().map(MetaMenu::getName).collect(Collectors.toSet());

    parentMenus.addAll(getAllParentsRecursive(parentNames));

    return parentMenus;
  }

  @Override
  public Set<MetaMenu> getAllChildMenus(String menuNames) {
    if (StringUtils.isBlank(menuNames)) {
      return Collections.emptySet();
    }

    return getAllChildrenRecursive(split(menuNames));
  }

  @Override
  public Map<String, Object> getRelatedMetaModel(String menuNames) {
    if (StringUtils.isBlank(menuNames)) {
      return Collections.emptyMap();
    }

    return split(menuNames).stream()
        .map(menuName -> Map.entry(menuName, getRelatedModel(menuName)))
        .filter(entry -> !entry.getValue().isEmpty())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  protected Set<MetaMenu> getAllChildrenRecursive(Set<String> menuNames) {
    if (menuNames.isEmpty()) {
      return Collections.emptySet();
    }

    Set<MetaMenu> childMenus =
        metaMenuRepository
            .all()
            .filter("self.parent.name in (:names)")
            .bind("names", menuNames)
            .fetchStream()
            .collect(Collectors.toSet());

    if (childMenus.isEmpty()) {
      logger.warn("No child menus found for menu names: {}", menuNames);
      return Collections.emptySet();
    }

    Set<String> childNames = childMenus.stream().map(MetaMenu::getName).collect(Collectors.toSet());

    childMenus.addAll(getAllChildrenRecursive(childNames));

    return childMenus;
  }

  protected Map<String, Object> getRelatedModel(String menuName) {
    MetaMenu menu = metaMenuRepository.findByName(menuName);

    if (menu == null || menu.getAction() == null) {
      return Collections.emptyMap();
    }

    MetaAction view = menu.getAction();

    if (view.getModel() == null) {
      return Collections.emptyMap();
    }

    Map<String, Object> menuMap = new HashMap<>();

    // Get the model
    MetaModel model = utilsRestService.getModel(view.getModel());
    if (model == null) {
      logger.warn("Model not found for menu name: {}", menuName);
      return Collections.emptyMap();
    }

    menuMap.put("model", model);

    // Get direct references
    ArrayList<MetaModel> directReferences = new ArrayList<>();
    utilsRestService.addReferences(model, directReferences, "ONE_TO_ONE", "ONE_TO_MANY");
    menuMap.put("directReferences", directReferences);

    // Get indirect references
    ArrayList<MetaModel> indirectReferences = new ArrayList<>();
    utilsRestService.addReferences(model, indirectReferences, "MANY_TO_ONE", "MANY_TO_MANY");
    menuMap.put("indirectReferences", indirectReferences);
    return menuMap;
  }

  protected Set<String> split(String menuNames) {
    if (menuNames == null || menuNames.trim().isEmpty()) {
      return Collections.emptySet();
    }

    return Set.of(menuNames.split(","));
  }
}
