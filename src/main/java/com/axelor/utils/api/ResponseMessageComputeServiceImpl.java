package com.axelor.utils.api;

import com.axelor.db.Model;
import com.axelor.db.mapper.Mapper;
import com.axelor.db.mapper.Property;
import com.axelor.i18n.I18n;

public class ResponseMessageComputeServiceImpl implements ResponseMessageComputeService {

  @Override
  public String computeCreateMessage(Model model) {
    Mapper mapper = Mapper.of(model.getClass());
    StringBuilder result =
        new StringBuilder(I18n.get("The object") + " " + model.getClass().getSimpleName());
    for (Property property : mapper.getProperties()) {
      if (property.isNameColumn()) {
        String namecolumn = (String) mapper.get(model, property.getName());
        if (!namecolumn.isEmpty()) {
          computeWithNameColumn(result, namecolumn);
          return result.toString();
        }
      }
    }
    computeWithId(model, result, mapper);
    return result.toString();
  }

  private void computeWithNameColumn(StringBuilder result, String namecolumn) {
    result.append(" ");
    result.append(namecolumn);
    result.append(" ");
    result.append(I18n.get("has been created"));
  }

  private void computeWithId(Model model, StringBuilder result, Mapper mapper) {
    result.append(" ");
    result.append(I18n.get("has been correctly created with the id:"));
    result.append(" ");
    result.append(mapper.get(model, "id"));
  }
}
