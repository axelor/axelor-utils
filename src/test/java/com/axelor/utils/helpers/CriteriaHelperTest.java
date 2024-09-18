package com.axelor.utils.helpers;

import static org.junit.jupiter.api.Assertions.*;

import com.axelor.auth.db.User;
import com.axelor.db.Query;
import com.axelor.meta.loader.LoaderHelper;
import com.axelor.utils.db.Country;
import com.axelor.utils.db.Move;
import com.axelor.utils.junit.BaseTest;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CriteriaHelperTest extends BaseTest {

  protected final LoaderHelper loaderHelper;

  @Inject
  CriteriaHelperTest(LoaderHelper loaderHelper) {
    this.loaderHelper = loaderHelper;
  }

  @BeforeEach
  void setUp() {
    loaderHelper.importCsv("data/users-input.xml");
    loaderHelper.importCsv("data/countries-input.xml");
  }

  @AfterEach
  @Transactional(rollbackOn = Exception.class)
  void tearDown() {
    Query.of(Country.class).delete();
    Query.of(User.class).delete();
  }

  @Test
  void count_noEntries() {
    assertEquals(0L, CriteriaHelper.count(Move.class));
  }

  @Test
  void count_multiples() {
    var userNbr = Query.of(User.class).count();
    assertEquals(userNbr, CriteriaHelper.count(User.class));
  }

  @Test
  void count_singleEntry() {
    assertEquals(1L, CriteriaHelper.count(Country.class));
  }

  @Test
  void fetch_noEntries() {
    assertEquals(Optional.empty(), CriteriaHelper.fetch(Move.class));
  }

  @Test
  void fetch_singleEntry() {
    var country = Query.of(Country.class).fetchOne();
    assertEquals(country, CriteriaHelper.fetch(Country.class).orElseThrow());
  }

  @Test
  void fetch_multipleEntries() {
    assertEquals(Optional.empty(), CriteriaHelper.fetch(User.class));
  }

  @Test
  void builder_buildWithFilters() {
    var users = Query.of(User.class).filter("self.code = :code").bind("code", "admin").fetch();
    assertEquals(
        users,
        CriteriaHelper.builder(User.class)
            .filter((cb, root) -> cb.equal(root.get("code"), "admin"))
            .build()
            .getResultList());
  }

  @Test
  void builder_buildWithoutFilters() {
    var users = Query.of(User.class).fetch();
    assertEquals(users, CriteriaHelper.builder(User.class).build().getResultList());
  }

  @Test
  void builder_buildWithSelection() {
    var users = Query.of(User.class).fetch();
    var userCodes =
        CriteriaHelper.builder(User.class, String.class)
            .select((db, root) -> root.get("code"))
            .build()
            .getResultList();
    assertEquals(users.stream().map(User::getCode).collect(Collectors.toList()), userCodes);
  }

  @Test
  void builder_buildWithoutSelection() {
    assertThrows(
        IllegalStateException.class,
        () -> CriteriaHelper.builder(User.class, String.class).build());
  }
}
