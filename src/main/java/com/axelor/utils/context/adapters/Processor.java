package com.axelor.utils.context.adapters;

import com.axelor.db.Model;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

@Singleton
public final class Processor {

  private final Map<Class<?>, Adapter<?>> adapterMap = new HashMap<>();
  private final Adapter<?> defaultAdapter = new DefaultAdapter();

  public Processor() {
    initMap();
  }

  private void initMap() {
    Reflections reflections =
        new Reflections(
            new ConfigurationBuilder().forPackage("com.axelor.utils.context.adapters"));
    Set<Class<?>> types =
        reflections.getTypesAnnotatedWith(ContextAdapter.class).stream()
            .filter(Adapter.class::isAssignableFrom)
            .collect(Collectors.toSet());
    for (Class<?> type : types) {
      try {
        Class<?> valueClass = type.getAnnotation(ContextAdapter.class).value();
        adapterMap.put(valueClass, (Adapter<?>) type.newInstance());
      } catch (InstantiationException | IllegalAccessException e) {
        throw new IllegalStateException(e);
      }
    }
  }

  public <T> List<T> processCollection(Class<T> klass, Object object) {
    if (!(object instanceof Collection)) {
      return new ArrayList<>();
    }
    List<T> list = new ArrayList<>();
    for (Object item : (Collection<?>) object) {
      list.add(process(klass, item));
    }
    return list;
  }

  public <T> T process(Class<T> klass, Object object) {
    Class<?> adapterClass = klass;
    if (Model.class.isAssignableFrom(klass)) {
      adapterClass = Model.class;
    }
    Adapter<?> adapter = this.adapterMap.getOrDefault(adapterClass, defaultAdapter);
    return adapter.transform(klass, adapter.process(object));
  }
}
