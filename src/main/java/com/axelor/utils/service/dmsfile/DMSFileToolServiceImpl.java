package com.axelor.utils.service.dmsfile;

import com.axelor.db.Model;
import com.axelor.dms.db.DMSFile;
import com.axelor.dms.db.repo.DMSFileRepository;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.service.MetaModelService;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class DMSFileToolServiceImpl implements DMSFileToolService {

  private final DMSFileRepository dmsFileRepository;

  @Inject
  public DMSFileToolServiceImpl(DMSFileRepository dmsFileRepository) {
    this.dmsFileRepository = dmsFileRepository;
  }

  @Override
  public List<DMSFile> fetchAttachedDMSFiles(Model model) {
    if (model == null || model.getId() == null) {
      return new ArrayList<>();
    }

    MetaModel metaModel = MetaModelService.getMetaModel(model.getClass());
    if (metaModel == null) {
      return new ArrayList<>();
    }

    return dmsFileRepository
        .all()
        .filter("self.relatedId = :id AND self.relatedModel = :model AND self.isDirectory IS FALSE")
        .bind("id", model.getId())
        .bind("model", metaModel.getFullName())
        .fetch();
  }

  @Override
  public String getInlineUrl(DMSFile dmsFile) {
    if (dmsFile == null) {
      return "";
    }
    return String.format("ws/dms/inline/%d", dmsFile.getId());
  }
}
