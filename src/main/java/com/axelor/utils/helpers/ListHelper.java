package com.axelor.utils.helpers;

import java.util.List;
import java.util.stream.Collectors;

public class ListHelper {
  public <T> List<T> intersection(List<T> list1, List<T> list2) {
    return list1.stream().filter(list2::contains).distinct().collect(Collectors.toList());
  }
}
