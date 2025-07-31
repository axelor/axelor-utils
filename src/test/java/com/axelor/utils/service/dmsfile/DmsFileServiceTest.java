package com.axelor.utils.service.dmsfile;

import com.axelor.auth.db.User;
import com.axelor.auth.db.repo.UserRepository;
import com.axelor.db.JPA;
import com.axelor.db.Model;
import com.axelor.db.mapper.Mapper;
import com.axelor.dms.db.DMSFile;
import com.axelor.dms.db.repo.DMSFileRepository;
import com.axelor.meta.loader.LoaderHelper;
import com.axelor.utils.db.Move;
import com.axelor.utils.db.MoveLine;
import com.axelor.utils.junit.BaseTest;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DmsFileServiceTest extends BaseTest {

  protected final LoaderHelper loaderHelper;
  protected final DmsFileService dmsFileService;
  protected final UserRepository userRepository;
  protected final DMSFileRepository dmsFileRepository;

  @Inject
  DmsFileServiceTest(
      LoaderHelper loaderHelper,
      DmsFileService dmsFileService,
      UserRepository userRepository,
      DMSFileRepository dmsFileRepository) {
    this.loaderHelper = loaderHelper;
    this.dmsFileService = dmsFileService;
    this.userRepository = userRepository;
    this.dmsFileRepository = dmsFileRepository;
  }

  @BeforeEach
  void setUp() {
    loaderHelper.importCsv("data/users-input.xml");
    loaderHelper.importCsv("data/moves-input.xml");
    loaderHelper.importCsv("data/move-lines-input.xml");
    loaderHelper.importCsv("data/dmsfile-input.xml");
    loaderHelper.importCsv("data/metamodel-input.xml");
  }

  @Test
  void fetchAttachedDMSFiles_fetchMultipleDmsFiles() {
    User user = userRepository.find(1L);
    List<DMSFile> result = dmsFileService.fetchAttachedDMSFiles(user);
    List<String> expectedFileNameList = new ArrayList<>();
    expectedFileNameList.add("adminReport.pdf");
    expectedFileNameList.add("export.csv");
    Assertions.assertEquals(
        expectedFileNameList.stream().sorted().collect(Collectors.toList()),
        result.stream().map(DMSFile::getFileName).sorted().collect(Collectors.toList()));
  }

  @Test
  void fetchAttachedDMSFiles_doNotFetchFolder() {
    Move move = JPA.find(Move.class, 1L);
    List<DMSFile> result = dmsFileService.fetchAttachedDMSFiles(move);
    String expectedFileName = "hotcoffee.mkv";
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals(expectedFileName, result.get(0).getFileName());
  }

  @Test
  void fetchAttachedDMSFiles_noDmsFile() {
    User user = userRepository.find(3L);
    List<DMSFile> result = dmsFileService.fetchAttachedDMSFiles(user);
    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  void fetchAttachedDMSFiles_unsavedRecord() {
    MoveLine moveLine = new MoveLine();
    List<DMSFile> result = dmsFileService.fetchAttachedDMSFiles(moveLine);
    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  void getInlineUrl_noDmsFile() {
    Assertions.assertEquals("", dmsFileService.getInlineUrl(null));
  }

  @Test
  void getInlineUrl_presentDmsFile() {
    DMSFile dmsFile = dmsFileRepository.find(1L);
    Assertions.assertEquals("ws/dms/inline/1", dmsFileService.getInlineUrl(dmsFile));
  }

  @Test
  void getDMSRoot_DMS_exist() {
    User user = JPA.find(User.class, 1L);
    String expectedFileParentName = "Mes Documents";
    DMSFile result = dmsFileService.getDMSRoot(user);
    Assertions.assertNotNull(result);
    Assertions.assertEquals(expectedFileParentName, result.getFileName());
  }

  @Test
  void getDMSRoot_DMS_not_exist() {
    Move move = JPA.find(Move.class, 1L);
    DMSFile result = dmsFileService.getDMSRoot(move);
    Assertions.assertNull(result);
  }

  @Test
  void getDMSHome_DMS_exist() {
    User user = JPA.find(User.class, 1L);
    DMSFile dmsFileParent = dmsFileService.getDMSRoot(user);
    DMSFile expectedDmsFile = new DMSFile();
    expectedDmsFile.setFileName("Admin");
    expectedDmsFile.setRelatedId(1L);
    expectedDmsFile.setRelatedModel("com.axelor.auth.db.User");
    expectedDmsFile.setParent(dmsFileParent);
    expectedDmsFile.setIsDirectory(true);
    Assertions.assertEquals(expectedDmsFile.toString(), getDMSHome(user, dmsFileParent).toString());
  }

  @Test
  void addLinkedDmsFiles_mergeDmsFiles() {
    User userToMerge = JPA.find(User.class, 1L);
    List<User> users = userRepository.all().filter("id != ?", 1L).fetch();
    addLinkedDmsFiles(users, userToMerge);
    DMSFile dmsRoot = dmsFileService.getDMSRoot(userToMerge);
    List<DMSFile> result = addLinkedDmsFiles(users, userToMerge);
    for (DMSFile dmsFile : result) {
      Assertions.assertEquals(dmsFile.getRelatedId(), userToMerge.getId());
      if (JPA.find(DMSFile.class, dmsFile.getId()).getParent() != null
          && dmsRoot != null
          && Objects.equals(
              JPA.find(DMSFile.class, dmsFile.getId()).getParent().getId(), dmsRoot.getId())) {
        Assertions.assertEquals(dmsFile.getParent(), getDMSHome(userToMerge, dmsRoot));
      }
    }
  }

  public static DMSFile getDMSHome(Model model, DMSFile dmsRoot) {
    final Mapper mapper = Mapper.of(model.getClass());
    String homeName = mapper.getNameField().get(model).toString();

    if (homeName == null) {
      homeName = Strings.padStart("" + model.getId(), 5, '0');
    }

    DMSFile dmsHome = new DMSFile();
    dmsHome.setFileName(homeName);
    dmsHome.setRelatedId(model.getId());
    dmsHome.setRelatedModel(model.getClass().getName());
    dmsHome.setParent(dmsRoot);
    dmsHome.setIsDirectory(true);
    return dmsHome;
  }

  public List<DMSFile> addLinkedDmsFiles(List<? extends Model> entityList, Model entityMerged) {

    DMSFile dmsRoot = dmsFileService.getDMSRoot(entityMerged);
    DMSFile dmsHome = getDMSHome(entityMerged, dmsRoot);
    List<DMSFile> dmsFileMerged = new ArrayList<>();

    for (Model entity : entityList) {
      List<DMSFile> dmsFileList =
          dmsFileRepository
              .all()
              .filter("self.relatedId = :id AND self.relatedModel = :model")
              .bind("id", entity.getId())
              .bind("model", entity.getClass().getName())
              .fetch();

      for (DMSFile dmsFile : dmsFileList) {
        if (dmsFile.getParent() != null
            && dmsRoot != null
            && Objects.equals(dmsFile.getParent().getId(), dmsRoot.getId())) {
          dmsFile.setParent(dmsHome);
        }
        dmsFile.setRelatedId(entityMerged.getId());
        dmsFileMerged.add(dmsFile);
      }
    }
    return dmsFileMerged;
  }
}
