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
package com.axelor.utils.helpers.net;

import com.axelor.utils.helpers.ExceptionHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public final class FtpHelper {

  private FtpHelper() {}

  public static void getDataFiles(
      String server,
      String username,
      String password,
      String folder,
      String destinationFolder,
      Calendar start,
      Calendar end) {

    try {

      // Connect and logon to FTP Server
      FTPClient ftp = new FTPClient();
      ftp.connect(server);
      ftp.login(username, password);

      // List the files in the directory
      ftp.changeWorkingDirectory(folder);
      FTPFile[] files = ftp.listFiles();
      getDataFile(files, destinationFolder, start, end, ftp);
      // Logout from the FTP Server and disconnect
      ftp.logout();
      ftp.disconnect();

    } catch (Exception e) {
      ExceptionHelper.error(e);
    }
  }

  public static void getDataFile(
      FTPFile[] files, String destinationFolder, Calendar start, Calendar end, FTPClient ftp) {
    for (FTPFile ftpFile : files) {

      Date fileDate = ftpFile.getTimestamp().getTime();
      if (fileDate.compareTo(start.getTime()) >= 0 && fileDate.compareTo(end.getTime()) <= 0) {

        // Download a file from the FTP Server
        File file = new File(destinationFolder + File.separator + ftpFile.getName());
        try (FileOutputStream fos = new FileOutputStream(file)) {
          ftp.retrieveFile(ftpFile.getName(), fos);
          file.setLastModified(fileDate.getTime());
        } catch (Exception e) {
          ExceptionHelper.error(e);
        }
      }
    }
  }
}
