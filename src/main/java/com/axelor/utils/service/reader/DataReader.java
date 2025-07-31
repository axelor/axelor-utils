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
package com.axelor.utils.service.reader;

import com.axelor.meta.db.MetaFile;

public interface DataReader {

  /**
   * Initialize the input file.
   *
   * @param input MetaFile to be read.
   * @param separator Separator of the file.
   * @return True if the file is initialized successfully.
   */
  boolean initialize(MetaFile input, String separator);

  /**
   * Returns record/row of a particular line.
   *
   * @param sheetName Name of the sheet.
   * @param index Index of the line.
   * @param headerSize Size of the header.
   * @return All the records of the line.
   */
  String[] read(String sheetName, int index, int headerSize);

  /**
   * Returns total number of lines.
   *
   * @param sheetName Name of the sheet.
   * @return Total number of lines.
   */
  int getTotalLines(String sheetName);

  /**
   * Returns name of sheets.
   *
   * @return All sheet names.
   */
  String[] getSheetNames();
}
