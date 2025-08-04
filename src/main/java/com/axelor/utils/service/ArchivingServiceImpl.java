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
package com.axelor.utils.service;

import com.axelor.common.ObjectUtils;
import com.axelor.common.StringUtils;
import com.axelor.db.JPA;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ArchivingServiceImpl implements ArchivingService {

  public static final String A_Z = "([A-Z])";

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
                          model.table_name as tableName
                        FROM meta_field field
                        LEFT JOIN meta_model model on field.meta_model = model.id
                        WHERE field.type_name like :objectName""");
    findModelWithobjectFieldQuery.setParameter("objectName", object.getClass().getSimpleName());
    List<Object[]> resultList = findModelWithobjectFieldQuery.getResultList();
    for (Object[] result : resultList) {
      computeRelationship(object, id, result)
          .ifPresent(relationship -> objectsLinkToMap.put((String) result[1], relationship));
    }
    return objectsLinkToMap;
  }

  protected Optional<String> computeRelationship(Object object, Long id, Object[] result) {
    String fieldName = ((String) result[0]).replaceAll(A_Z, "_$1").toLowerCase();
    String modelName = (String) result[1];
    String modelNameBDDFormat = modelName.replaceAll(A_Z, "_$1").toLowerCase().replace("^_", "");
    String relationship = (String) result[2];
    String mappedBy = null;
    if (result[3] != null) {
      mappedBy = ((String) result[3]).replaceAll(A_Z, "_$1").toLowerCase();
    }
    String tableObjectLinkName = ((String) result[4]).toLowerCase().replace(" ", "_");
    String tableObjectName = this.getTableObjectName(object);

    String query = null;
    if (relationship.equals("ManyToOne") || relationship.equals("OneToOne")) {
      query =
          "SELECT DISTINCT ol.%s FROM %s ol LEFT JOIN %s o ON ol.%s = o.id WHERE o.id = :objectId"
              .formatted(fieldName, tableObjectLinkName, tableObjectName, fieldName);
    } else if (result[3].equals("OneToMany")) {
      String manyToOneMappedField = StringUtils.notEmpty(mappedBy) ? mappedBy : modelNameBDDFormat;
      query =
          "SELECT DISTINCT ol.%s FROM %s ol LEFT JOIN %s o ON ol.id = o.%s WHERE o.id = :objectId"
              .formatted(fieldName, tableObjectLinkName, tableObjectName, manyToOneMappedField);
    } else if (relationship.equals("ManyToMany")) {
      String tableNameSet = tableObjectLinkName + "_" + fieldName;
      query =
          "SELECT DISTINCT %s FROM %s WHERE %s = :objectId"
              .formatted(fieldName, tableNameSet, fieldName);
    }

    if (query == null) {
      return Optional.empty();
    }

    Query findobjectQuery = JPA.em().createNativeQuery(query);
    findobjectQuery.setParameter("objectId", id);

    Object objectToCheck = null;
    try {
      objectToCheck = findobjectQuery.getSingleResult();
    } catch (NoResultException nRE) {
      // nothing to do
    }

    return objectToCheck != null ? Optional.of(relationship) : Optional.empty();
  }

  protected String getTableObjectName(Object object) {
    String moduleName =
        object
            .getClass()
            .getPackage()
            .getName()
            .replace("com.axelor.apps.", "")
            .replace(".db", "")
            .replace(".", "_")
            .toLowerCase();
    String objectName = object.getClass().getSimpleName().replaceAll(A_Z, "_$1").toLowerCase();
    return moduleName + objectName;
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
