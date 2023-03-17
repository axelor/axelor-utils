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
package com.axelor.utils.module;

import com.axelor.app.AxelorModule;
import com.axelor.utils.api.HttpExceptionHandler;
import com.axelor.utils.api.HttpExceptionHandlerImpl;
import com.axelor.utils.service.AppSettingsService;
import com.axelor.utils.service.AppSettingsServiceImpl;
import com.axelor.utils.service.ArchivingToolService;
import com.axelor.utils.service.ArchivingToolServiceImpl;
import com.axelor.utils.service.CipherService;
import com.axelor.utils.service.CipherServiceImpl;
import com.axelor.utils.service.ConvertBinaryToMetafileService;
import com.axelor.utils.service.ConvertBinaryToMetafileServiceImpl;
import com.axelor.utils.service.ListToolService;
import com.axelor.utils.service.ListToolServiceImpl;
import com.axelor.utils.service.TranslationService;
import com.axelor.utils.service.TranslationServiceImpl;
import com.google.inject.matcher.Matchers;

public class UtilsModule extends AxelorModule {

  @Override
  protected void configure() {
    bind(CipherService.class).to(CipherServiceImpl.class);
    bind(TranslationService.class).to(TranslationServiceImpl.class);
    bind(ArchivingToolService.class).to(ArchivingToolServiceImpl.class);
    bind(ListToolService.class).to(ListToolServiceImpl.class);
    bind(ConvertBinaryToMetafileService.class).to(ConvertBinaryToMetafileServiceImpl.class);
    bind(AppSettingsService.class).to(AppSettingsServiceImpl.class);
    bindInterceptor(
        Matchers.any(),
        Matchers.annotatedWith(HttpExceptionHandler.class),
        new HttpExceptionHandlerImpl());
  }
}