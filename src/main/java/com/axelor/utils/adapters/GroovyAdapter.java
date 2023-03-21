package com.axelor.utils.adapters;

import com.axelor.common.StringUtils;
import com.axelor.data.adapter.Adapter;
import com.axelor.script.GroovyScriptHelper;
import com.axelor.script.ScriptBindings;
import java.util.Map;
import java.util.Objects;

public class GroovyAdapter extends Adapter {

  @Override
  public Object adapt(Object value, Map<String, Object> context) {
    if (value == null || context == null) {
      return value;
    }
    final GroovyScriptHelper scriptHelper =
        new GroovyScriptHelper(new ScriptBindings(context));

    if (value instanceof String
        && StringUtils.notBlank(Objects.toString(value))) {
      return scriptHelper.eval(Objects.toString(value));
    }
    return value;
  }
}
