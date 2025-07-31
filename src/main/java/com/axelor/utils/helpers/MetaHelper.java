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

import static com.axelor.utils.MetaJsonFieldType.BOOLEAN;
import static com.axelor.utils.MetaJsonFieldType.DATE;
import static com.axelor.utils.MetaJsonFieldType.DATETIME;
import static com.axelor.utils.MetaJsonFieldType.DECIMAL;
import static com.axelor.utils.MetaJsonFieldType.INTEGER;
import static com.axelor.utils.MetaJsonFieldType.JSON_MANY_TO_MANY;
import static com.axelor.utils.MetaJsonFieldType.JSON_MANY_TO_ONE;
import static com.axelor.utils.MetaJsonFieldType.JSON_ONE_TO_MANY;
import static com.axelor.utils.MetaJsonFieldType.LONG;
import static com.axelor.utils.MetaJsonFieldType.MANY_TO_MANY;
import static com.axelor.utils.MetaJsonFieldType.MANY_TO_ONE;
import static com.axelor.utils.MetaJsonFieldType.ONE_TO_MANY;
import static com.axelor.utils.MetaJsonFieldType.STRING;
import static com.axelor.utils.MetaJsonFieldType.TIME;

import com.axelor.i18n.I18n;
import com.axelor.meta.db.MetaField;
import com.axelor.meta.db.MetaJsonField;
import com.axelor.utils.exception.UtilsExceptionMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/** Utility class containing static helper methods for meta fields and meta json fields */
public class MetaHelper {

  // this class should not be instantiated
  private MetaHelper() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Convert the type of a json field to a type of a field.
   *
   * @param nameType type of a json field
   * @return corresponding type of field
   */
  public static String jsonTypeToType(String nameType) {
    Map<String, String> typeToJsonTypeMap = createTypeToJsonTypeMap();
    // reverse the map
    Map<String, String> jsonTypeToTypeMap =
        typeToJsonTypeMap.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    String typeName = jsonTypeToTypeMap.get(nameType);
    if (typeName == null) {
      throw new IllegalStateException(
          String.format(I18n.get(UtilsExceptionMessage.ERROR_CONVERT_JSON_TYPE_TO_TYPE), nameType));
    }
    return typeName;
  }

  public static String computeFullClassName(MetaField metaField) {
    return String.format("%s.%s", metaField.getPackageName(), metaField.getTypeName());
  }

  /**
   * Convert the type of a field to a type of a json field.
   *
   * @param nameType type of a field
   * @return corresponding type of json field
   */
  public static String typeToJsonType(String nameType) {
    String jsonTypeName = createTypeToJsonTypeMap().get(nameType);
    if (jsonTypeName == null) {
      throw new IllegalStateException(
          String.format(I18n.get(UtilsExceptionMessage.ERROR_CONVERT_TYPE_TO_JSON_TYPE), nameType));
    }
    return jsonTypeName;
  }

  private static Map<String, String> createTypeToJsonTypeMap() {
    Map<String, String> typeToJsonTypeMap = new HashMap<>();
    typeToJsonTypeMap.put("String", STRING);
    typeToJsonTypeMap.put("Integer", INTEGER);
    typeToJsonTypeMap.put("Long", LONG);
    typeToJsonTypeMap.put("BigDecimal", DECIMAL);
    typeToJsonTypeMap.put("Boolean", BOOLEAN);
    typeToJsonTypeMap.put("LocalDateTime", DATETIME);
    typeToJsonTypeMap.put("LocalDate", DATE);
    typeToJsonTypeMap.put("LocalTime", TIME);
    typeToJsonTypeMap.put("ManyToOne", MANY_TO_ONE);
    typeToJsonTypeMap.put("ManyToMany", MANY_TO_MANY);
    typeToJsonTypeMap.put("OneToMany", ONE_TO_MANY);
    typeToJsonTypeMap.put("Custom-ManyToOne", JSON_MANY_TO_ONE);
    typeToJsonTypeMap.put("Custom-ManyToMany", JSON_MANY_TO_MANY);
    typeToJsonTypeMap.put("Custom-OneToMany", JSON_ONE_TO_MANY);
    return typeToJsonTypeMap;
  }

  /**
   * Get Model class name of wantedType in case wantedType is a ManyToOne or Custom-ManyToOne.
   *
   * @param indicator The field to get the class name from
   * @param wantedType The desired type of the field
   * @return The wantedType if wantedType is not a ManyToOne or Custom-ManyToOne
   */
  public static String getWantedClassName(MetaJsonField indicator, String wantedType) {
    String wantedClassName;
    if ((wantedType.equals("ManyToOne") || wantedType.equals("Custom-ManyToOne"))
        && indicator.getTargetModel() != null) {
      // it is a relational field so we get the target model class
      String targetName = indicator.getTargetModel();
      // get only the class without the package
      wantedClassName = targetName.substring(targetName.lastIndexOf('.') + 1);
    } else {
      wantedClassName = wantedType;
    }
    return wantedClassName;
  }
}
