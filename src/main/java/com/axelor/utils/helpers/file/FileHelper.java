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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FileHelper {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private FileHelper() {}

  /**
   * Méthode permettant de lire le contenu d'un fichier
   *
   * @param fileName Le nom du fichier
   * @return Une liste contenant l'ensemble des lignes
   * @throws IOException Si le fichier n'existe pas
   */
  public static List<String> reader(String fileName) throws IOException {

    List<String> content = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
      String ligne = "";

      while ((ligne = br.readLine()) != null) {
        content.add(ligne);
      }
    }

    return content;
  }

  /**
   * Méthode permettant d'écrire une ligne dans un fichier
   *
   * @param destinationFolder Le chemin du fichier
   * @param fileName Le nom du fichier
   * @param line La ligne à écrire
   * @throws IOException Si le fichier n'existe pas
   */
  public static void writer(String destinationFolder, String fileName, String line)
      throws IOException {
    System.setProperty("line.separator", "\r\n");

    File file = create(destinationFolder, fileName);
    try (FileWriter writer = new FileWriter(file)) {
      writer.write(line);
    }
  }

  /**
   * Méthode permettant d'écrire plusieurs lignes dans un fichier
   *
   * @param destinationFolder Le chemin du fichier
   * @param fileName Le nom du fichier
   * @param multiLine La liste de ligne à écrire
   * @return Le fichier créé
   */
  public static File writer(String destinationFolder, String fileName, List<String> multiLine) {
    System.setProperty("line.separator", "\r\n");

    File file = create(destinationFolder, fileName);
    try (BufferedWriter output = new BufferedWriter(new FileWriter(file))) {
      int i = 0;

      for (String line : multiLine) {

        output.write(line);
        output.newLine();
        i++;
        if (i % 50 == 0) {
          output.flush();
        }
      }

    } catch (IOException ex) {
      LOG.error(ex.getMessage());
    }

    return file;
  }

  /**
   * Création d'un fichier avec son chemin s'il n'existe pas
   *
   * @param fileName Le chemin du fichier
   * @return Le fichier créé
   */
  public static File create(String fileName) {

    String[] filePath = fileName.split(Pattern.quote(File.separator));
    String name = filePath[filePath.length - 1];
    return create(fileName.replace(name, ""), name);
  }

  /**
   * Création d'un fichier avec son chemin si il n'existe pas
   *
   * @param destinationFolder Le chemin du fichier
   * @param fileName Le nom du fichier
   * @return Le fichier créé
   */
  public static File create(String destinationFolder, String fileName) {

    File folder = new File(destinationFolder);
    if (!folder.exists()) {
      folder.mkdirs();
    }

    return new File(folder, fileName);
  }

  /**
   * Méthode permettant de copier le fichier vers une destination
   *
   * @param fileSrc Le chemin du fichier source
   * @param fileDest Le chemin du fichier destination
   * @throws IOException Si le fichier n'existe pas
   */
  public static void copy(String fileSrc, String fileDest) throws IOException {

    try (FileInputStream fileInputStream = new FileInputStream(fileSrc);
        FileOutputStream fileOutputStream = new FileOutputStream(fileDest)) {
      IOUtils.copy(fileInputStream, fileOutputStream);
    }
  }

  /**
   * Copy all files and directories from a Folder to a destination Folder. Must be called like:
   * listAllFilesInFolder(srcFolderPath, "", srcFolderPath, destFolderPath)
   *
   * @param sourceFolder Source directory.
   * @param destinationFolder Destination directory.
   * @throws IOException If the file does not exist.
   */
  public static void copyFolderToFolder(String sourceFolder, String destinationFolder)
      throws IOException {

    // Current Directory.
    File current = new File(sourceFolder);
    File destFile = new File(destinationFolder);

    if (!destFile.exists()) {
      destFile.mkdir();
    }

    if (current.isDirectory()) {

      // List all files and folder in the current directory.
      File[] list = current.listFiles();
      if (list != null) {
        // Read the files list.
        for (File file : list) {
          // Create current source File
          File tf = new File(sourceFolder + File.separator + file.getName());
          // Create current destination File
          File pf = new File(destinationFolder + File.separator + file.getName());
          if (tf.isDirectory() && !pf.exists()) {
            // If the file is a directory and does not exit in the
            // destination Folder.
            // Create the directory.
            pf.mkdir();
            copyFolderToFolder(tf.getAbsolutePath(), pf.getAbsolutePath());
          } else if (tf.isDirectory() && pf.exists()) {
            // If the file is a directory and exits in the
            // destination Folder.
            copyFolderToFolder(tf.getAbsolutePath(), pf.getAbsolutePath());
          } else if (tf.isFile()) {
            // If it is a file.
            copy(
                sourceFolder + File.separator + file.getName(),
                destinationFolder + File.separator + file.getName());
          } else {
            // Other cases.
            LOG.error("Error : Copy folder");
          }
        }
      }
    }
  }
}
