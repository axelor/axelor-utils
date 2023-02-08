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
package com.axelor.utils;

import com.axelor.common.ObjectUtils;
import com.axelor.db.JPA;
import com.axelor.db.Model;
import com.axelor.inject.Beans;
import com.axelor.rpc.Context;
import com.axelor.utils.context.adapters.Processor;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import org.apache.shiro.util.CollectionUtils;

public class MapTools {

  private MapTools() {}

  /**
   * A utility method to get a List of specified type from a List&lt;Map&lt;String,Object>>.
   * <BR/><BR/>
   * <b>Deprecated</b> in favor of <i>com.axelor.utils.MapTools#getCollection</i>
   *
   * @param tClass the class of items composing the returned list.
   * @param items the List&lt;Map&lt;String,Object>>.
   * @param <T> the class type of items.
   * @return the List&lt;T>.
   */
  @Deprecated
  @SuppressWarnings("unchecked")
  public static <T extends Model> List<T> makeList(
      @NotNull Class<T> tClass, @NotNull Object items) {
    Objects.requireNonNull(tClass);
    Objects.requireNonNull(items);
    if (ObjectUtils.isEmpty(items)) {
      return Lists.newArrayList();
    }
    return JPA.all(tClass)
        .filter(
            "self.id IN (?1)",
            ((List<Object>) items)
                .stream()
                    .filter(Objects::nonNull)
                    .map(
                        it -> {
                          if (tClass.isInstance(it)) {
                            return tClass.cast(it).getId();
                          }
                          return Long.parseLong(((Map<String, Object>) it).get("id").toString());
                        })
                    .sorted()
                    .distinct()
                    .collect(Collectors.toList()))
        .fetch();
  }

  /**
   * A utility method to get a managed item of specified type from a Map&lt;String, Object> or from
   * a field.
   * <BR/><BR/>
   * <b>Deprecated</b> in favor of <i>com.axelor.utils.MapTools#get</i>
   *
   * @param tClass the class of item.
   * @param object the Map&lt;String,Object> or the field.
   * @param <T> the class type of item.
   * @return the T type item managed.
   */
  @Deprecated
  @SuppressWarnings("unchecked")
  public static <T extends Model> T findObject(@NotNull Class<T> tClass, Object object) {
    Objects.requireNonNull(tClass);
    if (object == null) {
      return null;
    }
    if (tClass.isInstance(object) && tClass.cast(object).getId() != null) {
      return JPA.find(tClass, tClass.cast(object).getId());
    }
    if (((Map<String, Object>) object).get("id") == null) {
      return null;
    }
    return JPA.find(tClass, Long.parseLong(((Map<String, Object>) object).get("id").toString()));
  }

  @SuppressWarnings("unchecked")
  public static <T extends Model> List<T> getSelectedObjects(
      @NotNull Class<T> tClass, Context context) {
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
  public static <T> List<T> getCollection(Context context, Class<T> fieldClass, String fieldName) {
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
  public static <T> T get(Context context, Class<T> fieldClass, String fieldName) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(fieldClass);
    Objects.requireNonNull(fieldName);
    return Beans.get(Processor.class).process(fieldClass, context.get(fieldName));
  }
}
