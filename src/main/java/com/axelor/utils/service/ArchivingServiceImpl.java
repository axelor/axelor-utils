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
package com.axelor.utils.service;

import com.axelor.common.ObjectUtils;
import com.axelor.db.JPA;
import jakarta.persistence.Entity;
import jakarta.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ArchivingServiceImpl implements ArchivingService {

  @Override
  public Map<String, String> getObjectLinkTo(Object object, Long id) {
    Map<String, String> objectsLinkToMap = new HashMap<>();
    Query findModelWithobjectFieldQuery =
        JPA.em()
            .createNativeQuery(
                """
                        SELECT
                          field.name as fieldName,
                          model.name as ModelName,
                          field.relationship as relationship,
                          field.mapped_by as mappedBy,
                          model.table_name as tableName,
                          model.full_name as fullName
                        FROM meta_field field
                        LEFT JOIN meta_model model on field.meta_model = model.id
                        WHERE field.type_name like :objectName""");
    findModelWithobjectFieldQuery.setParameter("objectName", object.getClass().getSimpleName());
    List<Object[]> resultList = findModelWithobjectFieldQuery.getResultList();
    for (Object[] result : resultList) {
      computeRelationship(id, result)
          .ifPresent(relationship -> objectsLinkToMap.put((String) result[1], relationship));
    }
    return objectsLinkToMap;
  }

  protected Optional<String> computeRelationship(Long id, Object[] result) {
    String fieldName = (String) result[0];
    String relationship = (String) result[2];
    String fullName = (String) result[5];

    Class<?> modelClass = fullName != null ? JPA.model(fullName) : null;
    if (modelClass == null || !modelClass.isAnnotationPresent(Entity.class)) {
      return Optional.empty();
    }
    String entityName = JPA.em().getMetamodel().entity(modelClass).getName();

    String query;
    if (relationship.equals("ManyToOne") || relationship.equals("OneToOne")) {
      query = "SELECT 1 FROM %s self WHERE self.%s.id = :objectId".formatted(entityName, fieldName);
    } else if (relationship.equals("ManyToMany")) {
      query =
          "SELECT 1 FROM %s self JOIN self.%s linked WHERE linked.id = :objectId"
              .formatted(entityName, fieldName);
    } else {
      return Optional.empty();
    }

    List<?> linkList =
        JPA.em().createQuery(query).setParameter("objectId", id).setMaxResults(1).getResultList();
    return linkList.isEmpty() ? Optional.empty() : Optional.of(relationship);
  }

  @Override
  public String getModelTitle(String modelName) {
    Query findModelWithobjectFieldQuery =
        JPA.em()
            .createNativeQuery(
                "SELECT view.title as viewTitle FROM meta_view view WHERE view.name like :viewName");
    findModelWithobjectFieldQuery.setParameter(
        "viewName", modelName.replaceAll("([a-z])([A-Z])", "$1-$2").toLowerCase() + "-form");
    List<String> modelNameList = findModelWithobjectFieldQuery.getResultList();
    return !ObjectUtils.isEmpty(modelNameList) ? modelNameList.getFirst() : modelName;
  }
}
