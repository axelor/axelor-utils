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

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ObjectHelper {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /**
   * Méthode permettant de récupérer un champ d'une classe depuis son nom
   *
   * @param fieldName Le nom d'un champ
   * @param classGotten La classe portant le champ
   * @return Le champ trouvé
   */
  public static Field getField(String fieldName, @SuppressWarnings("rawtypes") Class classGotten) {
    Field field = null;
    try {
      LOG.debug("Processed class - {}", classGotten);
      field = classGotten.getDeclaredField(fieldName);

    } catch (SecurityException | NoSuchFieldException e) {
      ExceptionHelper.error(e);
    }
    LOG.debug("Found class : {}", field);
    return field;
  }

  /**
   * Methode permettant de récupérer un object enfant (d'après le nom d'un champ) depuis un object
   * parent
   *
   * @param obj Un objet parent
   * @param fieldName Un nom de champ
   * @return L'objet enfant trouvé
   */
  public static Object getObject(Object obj, String fieldName) {
    Method m = null;
    try {
      @SuppressWarnings("rawtypes")
      Class[] paramTypes = null;
      m =
          obj.getClass()
              .getMethod("get" + StringHelper.capitalizeFirstLetter(fieldName), paramTypes);
    } catch (SecurityException | NoSuchMethodException e) {
      return null;
    }
    LOG.debug("Found method : {}", m);
    try {
      Object[] args = null;
      obj = m.invoke(obj, args);
    } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
      return null;
    }
    LOG.debug("Found object : {}", obj);
    return obj;
  }

  /**
   * Usefully to remove all duplicates on a list. Here we can choose on which key we want to check
   * for duplicate
   *
   * <p>ex: If we want to check on ids List&lt;Person&gt; distinctElements = list.stream().filter(
   * distinctByKey(p -&gt; p.getId()) ).collect( Collectors.toList() );
   *
   * @param keyExtractor Extract method use
   * @param <T> The type of the input arguments of the predicate
   * @return The predicate
   */
  public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
    Map<Object, Boolean> map = new ConcurrentHashMap<>();
    return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
  }
}
