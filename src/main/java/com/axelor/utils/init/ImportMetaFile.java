package com.axelor.utils.init;

import com.axelor.common.ObjectUtils;
import com.axelor.meta.MetaFiles;
import com.axelor.meta.db.MetaFile;
import jakarta.inject.Inject;
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

  /**
   * Import a {@link MetaFile} entity with AOP imports.
   *
   * <p>Example of an XML import:
   *
   * <pre>{@code
   * <input file="meta-files.csv" separator=";" type="com.axelor.meta.db.MetaFile" call="com.axelor.utils.init.ImportMetaFile:importMetaFile"/>
   * }</pre>
   *
   * <p>The CSV file must contain the following entries:
   *
   * <ul>
   *   <li><b>filePath</b> (String): a path relative to the base directory represented by
   *       <b>__path__</b> that points to the file to upload.
   *   <li><b>importId</b> (String): for easy mapping inside other csv files
   * </ul>
   *
   * <p>If the required entries are missing or the target file does not exist, an exception is
   * thrown.
   *
   * @param bean the target MetaFile entity to populate/update (must be a MetaFile)
   * @param values a map of parameters containing at least {@code "filePath"}
   * @return the uploaded and persisted MetaFile instance
   * @throws IllegalArgumentException if {@code filePath} is missing/empty or the file cannot be
   *     found
   * @throws RuntimeException if an unexpected error occurs during upload
   */
  public MetaFile importMetaFile(Object bean, Map<String, Object> values) {
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
    assert bean instanceof MetaFile;
    MetaFile metaFile = (MetaFile) bean;
    try {
      return metaFiles.upload(file, metaFile);
    } catch (Exception e) {
      LOG.error("Error when importing meta file", e);
      throw new RuntimeException(e);
    }
  }
}
