/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2025 Axelor (<http://axelor.com>).
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axelor.utils.helpers.file;

import com.axelor.file.temp.TempFiles;
import com.axelor.i18n.I18n;
import com.axelor.utils.exception.UtilsExceptionMessage;
import com.google.common.base.Preconditions;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

public final class PdfHelper {

  private PdfHelper() {}

  /**
   * Merge pdf, then return the webservice url to the created file.
   *
   * @param fileList a list of files to merge
   * @param fileName the name of the created file
   * @return the link to the file
   * @throws IOException if mergePdfToFileLink fails to write the new file.
   */
  public static String mergePdfToFileLink(List<File> fileList, String fileName) throws IOException {
    return getFileLinkFromPdfFile(mergePdf(fileList), fileName);
  }

  /**
   * Append multiple PDF files into one PDF.
   *
   * @param fileList a list of path of PDF files to merge.
   * @return The link to access the generated PDF.
   * @throws IOException if mergePdf fails to write the new file.
   * @throws IllegalArgumentException if the fileList is empty.
   */
  public static File mergePdf(List<File> fileList) throws IOException {
    PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();
    for (File file : fileList) {
      pdfMergerUtility.addSource(file);
    }
    Path tmpFile = TempFiles.createTempFile(null, "");
    try (FileOutputStream stream = new FileOutputStream(tmpFile.toFile())) {
      pdfMergerUtility.setDestinationStream(stream);
      pdfMergerUtility.mergeDocuments(null);
    }
    return tmpFile.toFile();
  }

  /**
   * Return a webservice url to get a printed pdf with a defined name.
   *
   * @param file the printed report
   * @param fileName the file name
   * @return the url
   */
  public static String getFileLinkFromPdfFile(File file, String fileName) {

    String fileLink = "ws/files/report?link=" + file.getName();
    fileLink += "&?name=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8);
    return fileLink;
  }

  /**
   * Allows to get a PDF with multiple copies.
   *
   * @param file the PDF to copy
   * @param copyNumber the number of copies
   * @return a new file with the number of asked copies.
   * @throws IllegalArgumentException if copy number is inferior or equal to 0.
   * @throws IOException if mergePdf fails to write the new file.
   */
  public static File printCopiesToFile(File file, int copyNumber) throws IOException {
    Preconditions.checkArgument(
        copyNumber > 0, I18n.get(UtilsExceptionMessage.BAD_COPY_NUMBER_ARGUMENT));
    List<File> invoicePrintingToMerge = Collections.nCopies(copyNumber, file);
    return mergePdf(invoicePrintingToMerge);
  }
}
