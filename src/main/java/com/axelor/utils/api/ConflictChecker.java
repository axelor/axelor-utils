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
package com.axelor.utils.api;

import com.axelor.auth.db.AuditableModel;
import com.axelor.i18n.I18n;
import com.axelor.utils.exception.UtilsExceptionMessage;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;

public class ConflictChecker {

  private ConflictChecker() {
    throw new IllegalStateException("Utility class");
  }

  public static void checkVersion(AuditableModel currentObject, int versionProvided) {
    if (currentObject.getVersion() != versionProvided) {
      throw new ClientErrorException(
          I18n.get(UtilsExceptionMessage.OBJECT_ALREADY_UPDATED), Response.Status.CONFLICT);
    }
  }
}
