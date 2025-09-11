package com.axelor.utils.init;

import com.axelor.common.ObjectUtils;
import com.axelor.meta.MetaFiles;
import com.axelor.meta.db.MetaFile;
import com.google.inject.Inject;
import java.io.File;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImportMetaFile {

  private final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  protected final MetaFiles metaFiles;

  @Inject
  public ImportMetaFile(MetaFiles metaFiles) {
    this.metaFiles = metaFiles;
  }

  public MetaFile importMetaFile(Object bean, Map<String, Object> values) {
    assert bean instanceof MetaFile;
    MetaFile metaFile = (MetaFile) bean;
    String filePath = (String) values.get("filePath");
    if (ObjectUtils.isEmpty(filePath)) {
      LOG.error("'filePath' is required");
      throw new IllegalArgumentException("'filePath' is required");
    }
    final Path path = (Path) values.get("__path__");
    final File file = path.resolve(filePath).toFile();
    if (!file.isFile()) {
      LOG.error("No file found: {}", file.getAbsolutePath());
      throw new IllegalArgumentException("No file found: " + file.getAbsolutePath());
    }
    try {
      metaFile = metaFiles.upload(file, metaFile);
    } catch (Exception e) {
      LOG.error("Error when importing meta file", e);
      throw new RuntimeException(e);
    }
    return metaFile;
  }
}
