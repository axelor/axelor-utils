package com.axelor.utils.helpers;

import com.axelor.auth.db.AuditableModel;
import com.axelor.db.JpaRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.annotation.Nonnull;
import org.apache.commons.collections.CollectionUtils;

public final class WrappingHelper {
  private WrappingHelper() {}

  public static <T extends AuditableModel> void accept(
      @Nonnull Consumer<T> consumer, @Nonnull Class<T> klass, List<Long> ids) {
    if (CollectionUtils.isEmpty(ids)) {
      return;
    }
    JpaRepository<T> jpaRepository = JpaRepository.of(klass);
    ids.stream()
        .filter(it -> it != null && it > 0)
        .map(jpaRepository::find)
        .filter(Objects::nonNull)
        .forEach(consumer);
  }

  public static <T extends AuditableModel> void accept(
      @Nonnull Consumer<T> consumer, @Nonnull Class<T> klass, Long id) {
    if (id == null) {
      return;
    }
    T t = JpaRepository.of(klass).find(id);
    if (t != null) {
      consumer.accept(t);
    }
  }

  public static <T> void accept(Consumer<T> consumer, T value) {
    try {
      if (value != null) {
        consumer.accept(value);
      }
    } catch (Exception e) {
      ExceptionHelper.trace(e);
    }
  }

  public static <T, U> U wrap(T t, U defaultValue, Function<T, U> function) {
    return t != null ? function.apply(t) : defaultValue;
  }

  public static <T> List<T> wrap(List<T> list) {
    return list == null ? new ArrayList<>() : list;
  }

  public static <T> Set<T> wrap(Set<T> list) {
    return list == null ? new HashSet<>() : list;
  }

  public static BigDecimal wrap(BigDecimal decimal) {
    return decimal == null ? BigDecimal.ZERO : decimal;
  }
}
