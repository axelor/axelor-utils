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
package com.axelor.utils.service;

import com.axelor.inject.Beans;
import com.axelor.meta.MetaFiles;
import com.axelor.meta.db.MetaFile;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Objects;

public class BinaryConversionServiceImpl implements BinaryConversionService {

  public MetaFile toMetaFile(byte[] bytes) throws IOException {
    Objects.requireNonNull(bytes);
    String base64 = new String(bytes);
    String base64Data = "";
    if (base64.contains(",")) {
      base64Data = base64.split(",")[1];
    }
    byte[] data = Base64.getDecoder().decode(base64Data);
    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data)) {

      return Beans.get(MetaFiles.class)
          .upload(inputStream, Files.createTempFile(null, null).toFile().getName());
    }
  }
}
