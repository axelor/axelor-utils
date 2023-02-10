package com.axelor.meta.loader;

import com.axelor.common.FileUtils;
import com.axelor.common.ResourceUtils;
import com.axelor.data.csv.CSVImporter;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
@Singleton
public class LoaderHelper {

  private static final String INPUT_DIR_NAME = "input";
  protected final ViewLoader loader;
  protected final Module module;

  protected Path path(String resource) throws URISyntaxException {
    return Paths.get(ResourceUtils.getResource(resource).toURI());
  }

  @Inject
  public LoaderHelper(ViewLoader loader) {
    this.module = new Module("utils-test");
    this.loader = loader;
  }

  @Transactional
  public void loadViewFile(String resourceLocation) {
    final URL url = ResourceUtils.getResource(resourceLocation);
    loader.doLoad(url, module, true);
  }

  public void importCsv(String configFileName) throws URISyntaxException {
    Path config = path(configFileName);
    File data = FileUtils.getFile(config.toFile().getParentFile(), INPUT_DIR_NAME);
    CSVImporter importer =
        new CSVImporter(config.toFile().getAbsolutePath(), data.getAbsolutePath(), null);
    importer.run();
  }
}
