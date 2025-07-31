package com.axelor.utils.helpers;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ListHelper {

  private ListHelper() {
    throw new IllegalStateException("Cannot instantiate utility classes.");
  }

  /**
   * Returns the intersection of two lists
   *
   * <p>It returns a list containing only the elements that are present in both lists
   *
   * @param list1 the first {@code List}
   * @param list2 the second {@code List}
   * @param <T> type of elements in list
   * @return the intersection of list1 and list2
   */
  public static <T> List<T> intersection(List<T> list1, List<T> list2) {
    if (list1 == null || list2 == null) {
      return Collections.emptyList();
    }
    return list1.stream().filter(list2::contains).distinct().collect(Collectors.toList());
  }

  /**
   * Safely gets the first element of a {@link List}
   *
   * <p>If first element is null or the list is empty, then it returns {@link Optional#empty()}
   *
   * @param list the {@code List}
   * @param <E> type of elements in list
   * @return the first element of list or {@link Optional#empty()} if absent or null
   */
  @Nonnull
  public static <E> Optional<E> first(@Nullable List<E> list) {
    if (list == null || list.isEmpty()) {
      return Optional.empty();
    }
    return Optional.ofNullable(list.get(0));
  }
}
