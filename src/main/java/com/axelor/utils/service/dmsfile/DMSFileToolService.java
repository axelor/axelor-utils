package com.axelor.utils.service.dmsfile;

import com.axelor.db.Model;
import com.axelor.dms.db.DMSFile;
import java.util.List;

public interface DMSFileToolService {

  /**
   * Given any model present in database, returns the list of every DMSFile representing a file
   * (excluding folders) attached to this model.
   *
   * @param model any model
   * @return a list of DMSFile related to the given model an empty list if the model is null or has
   *     no id.
   */
  List<DMSFile> fetchAttachedDMSFiles(Model model);
}
