package com.axelor.utils.helpers.context;

import com.axelor.common.StringUtils;
import com.axelor.db.JpaRepository;
import com.axelor.db.Model;
import com.axelor.db.mapper.Mapper;
import com.axelor.db.mapper.Property;
import com.axelor.db.mapper.PropertyType;
import com.axelor.rpc.Context;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

public class EntityMergingHelper {

  private EntityMergingHelper() {}

  /**
   * Merge the given context into the database entity. Does not save the entity
   *
   * @param context the context from the view to be merged
   * @param entityClass the class of the entity
   * @param <T> the entity type
   * @return the merged entity
   */
  public static <T extends Model> T merge(Context context, Class<T> entityClass) {
    return merge(new HashMap<>(context), null, entityClass);
  }

  private static <T extends Model> T merge(
      Map<String, Object> context, T dbEntity, Class<T> entityClass) {
    if (context == null || context.isEmpty()) {
      return null;
    }
    if (dbEntity == null) {
      dbEntity = findOrCreate(context, entityClass);
    }
    mergeProperties(context, dbEntity, entityClass);
    return dbEntity;
  }

  private static <T extends Model> T findOrCreate(
      Map<String, Object> context, Class<T> entityClass) {
    try {
      return context.get("id") == null
          ? entityClass.getDeclaredConstructor().newInstance()
          : JpaRepository.of(entityClass).find(Long.parseLong(context.get("id").toString()));
    } catch (InstantiationException
        | IllegalAccessException
        | InvocationTargetException
        | NoSuchMethodException e) {
      return null;
    }
  }

  private static <T extends Model> void mergeProperties(
      Map<String, Object> context, T dbEntity, Class<T> entityClass) {
    if (dbEntity == null) {
      return;
    }
    Mapper mapper = Mapper.of(entityClass);
    for (Property property : mapper.getProperties()) {
      processProperty(context, dbEntity, property);
    }
  }

  private static <T extends Model> void processProperty(
      Map<String, Object> context, T dbEntity, Property property) {
    if (!context.containsKey(property.getName()) || property.isPrimary()) {
      return;
    }
    Object contextValue = context.get(property.getName());
    switch (property.getType()) {
      case ONE_TO_ONE:
        processSimpleRelation(dbEntity, property, contextValue, true);
        break;
      case MANY_TO_ONE:
        processSimpleRelation(dbEntity, property, contextValue, false);
        break;
      case ONE_TO_MANY:
        processMultiRelation(dbEntity, property, contextValue, true);
        break;
      case MANY_TO_MANY:
        processMultiRelation(dbEntity, property, contextValue, false);
        break;
      default:
        processSimpleField(dbEntity, property, contextValue);
    }
  }

  private static <T extends Model> void processSimpleRelation(
      T dbEntity, Property property, Object contextValue, boolean deep) {
    if (contextValue instanceof Map) {
      contextValue = mergeProperty(contextValue, dbEntity, property, deep);
    }
    if (Objects.equals(contextValue, property.get(dbEntity))) {
      return;
    }
    property.set(dbEntity, contextValue);
  }

  @SuppressWarnings("unchecked")
  private static <T extends Model> T mergeProperty(
      Object contextValue, T dbEntity, Property property, boolean deep) {
    return deep
        ? merge(
            (Map<String, Object>) contextValue,
            (T) property.get(dbEntity),
            (Class<T>) property.getTarget())
        : findOrMerge((Map<String, Object>) contextValue, null, (Class<T>) property.getTarget());
  }

  @SuppressWarnings("unchecked")
  private static <T extends Model> void processMultiRelation(
      T dbEntity, Property property, Object contextValue, boolean deep) {

    Collection<T> dbItems = (Collection<T>) property.get(dbEntity);
    contextValue = wrap((Collection<Object>) contextValue, property.getType());
    Collection<Object> contextList = (Collection<Object>) contextValue;
    String mappedBy = property.getMappedBy();
    Mapper mappedByMapper = Mapper.of(property.getTarget());
    Property mappedByProperty = getProperty(mappedBy, mappedByMapper);

    for (Object contextItem : contextList) {
      processCollectionContext(dbEntity, property, mappedByProperty, dbItems, contextItem, deep);
    }

    if (dbItems != null) {
      clearRemovedItems(dbItems, contextList);
    }
  }

