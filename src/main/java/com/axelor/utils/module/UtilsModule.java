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
package com.axelor.utils.module;

import com.axelor.app.AxelorModule;
import com.axelor.utils.api.HttpExceptionHandler;
import com.axelor.utils.api.HttpExceptionHandlerImpl;
import com.axelor.utils.api.ResponseMessageComputeService;
import com.axelor.utils.api.ResponseMessageComputeServiceImpl;
import com.axelor.utils.rest.UtilsMenuRestService;
import com.axelor.utils.rest.UtilsMenuRestServiceImpl;
import com.axelor.utils.rest.UtilsRestService;
import com.axelor.utils.rest.UtilsRestServiceImpl;
import com.axelor.utils.service.ActionService;
import com.axelor.utils.service.ActionServiceImpl;
import com.axelor.utils.service.AllModelSelectService;
import com.axelor.utils.service.AllModelSelectServiceImpl;
import com.axelor.utils.service.AppSettingsService;
import com.axelor.utils.service.AppSettingsServiceImpl;
import com.axelor.utils.service.ArchivingService;
import com.axelor.utils.service.ArchivingServiceImpl;
import com.axelor.utils.service.BinaryConversionService;
import com.axelor.utils.service.BinaryConversionServiceImpl;
import com.axelor.utils.service.CipherService;
import com.axelor.utils.service.CipherServiceImpl;
import com.axelor.utils.service.TranslationService;
import com.axelor.utils.service.TranslationServiceImpl;
import com.axelor.utils.service.dmsfile.DmsFileService;
import com.axelor.utils.service.dmsfile.DmsFileServiceImpl;
import com.axelor.utils.service.reader.DataReaderFactory;
import com.axelor.utils.service.reader.DataReaderFactoryImpl;
import com.google.inject.matcher.Matchers;

public class UtilsModule extends AxelorModule {

  @Override
  protected void configure() {
    bind(CipherService.class).to(CipherServiceImpl.class);
    bind(TranslationService.class).to(TranslationServiceImpl.class);
    bind(ArchivingService.class).to(ArchivingServiceImpl.class);
    bind(BinaryConversionService.class).to(BinaryConversionServiceImpl.class);
    bind(AppSettingsService.class).to(AppSettingsServiceImpl.class);
    bind(DmsFileService.class).to(DmsFileServiceImpl.class);
    bind(UtilsRestService.class).to(UtilsRestServiceImpl.class);
    bind(UtilsMenuRestService.class).to(UtilsMenuRestServiceImpl.class);
    bind(ActionService.class).to(ActionServiceImpl.class);
    bind(DataReaderFactory.class).to(DataReaderFactoryImpl.class);
    bind(ResponseMessageComputeService.class).to(ResponseMessageComputeServiceImpl.class);
    bind(AllModelSelectService.class).to(AllModelSelectServiceImpl.class);

    bindInterceptor(
        Matchers.any(),
        Matchers.annotatedWith(HttpExceptionHandler.class),
        new HttpExceptionHandlerImpl());
  }
}
