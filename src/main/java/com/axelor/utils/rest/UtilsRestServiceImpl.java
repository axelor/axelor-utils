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
package com.axelor.utils.rest;

import com.axelor.db.mapper.Mapper;
import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.axelor.utils.ExceptionTool;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;

public class UtilsRestServiceImpl implements UtilsRestService {

  @Override
  public void addReferences(MetaModel model, List<MetaModel> listOfRef, String... types) {
    try {
      List<MetaModel> modelList =
          Arrays.stream(Mapper.of(Class.forName(model.getFullName())).getProperties())
              .filter(
                  property ->
                      (property.getType().toString().equals(types[0])
                              || property.getType().toString().equals(types[1]))
                          && !listOfRef.contains(getModel(property.getTarget().getName())))
              .map(it -> getModel(it.getTarget().getName()))
              .collect(Collectors.toList());

      if (CollectionUtils.isEmpty(modelList)) {
        return;
      }

      listOfRef.addAll(modelList);

      modelList.forEach(metaModel -> addReferences(metaModel, listOfRef, types));

    } catch (ClassNotFoundException e) {
      ExceptionTool.trace(e);
    }
  }

  @Override
  public MetaModel getModel(String fullName) {

    String modelName = fullName.substring(fullName.lastIndexOf('.') + 1);

    return Beans.get(MetaModelRepository.class).findByName(modelName);
  }
}
