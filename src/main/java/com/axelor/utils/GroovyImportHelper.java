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
import java.util.Objects;
import org.apache.commons.collections.CollectionUtils;

public class GroovyImportHelper {

  @Transactional(rollbackOn = Exception.class)
  public Object compute(Object bean, Map<String, Object> values) {
    if (bean == null || values == null || CollectionUtils.isEmpty(values.entrySet())) {
      return bean;
    }
    assert bean instanceof Model;
    final Model model = (Model) bean;
    final Mapper mapper = Mapper.of(EntityHelper.getEntityClass(model));
    final GroovyScriptHelper scriptHelper =
        new GroovyScriptHelper(new ScriptBindings(Mapper.toMap(model)));

    values
        .entrySet()
        .parallelStream()
        .filter(
            it ->
                it.getKey() != null
                    && it.getKey().startsWith("eval:")
                    && it.getValue() != null
                    && it.getValue() instanceof String
                    && StringUtils.notBlank(Objects.toString(it.getValue())))
        .forEach(
            it ->
                mapper.set(
                    model,
                    it.getKey().substring(5),
                    scriptHelper.eval(Objects.toString(it.getValue()))));
    return JPA.save(model);
  }
}
