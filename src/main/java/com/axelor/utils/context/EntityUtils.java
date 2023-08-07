package com.axelor.utils.context;

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

public class EntityUtils {

  /**
   * Merge the given context into the database entity. Does not save the entity
   *
   * @param context the context from the view to be merged
   * @param entityClass the class of the entity
   * @param <T> the entity type
   * @return the merged entity
   */
  public static <T extends Model> T merge(Context context, Class<T> entityClass) {
    return EntityUtils.merge(new HashMap<>(context), entityClass);
  }

  private static <T extends Model> T merge(Map<String, Object> context, Class<T> entityClass) {
    if (context == null || context.isEmpty()) {
      return null;
    }
    T dbEntity;
    try {
      dbEntity =
          context.get("id") == null
              ? entityClass.getDeclaredConstructor().newInstance()
              : JpaRepository.of(entityClass).find(Long.parseLong(context.get("id").toString()));
    } catch (InstantiationException
        | IllegalAccessException
        | InvocationTargetException
        | NoSuchMethodException e) {
      return null;
    }
    Mapper mapper = Mapper.of(entityClass);
    for (Property property : mapper.getProperties()) {
      processProperty(context, dbEntity, property);
    }
    return dbEntity;
  }

  private static <T extends Model> void processProperty(
      Map<String, Object> context, T dbEntity, Property property) {
    if (!context.containsKey(property.getName())) {
      return;
    }
    Object object = context.get(property.getName());
    switch (property.getType()) {
      case ONE_TO_ONE:
      case MANY_TO_ONE:
        processSimpleRelation(dbEntity, property, object);
        break;
      case ONE_TO_MANY:
      case MANY_TO_MANY:
        processMultiRelation(dbEntity, property, object);
        break;
      default:
        processSimpleField(dbEntity, property, object);
    }
  }

  private static <T extends Model> void processSimpleRelation(
      T dbEntity, Property property, Object object) {
    @SuppressWarnings("unchecked")
    Model model = merge((Map<String, Object>) object, (Class<T>) property.getTarget());
    property.set(dbEntity, model);
  }

  @SuppressWarnings("unchecked")
  private static <T extends Model> void processMultiRelation(
      T dbEntity, Property property, Object object) {
    object = wrap((Collection<Map<String, Object>>) object, property.getType());
    Collection<Map<String, Object>> list = (Collection<Map<String, Object>>) object;
    String mappedBy = property.getMappedBy();

    Mapper mappedByMapper = Mapper.of(property.getTarget());
    Property mappedByProperty =
        StringUtils.notBlank(mappedBy) ? mappedByMapper.getProperty(mappedBy) : null;
    Collection<Model> collection =
        list.stream()
            .map(map -> processCollectionContext(dbEntity, property, mappedByProperty, map))
            .collect(
                () -> EntityUtils.getDefaultCollection(property.getType()),
                Collection::add,
                Collection::addAll);
    property.set(dbEntity, collection);
  }

  private static <T extends Model> void processSimpleField(
      T dbEntity, Property property, Object object) {
    property.set(dbEntity, object);
  }

  @SuppressWarnings("unchecked")
  private static <T extends Model> T processCollectionContext(
      T dbEntity, Property property, Property mappedByProperty, Map<String, Object> map) {
    T model = merge(map, (Class<T>) property.getTarget());
    if (mappedByProperty != null) {
      mappedByProperty.set(model, dbEntity);
    }
    return model;
  }

  private static <T> Collection<T> wrap(Collection<T> collection, PropertyType propertyType) {
    if (collection == null) {
      return EntityUtils.getDefaultCollection(propertyType);
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
}
