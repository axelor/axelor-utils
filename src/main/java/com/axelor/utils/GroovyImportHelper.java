package com.axelor.utils;

import com.axelor.common.StringUtils;
import com.axelor.db.EntityHelper;
import com.axelor.db.JPA;
import com.axelor.db.Model;
import com.axelor.db.mapper.Mapper;
import com.axelor.script.GroovyScriptHelper;
import com.axelor.script.ScriptBindings;
import com.google.inject.persist.Transactional;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class GroovyImportHelper {

  @Transactional(rollbackOn = Exception.class)
  public Object compute(Object bean, Map<String, Object> values) {
    assert bean instanceof Model;
    Model model = (Model) bean;
    Mapper mapper = Mapper.of(EntityHelper.getEntityClass(model));
    ScriptBindings scriptBindings = new ScriptBindings(Mapper.toMap(model));
    GroovyScriptHelper groovyScriptHelper = new GroovyScriptHelper(scriptBindings);
    for (Entry<String, Object> entry : values.entrySet()) {
      if (entry.getKey().startsWith("eval:")
          && entry.getValue() != null
          && entry.getValue() instanceof String
          && StringUtils.notBlank(Objects.toString(entry.getValue()))) {
        mapper.set(
            model,
            entry.getKey().substring(5),
            groovyScriptHelper.eval(Objects.toString(entry.getValue())));
      }
    }
    return JPA.save(model);
  }
}
