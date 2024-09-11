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

import com.axelor.i18n.I18n;
import com.axelor.inject.Beans;
import com.axelor.utils.exception.UtilsExceptionMessage;
import com.axelor.utils.helpers.ExceptionHelper;
import com.axelor.utils.service.AppSettingsService;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class HttpExceptionHandlerImpl implements MethodInterceptor {

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    try {
      if (Beans.get(AppSettingsService.class).isApiEnabled()) {
        return invocation.proceed();
      } else {
        throw new ForbiddenException(I18n.get(UtilsExceptionMessage.API_DISABLED));
      }
    } catch (BadRequestException e) {
      return ResponseConstructor.build(Response.Status.BAD_REQUEST, e.getMessage());
    } catch (ForbiddenException e) {
      return ResponseConstructor.build(Response.Status.FORBIDDEN, e.getMessage());
    } catch (NotFoundException e) {
      return ResponseConstructor.build(Response.Status.NOT_FOUND, e.getMessage());
    } catch (ClientErrorException e) {
      return ResponseConstructor.build(Response.Status.CONFLICT, e.getMessage());
    } catch (Exception e) {
      ExceptionHelper.error(e);
      return ResponseConstructor.build(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }
}
