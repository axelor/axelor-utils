package com.axelor.utils.service.dmsfile;

import com.axelor.auth.db.User;
import com.axelor.auth.db.repo.UserRepository;
import com.axelor.db.JPA;
import com.axelor.dms.db.DMSFile;
import com.axelor.dms.db.repo.DMSFileRepository;
import com.axelor.meta.loader.LoaderHelper;
import com.axelor.utils.db.Move;
import com.axelor.utils.db.MoveLine;
import com.axelor.utils.junit.BaseTest;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestDmsFileToolService extends BaseTest {

  protected final LoaderHelper loaderHelper;
  protected final DMSFileToolService dmsFileToolService;

  protected final UserRepository userRepository;
  protected final DMSFileRepository dmsFileRepository;

  @Inject
  TestDmsFileToolService(
      LoaderHelper loaderHelper,
      DMSFileToolService dmsFileToolService,
      UserRepository userRepository,
      DMSFileRepository dmsFileRepository) {
    this.loaderHelper = loaderHelper;
    this.dmsFileToolService = dmsFileToolService;
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
    List<DMSFile> result = dmsFileToolService.fetchAttachedDMSFiles(user);
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
    List<DMSFile> result = dmsFileToolService.fetchAttachedDMSFiles(move);
    String expectedFileName = "hotcoffee.mkv";
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals(expectedFileName, result.get(0).getFileName());
  }

  @Test
  void fetchAttachedDMSFiles_noDmsFile() {
    User user = userRepository.find(3L);
    List<DMSFile> result = dmsFileToolService.fetchAttachedDMSFiles(user);
    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  void fetchAttachedDMSFiles_unsavedRecord() {
    MoveLine moveLine = new MoveLine();
    List<DMSFile> result = dmsFileToolService.fetchAttachedDMSFiles(moveLine);
    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  void getInlineUrl_noDmsFile() {
    Assertions.assertEquals("", dmsFileToolService.getInlineUrl(null));
  }

  @Test
  void getInlineUrl_presentDmsFile() {
    DMSFile dmsFile = dmsFileRepository.find(1L);
    Assertions.assertEquals("ws/dms/inline/1", dmsFileToolService.getInlineUrl(dmsFile));
  }
}
