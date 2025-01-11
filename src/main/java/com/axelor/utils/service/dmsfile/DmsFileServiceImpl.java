package com.axelor.utils.service.dmsfile;

import com.axelor.db.Model;
import com.axelor.db.mapper.Mapper;
import com.axelor.dms.db.DMSFile;
import com.axelor.dms.db.repo.DMSFileRepository;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.service.MetaModelService;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class DmsFileServiceImpl implements DmsFileService {

  private final DMSFileRepository dmsFileRepository;

  @Inject
  public DmsFileServiceImpl(DMSFileRepository dmsFileRepository) {
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

  @Override
  public void addLinkedDMSFiles(List<? extends Model> entityList, Model entityMerged) {
    DMSFile dmsRoot = getDMSRoot(entityMerged);
    DMSFile dmsHome = getDMSHome(entityMerged, dmsRoot);

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
            && dmsFile.getParent().getId().equals(dmsRoot.getId())) {
          dmsFile.setParent(dmsHome);
        }
        dmsFile.setRelatedId(entityMerged.getId());
        dmsFileRepository.save(dmsFile);
      }
    }
  }

  @Override
  public DMSFile getDMSRoot(Model model) {
    return dmsFileRepository
        .all()
        .filter(
            "COALESCE(self.isDirectory, FALSE) = TRUE AND self.relatedModel = :model AND COALESCE(self.relatedId, 0) = 0")
        .bind("model", model.getClass().getName())
        .fetchOne();
  }

  @Override
  public DMSFile getDMSHome(Model model, DMSFile dmsRoot) {
    String homeName;
    final Mapper mapper = Mapper.of(model.getClass());
    homeName = mapper.getNameField().get(model).toString();

    if (homeName == null) {
      homeName = Strings.padStart("" + model.getId(), 5, '0');
    }

    DMSFile dmsHome = new DMSFile();
    dmsHome.setFileName(homeName);
    dmsHome.setRelatedId(model.getId());
    dmsHome.setRelatedModel(model.getClass().getName());
    dmsHome.setParent(dmsRoot);
    dmsHome.setIsDirectory(true);

    return dmsFileRepository.save(dmsHome);
  }
}
