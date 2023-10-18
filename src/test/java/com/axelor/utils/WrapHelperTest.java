package com.axelor.utils;

import com.axelor.auth.db.User;
import com.axelor.meta.loader.LoaderHelper;
import com.axelor.utils.helpers.WrappingHelper;
import com.axelor.utils.junit.BaseTest;
import com.google.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WrapHelperTest extends BaseTest {
  protected final LoaderHelper loaderHelper;

  @Inject
  public WrapHelperTest(LoaderHelper loaderHelper) {
    this.loaderHelper = loaderHelper;
  }

  @BeforeEach
  void setUp() {
    loaderHelper.importCsv("data/users-input.xml");
  }

  @Test
  void wrap_when_decimal_is_null() {
    Assertions.assertEquals(BigDecimal.ZERO, WrappingHelper.wrap((BigDecimal) null));
    Assertions.assertEquals(BigDecimal.TEN, WrappingHelper.wrap(BigDecimal.TEN));
  }

  @Test
  void wrap_when_decimal_is_not_null() {
    Assertions.assertEquals(BigDecimal.ZERO, WrappingHelper.wrap((BigDecimal) null));
    Assertions.assertEquals(BigDecimal.TEN, WrappingHelper.wrap(BigDecimal.TEN));
  }

  @Test
  void wrap_when_list_is_null() {
    Assertions.assertEquals(new ArrayList<>(), WrappingHelper.wrap((List<?>) null));
    Assertions.assertEquals(new ArrayList<>(), WrappingHelper.wrap(new ArrayList<>()));
  }

  @Test
  void wrap_when_list_is_not_null() {
    List<String> list = new ArrayList<>();
    list.add("test");
    Assertions.assertEquals(List.of("test"), WrappingHelper.wrap(list));
  }

  @Test
  void wrap_when_set_is_null() {
    Assertions.assertEquals(new HashSet<>(), WrappingHelper.wrap((Set<?>) null));
    Assertions.assertEquals(new HashSet<>(), WrappingHelper.wrap(new HashSet<>()));
  }

  @Test
  void wrap_when_set_is_not_null() {
    Set<String> set = new HashSet<>();
    set.add("test");
    Assertions.assertEquals(Set.of("test"), WrappingHelper.wrap(set));
  }

  @Test
  void wrap_function() {
    Assertions.assertEquals(
        BigDecimal.valueOf(100),
        WrappingHelper.wrap(BigDecimal.TEN, BigDecimal.ZERO, it -> it.multiply(BigDecimal.TEN)));
    Assertions.assertEquals(
        BigDecimal.ZERO,
        WrappingHelper.wrap((BigDecimal) null, BigDecimal.ZERO, it -> it.multiply(BigDecimal.TEN)));
  }

  @Test
  void accept_decimal() {
    List<BigDecimal> list = new ArrayList<>();
    WrappingHelper.accept(list::add, (BigDecimal) null);
    Assertions.assertEquals(0, list.size());
    WrappingHelper.accept(list::add, BigDecimal.TEN);
    Assertions.assertEquals(1, list.size());
  }

  @Test
  void accept_user() {
    List<User> list = new ArrayList<>();
    WrappingHelper.accept(list::add, User.class, (Long) null);
    Assertions.assertEquals(0, list.size());
    WrappingHelper.accept(list::add, User.class, 1L);
    Assertions.assertEquals(1, list.size());
  }

  @Test
  void accept_user_list() {
    List<User> list = new ArrayList<>();
    WrappingHelper.accept(list::add, User.class, (Long) null);
    Assertions.assertEquals(0, list.size());
    WrappingHelper.accept(list::add, User.class, List.of(1L, 2L));
    Assertions.assertEquals(2, list.size());
  }
}
