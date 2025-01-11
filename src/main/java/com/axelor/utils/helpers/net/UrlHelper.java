/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2022 Axelor (<http://axelor.com>).
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
package com.axelor.utils.helpers.net;

import com.axelor.i18n.I18n;
import com.axelor.utils.exception.UtilsExceptionMessage;
import com.axelor.utils.helpers.ExceptionHelper;
import com.axelor.utils.helpers.file.FileHelper;
import com.google.common.base.Strings;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.net.URLConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UrlHelper {

  static final int SIZE = 1024;

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /**
   * Test the validity of an url.
   *
   * @param url the URL to test
   * @return null if the URL is valid, an error otherwise
   */
  public static String notExist(String url) {

    if (Strings.isNullOrEmpty(url)) {
      return I18n.get(UtilsExceptionMessage.URL_SERVICE_1);
    }

    try {
      URL fileURL = new URL(url);
      fileURL.openConnection().connect();
      return null;
    } catch (java.net.MalformedURLException ex) {
      ExceptionHelper.error(ex);
      return String.format(I18n.get(UtilsExceptionMessage.URL_SERVICE_2), url);
    } catch (java.io.IOException ex) {
      ExceptionHelper.error(ex);
      return String.format(I18n.get(UtilsExceptionMessage.URL_SERVICE_3), url);
    }
  }

  public static void fileUrl(
      File file, String fAddress, String localFileName, String destinationDir) {

    try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
      int byteRead;
      int byteWritten = 0;
      byte[] buf = new byte[SIZE];
      URL url = new URL(fAddress);
      URLConnection urlConnection = url.openConnection();
      InputStream inputStream = urlConnection.getInputStream();

      while ((byteRead = inputStream.read(buf)) != -1) {
        outputStream.write(buf, 0, byteRead);
        byteWritten += byteRead;
      }

      LOG.info("Downloaded Successfully.");
      LOG.debug("No of bytes {}", byteWritten);

    } catch (IOException ex) {
      ExceptionHelper.error(ex);
    }
  }

  public static File fileDownload(String fAddress, String destinationDir, String fileName) {

    int slashIndex = fAddress.lastIndexOf('/');
    int periodIndex = fAddress.lastIndexOf('.');

    if (periodIndex >= 1 && slashIndex >= 0 && slashIndex < fAddress.length() - 1) {
      LOG.debug("Downloading file {} from {} to {}", fileName, fAddress, destinationDir);

      File file = FileHelper.create(destinationDir, fileName);

      fileUrl(file, fAddress, fileName, destinationDir);

      return file;

    } else {
      LOG.error("Destination path or filename is not well formatted.");
      return null;
    }
  }

  public static void fileDownload(
      File file, String fAddress, String destinationDir, String fileName) {

    int slashIndex = fAddress.lastIndexOf('/');
    int periodIndex = fAddress.lastIndexOf('.');

    if (periodIndex >= 1 && slashIndex >= 0 && slashIndex < fAddress.length() - 1) {
      LOG.debug("Downloading file {} from {} to {}", fileName, fAddress, destinationDir);
      fileUrl(file, fAddress, fileName, destinationDir);
    } else {
      LOG.error("Destination path or filename is not well formatted.");
    }
  }
}
