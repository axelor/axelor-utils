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
package com.axelor.utils.helpers;

import com.axelor.db.EntityHelper;
import com.axelor.db.JPA;
import com.axelor.db.Model;
import com.axelor.inject.Beans;
import com.axelor.rpc.Context;
import com.axelor.utils.helpers.context.adapters.Processor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import org.apache.shiro.util.CollectionUtils;

public class MapHelper {

  private MapHelper() {}

  @SuppressWarnings("unchecked")
  public static <T extends Model> List<T> getSelectedObjects(
      @NotNull Class<T> tClass, Map<String, Object> context) {
    Objects.requireNonNull(tClass);
    Objects.requireNonNull(context);
    List<Integer> ids = (ArrayList<Integer>) context.get("_ids");
    if (CollectionUtils.isEmpty(ids)) {
      return new ArrayList<>();
    }
    return ids.stream()
        .map(it -> JPA.find(tClass, Long.parseLong(it.toString())))
        .collect(Collectors.toList());
  }

  /**
   * Simplifies a map of typed values by returning a HashMap of untyped objects. Useful before
   * passing the map to {@link com.axelor.db.Query#bind(Map)}
   *
   * @param map a Map of typed values
   * @param <T> the type of the keys in the map
   * @param <U> the type of the values in the map
   * @return a Map of keys to untyped objects representing the values in the input map
   */
  public static <T, U> Map<T, Object> simplifyMap(Map<T, U> map) {
    return map.entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  /**
   * A utility method to get an object of the given fieldClass in the given context based on the
   * given fieldName.
   *
   * @param context the context to search into.
   * @param fieldClass the class of the given field.
   * @param fieldName the field name.
   * @return the field value.
   * @param <T> the field type.
   */
  public static <T> List<T> getCollection(
      Map<String, Object> context, Class<T> fieldClass, String fieldName) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(fieldClass);
    Objects.requireNonNull(fieldName);
    return Beans.get(Processor.class).processCollection(fieldClass, context.get(fieldName));
  }

  /**
   * A utility method to get an object of the given fieldClass in the given context based on the
   * given fieldName.
   *
   * @param context the context to search into.
   * @param fieldClass the class of the given field.
   * @param fieldName the field name.
   * @return the field value.
   * @param <T> the field type.
   */
  public static <T> T get(Map<String, Object> context, Class<T> fieldClass, String fieldName) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(fieldClass);
    Objects.requireNonNull(fieldName);
    return Beans.get(Processor.class).process(fieldClass, context.get(fieldName));
  }

  public static <I extends Model, O extends Model> O findParent(
      Class<I> contextClass, Class<O> parentClass, Context context, Function<I, O> function) {
    Context parentContext = context.getParent();
    O parent = null;
    if (parentContext == null || !Objects.equals(parentContext.getContextClass(), parentClass)) {
      I model = context.asType(contextClass);
      if (model != null) {
        parent = function.apply(model);
      }
    } else {
      parent = parentContext.asType(parentClass);
    }
    return EntityHelper.getEntity(parent);
  }
}
