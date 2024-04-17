package com.axelor.utils.helpers;

import com.google.common.base.Optional;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ListHelper {
  public <T> List<T> intersection(List<T> list1, List<T> list2) {
    return list1.stream().filter(list2::contains).distinct().collect(Collectors.toList());
  }

  /**
   * Safely gets the first element of a {@link List}
   *
   * <p>If first element is null or the list is empty, then it returns {@link Optional#absent()}
   *
   * @param list the {@code List}
   * @param <E> type of elements in list
   * @return the first element of list or {@link Optional#absent()} if absent or null
   */
  @Nonnull
  public static <E> Optional<E> first(@Nullable List<E> list) {
    if (list == null || list.isEmpty()) {
      return Optional.absent();
    }
    return Optional.fromNullable(list.get(0));
  }
}
