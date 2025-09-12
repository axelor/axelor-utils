package com.axelor.utils.init;

import com.axelor.meta.db.MetaFile;
import com.axelor.meta.db.repo.MetaFileRepository;
import com.axelor.meta.loader.LoaderHelper;
import com.axelor.utils.junit.BaseTest;
import com.google.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ImportMetaFileTest extends BaseTest {

  protected final LoaderHelper loaderHelper;
  protected final MetaFileRepository metaFileRepository;

  @Inject
  public ImportMetaFileTest(LoaderHelper loaderHelper, MetaFileRepository metaFileRepository) {
    this.loaderHelper = loaderHelper;
    this.metaFileRepository = metaFileRepository;
  }

  @Test
  public void testImportMetaFile() {
    loaderHelper.importCsv("data/metafiles-input.xml");
    MetaFile metaFile =
        metaFileRepository.all().filter("self.fileName = ?1", "axelor.png").fetchOne();
    Assertions.assertNotNull(metaFile, "The MetaFile should not be null");
  }
}
