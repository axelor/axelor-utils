package com.axelor.utils.context;

import com.axelor.auth.db.Group;
import com.axelor.auth.db.Role;
import com.axelor.auth.db.User;
import com.axelor.auth.db.repo.GroupRepository;
import com.axelor.auth.db.repo.UserRepository;
import com.axelor.db.JPA;
import com.axelor.db.Query;
import com.axelor.meta.loader.LoaderHelper;
import com.axelor.rpc.Context;
import com.axelor.utils.db.Move;
import com.axelor.utils.db.MoveLine;
import com.axelor.utils.db.MoveReference;
import com.axelor.utils.helpers.context.EntityMergingHelper;
import com.axelor.utils.junit.BaseTest;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntityMergingHelperTest extends BaseTest {
  protected final LoaderHelper loaderHelper;
  protected final UserRepository userRepository;
  protected final GroupRepository groupRepository;

  @Inject
  public EntityMergingHelperTest(
      LoaderHelper loaderHelper, UserRepository userRepository, GroupRepository groupRepository) {
    this.loaderHelper = loaderHelper;
    this.userRepository = userRepository;
    this.groupRepository = groupRepository;
  }

  @BeforeEach
  public void beforeEach() {
    loaderHelper.importCsv("data/groups-input.xml");
    loaderHelper.importCsv("data/users-input.xml");
    loaderHelper.importCsv("data/move-references-input.xml");
    loaderHelper.importCsv("data/moves-input.xml");
    loaderHelper.importCsv("data/move-lines-input.xml");
    createRole("Test");
  }

  @Transactional
  public Role createRole(String roleName) {
    Role role = new Role();
    role.setName(roleName);
    return JPA.save(role);
  }

  public Role findRole(String roleName) {
    return Query.of(Role.class).filter("self.name = :name").bind("name", roleName).fetchOne();
  }

  @AfterEach
  @Transactional
  public void afterEach() {
    Query.of(MoveLine.class).delete();
    Query.of(Move.class).delete();
    Query.of(MoveReference.class).delete();
    Query.of(User.class).delete();
    Query.of(Group.class).delete();
    Query.of(Role.class).delete();
  }

  @Test
  void simpleField() {
    User dbUser = userRepository.findByCode("admin");

    Assertions.assertEquals("Admin", dbUser.getName());

    Context context = new Context(dbUser.getId(), User.class);
    context.put("name", "Tutu");
    User mergedUser = EntityMergingHelper.merge(context, User.class);

    Assertions.assertEquals("Tutu", mergedUser.getName());
    Assertions.assertEquals("admin", mergedUser.getCode());
  }

  @Test
  void emptySimpleField() {
    User dbUser = userRepository.findByCode("admin");
    Assertions.assertEquals("admin@axelor.com", dbUser.getEmail());

    Context context = new Context(dbUser.getId(), User.class);
    context.put("email", null);
    User mergedUser = EntityMergingHelper.merge(context, User.class);

    Assertions.assertNull(mergedUser.getEmail());
  }

  @Test
  void oneToOne() {
    List<Move> dbMoves = Query.of(Move.class).filter("self.moveReference IS NOT NULL").fetch();
    Assertions.assertTrue(dbMoves.size() >= 2);
    Move firstMove = dbMoves.stream().findFirst().orElse(null);
    Assertions.assertNotNull(firstMove);
    Move secondMove = dbMoves.stream().filter(move -> move != firstMove).findFirst().orElse(null);
    Assertions.assertNotNull(secondMove);

    Context firstContext = new Context(firstMove.getId(), Move.class);
    firstContext.put("moveReference", null);
    Move firstMergedMove = EntityMergingHelper.merge(firstContext, Move.class);
    Assertions.assertNull(firstMergedMove.getMoveReference());

    MoveReference firstMoveReference = firstMove.getMoveReference();

    Context secondContext = new Context(secondMove.getId(), Move.class);
    secondContext.put("moveReference", firstMoveReference);
    Move secondMergedMove = EntityMergingHelper.merge(secondContext, Move.class);
    Assertions.assertEquals(firstMoveReference, secondMergedMove.getMoveReference());
  }

  @Test
  void manyToOne() {
    User dbUser = userRepository.findByCode("admin");
    Group adminGroup = dbUser.getGroup();
    Assertions.assertNotNull(adminGroup);

    Group clientsGroup = groupRepository.findByCode("clients");
    Assertions.assertNotNull(clientsGroup);

    Map<String, Object> groupMap = new HashMap<>();
    groupMap.put("id", clientsGroup.getId());
    groupMap.put("name", clientsGroup.getName());

    Map<String, Object> userMap = new HashMap<>();
    userMap.put("group", groupMap);
    userMap.put("id", dbUser.getId());
    Context context = new Context(userMap, User.class);
    User mergedUser = EntityMergingHelper.merge(context, User.class);
    Assertions.assertEquals(clientsGroup, mergedUser.getGroup());

    Context emptyContext = new Context(dbUser.getId(), User.class);
    emptyContext.put("group", null);
    User emptyMergedUser = EntityMergingHelper.merge(emptyContext, User.class);
    Assertions.assertNull(emptyMergedUser.getGroup());
  }

  @Test
  void oneToMany() {
    Move move = Query.of(Move.class).filter("self.code = 'TEST_MOVE_1'").fetchOne();
    Assertions.assertNotNull(move);

    List<MoveLine> moveLines = move.getMoveLines();

    List<Map<String, Object>> moveLineMapList =
        moveLines.stream().map(this::moveLineToMap).collect(Collectors.toList());

    moveLineMapList.forEach(moveLineMap -> moveLineMap.put("credit", BigDecimal.ONE));

    Map<String, Object> moveMap = new HashMap<>();
    moveMap.put("moveLines", moveLineMapList);
    moveMap.put("id", move.getId());
    Context context = new Context(moveMap, Move.class);
    Move mergedMove = EntityMergingHelper.merge(context, Move.class);

    mergedMove
        .getMoveLines()
        .forEach(
            moveLine -> {
              Assertions.assertEquals(BigDecimal.ONE, moveLine.getCredit());
              Assertions.assertEquals(
                  moveLine.getDebit(), MoveLine.find(moveLine.getId()).getDebit());
            });
  }

  private Map<String, Object> moveLineToMap(MoveLine moveLine) {
    Map<String, Object> moveLineMap = new HashMap<>();
    moveLineMap.put("id", moveLine.getId());
    moveLineMap.put("credit", moveLine.getCredit());
    moveLineMap.put("debit", moveLine.getDebit());
    return moveLineMap;
  }

  @Test
  void manyToMany() {
    Role role = findRole("Test");

    User user = Query.of(User.class).filter("self.code LIKE 'admin'").fetchOne();
    Assertions.assertNotNull(user);

    Map<String, Object> roleMap = new HashMap<>();
    roleMap.put("id", role.getId());
    List<Map<String, Object>> rolesMapList = new ArrayList<>();
    rolesMapList.add(roleMap);

    Map<String, Object> userMap = new HashMap<>();
    userMap.put("id", user.getId());
    userMap.put("roles", rolesMapList);
    Context context = new Context(userMap, User.class);
    User mergedUser = EntityMergingHelper.merge(context, User.class);

    Assertions.assertTrue(mergedUser.getRoles().contains(role));
  }

  @Test
  void manyToManyRemoveEntityFromList() {
    Role role = findRole("Test");

    User user = Query.of(User.class).filter("self.code LIKE 'admin'").fetchOne();
    Assertions.assertNotNull(user);

    user = addRoleToUser(role, user);
    Assertions.assertFalse(user.getRoles().isEmpty());

    Context context = new Context(user.getId(), User.class);
    context.put("roles", new HashSet<>());

    User mergedUser = EntityMergingHelper.merge(context, User.class);
    Assertions.assertTrue(mergedUser.getRoles().isEmpty());
  }

  @Transactional
  public User addRoleToUser(Role role, User user) {
    Optional.of(user).map(User::getRoles).ifPresent(roles -> roles.add(role));
    return JPA.save(user);
  }

  @Test
  void dottedManyToManyInContext() {
    Role role = findRole("Test");

    User user = Query.of(User.class).filter("self.code LIKE 'admin'").fetchOne();
    Group group = user.getGroup();

    Assertions.assertNotNull(user);

    Context context = getContext(role, group, user);
    Integer groupVersion = group.getVersion();
    User mergedUser = getUser(context);

    Assertions.assertEquals(mergedUser.getGroup().getVersion(), groupVersion);
    Assertions.assertEquals("Tutu", mergedUser.getName());
  }

  private static Context getContext(Role role, Group group, User user) {
    Map<String, Object> roleMap = new HashMap<>();
    roleMap.put("id", role.getId());
    List<Map<String, Object>> rolesMapList = new ArrayList<>();
    rolesMapList.add(roleMap);

    Map<String, Object> groupMap = new HashMap<>();
    groupMap.put("id", group.getId());
    groupMap.put("roles", rolesMapList);

    Map<String, Object> userMap = new HashMap<>();
    userMap.put("id", user.getId());
    userMap.put("name", "Tutu");
    userMap.put("group", groupMap);

    return new Context(userMap, User.class);
  }

  @Transactional
  protected User getUser(Context context) {
    return JPA.save(EntityMergingHelper.merge(context, User.class));
  }

  @Test
  void realEntityInContext() {
    Role role = findRole("Test");

    User user = Query.of(User.class).filter("self.code LIKE 'admin'").fetchOne();
    Assertions.assertNotNull(user);

    List<Role> rolesList = new ArrayList<>();
    rolesList.add(role);

    Map<String, Object> userMap = new HashMap<>();
    userMap.put("id", user.getId());
    userMap.put("roles", rolesList);
    Context context = new Context(userMap, User.class);
    User mergedUser = EntityMergingHelper.merge(context, User.class);

    Assertions.assertTrue(mergedUser.getRoles().contains(role));
  }
}
