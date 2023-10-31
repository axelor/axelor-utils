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
package com.axelor.utils.service.reader;

import com.axelor.meta.MetaFiles;
import com.axelor.meta.db.MetaFile;
import com.axelor.utils.helpers.ExceptionHelper;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class CsvReader implements DataReader {

  private CSVFormat format = null;
  private List<CSVRecord> totalRows = new ArrayList<>();
  private String fileName;

  @Override
  public boolean initialize(MetaFile input, String separator) {

    if (input == null) {
      return false;
    }

    fileName = input.getFileName().replaceAll(".csv", "");
    File inFile = MetaFiles.getPath(input).toFile();
    if (!inFile.exists()) {
      return false;
    }

    try (FileReader reader = new FileReader(inFile)) {
      format = CSVFormat.Builder.create().setDelimiter(separator.charAt(0)).build();
      totalRows = format.parse(reader).getRecords();
      if (CollectionUtils.isEmpty(totalRows)) {
        return false;
      }
    } catch (IOException e) {
      ExceptionHelper.trace(e);
      return false;
    }
    return true;
  }

  @Override
  public String[] read(String sheetName, int index, int headerSize) {

    if (format == null || CollectionUtils.isEmpty(totalRows)) {
      return new String[0];
    }

    return totalRows.get(index).stream().toArray(String[]::new);
  }

  @Override
  public int getTotalLines(String sheetName) {
    if (format == null) {
      return 0;
    }

    return totalRows.size();
  }

  @Override
  public String[] getSheetNames() {

    if (format == null || CollectionUtils.isEmpty(totalRows)) {
      return new String[0];
    }

    String[] sheets = new String[1];
    sheets[0] = fileName;

    return sheets;
  }
}
