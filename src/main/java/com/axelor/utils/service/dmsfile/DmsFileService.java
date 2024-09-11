package com.axelor.utils.service.dmsfile;

import com.axelor.db.Model;
import com.axelor.dms.db.DMSFile;
import java.util.List;

public interface DmsFileService {

  /**
   * Given any model present in database, returns the list of every DMSFile representing a file
   * (excluding folders) attached to this model.
   *
   * @param model any model
   * @return a list of DMSFile related to the given model an empty list if the model is null or has
   *     no id.
   */
  List<DMSFile> fetchAttachedDMSFiles(Model model);

  /**
   * Given any DMS file, returns the inline URL of the DMS file. The inline URL is mostly used to
   * display a PDF file in a viewer in a form view.
   *
   * @param dmsFile any DMS file
   * @return the inline URL in a string.
   */
  String getInlineUrl(DMSFile dmsFile);

  /**
   * Given any Model object and a list of model objects, it changes the DMS file of any object in
   * the list by linked them to the object merged .
   *
   * @param entityList List of Model objects
   * @param entityMerged Model object
   */
  void addLinkedDMSFiles(List<? extends Model> entityList, Model entityMerged);

  /**
   * Given any Model object, returns the DMS file root of the DMS file. The DMS root file is a file
   * with a relatedId equal to 0 and isDirectory with a value set to true display a PDF file in a
   * viewer in a form view.
   *
   * @param model DMS file
   * @return the DMS file root .
   */
  DMSFile getDMSRoot(Model model);

  /**
   * Given any Model object and DMS file, returns the DMS file home of the model passed.
   *
   * @param model any Model object
   * @param dmsRoot any dms root file
   * @return the DMS file .
   */
  DMSFile getDMSHome(Model model, DMSFile dmsRoot);
}
