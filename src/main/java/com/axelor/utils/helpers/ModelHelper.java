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

import com.axelor.common.Inflector;
import com.axelor.db.EntityHelper;
import com.axelor.db.JPA;
import com.axelor.db.JpaRepository;
import com.axelor.db.Model;
import com.axelor.db.mapper.Mapper;
import com.axelor.i18n.I18n;
import com.axelor.utils.ThrowConsumer;
import com.axelor.utils.exception.UtilsExceptionMessage;
import com.google.common.base.Preconditions;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.Column;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

public final class ModelHelper {

  private ModelHelper() {}

  /**
   * Apply consumer to each record found from collection of IDs.
   *
   * @param modelClass Model of collection elements.
   * @param ids collection of IDs.
   * @param consumer to apply on each record.
   * @param <T> Model type.
   * @return the number of errors that occurred.
   */
  public static <T extends Model> int apply(
      Class<T> modelClass, Collection<? extends Number> ids, ThrowConsumer<T, Exception> consumer) {

    Preconditions.checkNotNull(ids, I18n.get("The collection of IDs cannot be null."));
    Preconditions.checkNotNull(consumer, I18n.get("The consumer cannot be null."));

    int errorCount = 0;

    for (Number id : ids) {
      try {
        if (id != null) {
          T model = JPA.find(modelClass, id.longValue());
          if (model != null) {
            consumer.accept(model);
            continue;
          }
        }

        throw new IllegalStateException(String.format(I18n.get("Cannot find record #%s"), id));
      } catch (Exception e) {
        ++errorCount;
        ExceptionHelper.error(e);
      } finally {
        JPA.clear();
      }
    }

    return errorCount;
  }

  /**
   * Get unique constraint errors.
   *
   * @param model Model to check.
   * @param messages Custom error messages.
   * @return Map of unique constraint errors.
   */
  public static Map<String, String> getUniqueErrors(Model model, Map<String, String> messages) {
    Map<String, String> errors = new HashMap<>();
    Collection<Field> fields = ModelHelper.checkUniqueFields(model);

    for (Field field : fields) {
      String message =
          messages.getOrDefault(field.getName(), UtilsExceptionMessage.RECORD_UNIQUE_FIELD);
      errors.put(field.getName(), message);
    }

    return errors;
  }

  /**
   * Get unique constraint errors.
   *
   * @param model Model to check.
   * @return Map of unique constraint errors.
   */
  public static Map<String, String> getUniqueErrors(Model model) {
    return getUniqueErrors(model, Collections.emptyMap());
  }

  /**
   * Get set of fields affected by unique constraint error.
   *
   * @param model Model to check.
   * @return Set of unique fields.
   */
  private static Set<Field> checkUniqueFields(Model model) {
    Set<Field> errors = new HashSet<>();
    Class<? extends Model> modelClass = EntityHelper.getEntityClass(model);

    for (Field field : modelClass.getDeclaredFields()) {
      Column column = field.getAnnotation(Column.class);

      if (column == null || !column.unique()) {
        continue;
      }

      String filter = String.format("self.%s = :value", field.getName());
      String getterName = fieldNameToGetter(field.getName());

      try {
        Method getter = modelClass.getMethod(getterName);
        Object value = getter.invoke(model);
        Model existing = JPA.all(modelClass).filter(filter).bind("value", value).fetchOne();

        if (existing != null && !existing.getId().equals(model.getId())) {
          errors.add(field);
        }
      } catch (NoSuchMethodException
          | SecurityException
          | IllegalAccessException
          | IllegalArgumentException
          | InvocationTargetException e) {
        LoggerFactory.getLogger(ModelHelper.class).error(e.getMessage(), e);
      }
    }

    return errors;
  }

  private static String fieldNameToGetter(String name) {
    return "get" + capitalize(name);
  }

  private static String capitalize(String string) {
    return string.substring(0, 1).toUpperCase() + string.substring(1);
  }

  public static String normalizeKeyword(String name, boolean isFieldName) {
    if (name == null) return "";
    name = StringUtils.stripAccents(name).replaceAll("[^a-zA-Z0-9 ]", "");
    return Inflector.getInstance().camelize(name, isFieldName);
  }

  @SuppressWarnings("unchecked")
  public static <T> T toBean(Class<T> klass, Object mapObject) {
    Map<String, Object> map = (Map<String, Object>) mapObject;
    return Mapper.toBean(klass, map);
  }

  /**
   * Copy the content of a list with repository copy method.
   *
   * @param repo Repository to use for copy model.
   * @param src The source list to copy.
   * @param deep Copy all deep reference.
   * @param <T> The list model.
   * @return A new list with the content of src list.
   */
  public static <T extends Model> List<T> copy(JpaRepository<T> repo, List<T> src, boolean deep) {
    List<T> dest = new ArrayList<>();
    for (T obj : src) {
      T cpy = repo.copy(obj, deep);
      dest.add(cpy);
    }
    return dest;
  }

  /**
   * Find the model class from the full name.
   *
   * @param fullName The full name of the model.
   * @return The class of the model.
   */
  public static Class<? extends Model> findModelClass(String fullName) {
    try {
      Class<?> modelClass = Class.forName(fullName);
      return modelClass.asSubclass(Model.class);
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException(e);
    }
  }
}