  private static <T> Collection<T> wrap(Collection<T> collection, PropertyType propertyType) {
    if (collection == null) {
      return getDefaultCollection(propertyType);
    }
    return collection;
  }

  private static <T> Collection<T> getDefaultCollection(PropertyType propertyType) {
    switch (propertyType) {
      case ONE_TO_MANY:
        return new ArrayList<>();
      case MANY_TO_MANY:
        return new HashSet<>();
      default:
        return Collections.emptyList();
    }
  }

  private static Property getProperty(String mappedBy, Mapper mappedByMapper) {
    return StringUtils.notBlank(mappedBy) ? mappedByMapper.getProperty(mappedBy) : null;
  }

  @SuppressWarnings("unchecked")
  private static <T extends Model> void processCollectionContext(
      T dbEntity,
      Property property,
      Property mappedByProperty,
      Collection<T> dbItems,
      Object contextItem,
      boolean deep) {
    if (contextItem instanceof Map) {
      processCollectionContext(
          dbEntity,
          property,
          mappedByProperty,
          dbItems,
          contextItem,
          (it, item) ->
              deep
                  ? merge((Map<String, Object>) it, item, (Class<T>) property.getTarget())
                  : findOrMerge((Map<String, Object>) it, item, (Class<T>) property.getTarget()));
    } else if (contextItem instanceof Model) {
      processCollectionContext(
          dbEntity, property, mappedByProperty, dbItems, contextItem, (it, item) -> (T) it);
    }
  }

  private static <T extends Model> void clearRemovedItems(
      Collection<T> dbItems, Collection<Object> contextList) {
    dbItems.removeIf(
        dbItem -> contextList.stream().noneMatch(contextItem -> isSame(dbItem, contextItem)));
  }

  private static <T extends Model> void processSimpleField(
      T dbEntity, Property property, Object contextValue) {
    if (Objects.equals(contextValue, property.get(dbEntity))) {
      return;
    }
    property.set(dbEntity, contextValue);
  }

  private static <T extends Model> T findOrMerge(
      Map<String, Object> context, T dbEntity, Class<T> entityClass) {
    if (context == null || context.isEmpty()) {
      return null;
    }
    if (context.containsKey("id")) {
      return findOrCreate(context, entityClass);
    }
    return merge(context, dbEntity, entityClass);
  }

  private static <T extends Model> void processCollectionContext(
      T dbEntity,
      Property property,
      Property mappedByProperty,
      Collection<T> dbItems,
      Object contextItem,
      BiFunction<Object, T, T> transformation) {
    T item = findMatchingItem(dbItems, contextItem);
    T model = transformation.apply(contextItem, item);
    if (item == null) {
      property.add(dbEntity, model);
    }
    if (mappedByProperty != null) {
      mappedByProperty.set(model, dbEntity);
    }
  }

  private static <T extends Model> T findMatchingItem(Collection<T> dbItems, Object contextItem) {
    return dbItems == null
        ? null
        : dbItems.stream().filter(t -> isSame(t, contextItem)).findFirst().orElse(null);
  }

  @SuppressWarnings("unchecked")
  private static <T extends Model> boolean isSame(T dbItem, Object contextItem) {
    if (dbItem == null || contextItem == null) {
      return false;
    }
    if (contextItem instanceof Map) {
      return isMatchingById((Map<String, Object>) contextItem, dbItem);
    }
    if (contextItem instanceof Model) {
      return Objects.equals(((Model) contextItem).getId(), dbItem.getId());
    }
    return false;
  }

  private static <T extends Model> boolean isMatchingById(Map<String, Object> map, T t) {
    return Objects.equals(
        Optional.ofNullable(t.getId()).map(Objects::toString).orElse(null),
        Optional.ofNullable(map.get("id")).map(Objects::toString).orElse(null));
  }
}
