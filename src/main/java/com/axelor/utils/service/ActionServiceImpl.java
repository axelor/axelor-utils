package com.axelor.utils.service;

import com.axelor.common.ObjectUtils;
import com.axelor.common.StringUtils;
import com.axelor.db.EntityHelper;
import com.axelor.db.JPA;
import com.axelor.db.Model;
import com.axelor.db.mapper.Mapper;
import com.axelor.db.mapper.Property;
import com.axelor.meta.ActionExecutor;
import com.axelor.meta.ActionHandler;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.rpc.ContextEntity;
import com.axelor.rpc.ContextHandlerFactory;
import com.axelor.rpc.Resource;
import com.google.inject.Inject;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ActionServiceImpl implements ActionService {

  protected ActionExecutor executor;

  private String modelName;
  private Map<String, Object> context;

  @Inject
  public ActionServiceImpl(ActionExecutor executor) {
    this.executor = executor;
  }
  /**
   * Utility method to apply action-record to an object
   *
   * @param actions name of actions to apply, comma separated
   * @param bean object to apply actions to
   * @return updated bean parameter
   */
  @SuppressWarnings("unchecked")
  @Override
  public Object applyActions(String actions, Object bean) {
    if (StringUtils.isBlank(actions) || bean == null) {
      return bean;
    }
    final Class<? extends Model> klass = (Class<? extends Model>) bean.getClass();
    this.modelName = klass.getName();
    this.context = Mapper.toMap(bean);

    this.apply(actions);

    return this.updateBean(bean);
  }

  @SuppressWarnings("unchecked")
  protected void apply(String actions) {
    ActionHandler handler = this.createHandler(actions);
    Object value = handler.execute();
    ActionResponse response = (ActionResponse) value;
    List<Map<String, Object>> dataList = (List<Map<String, Object>>) response.getData();

    for (Map<String, Object> map : dataList) {
      this.updateContext(map);
    }
  }

  @SuppressWarnings("unchecked")
  private void updateContext(Map<String, Object> value) {
    if (value == null) {
      return;
    }

    Object values = value.get("values");
    Map<String, Object> map = new HashMap<>();

    if (values instanceof ContextEntity) {
      map = ((ContextEntity) values).getContextMap();
    } else if (values instanceof Model) {
      map = Mapper.toMap(value);
    } else if (values instanceof Map) {
      map = (Map<String, Object>) values;
    }

    values = value.get("attrs");

    if (values instanceof Map) {
      for (Object key : ((Map<String, Object>) values).keySet()) {
        var name = key.toString();
        Map<String, Object> attrs = (Map<String, Object>) ((Map<String, Object>) values).get(key);
        if (attrs.containsKey("value")) {
          map.put(name, attrs.get("value"));
        }
        if (attrs.containsKey("value:set")) {
          map.put(name, attrs.get("value:set"));
        }
      }
    }

    context.putAll(map);
  }

  protected ActionHandler createHandler(String action) {
    var request = new ActionRequest();
    Map<String, Object> data = new HashMap<>();

    request.setData(data);
    request.setModel(modelName);
    request.setAction(action);
    data.put("context", context);

    return executor.newActionHandler(request);
  }

  @SuppressWarnings("unchecked")
  protected <T extends Model> Object updateBean(Object bean) {
    final Class<T> klass = (Class<T>) JPA.model(this.modelName);
    var updatedBean = Mapper.toBean(klass, this.context);

    for (Property property : JPA.fields(klass)) {
      Object oldValue = property.get(bean);
      Object newValue = property.get(updatedBean);
      if (!ObjectUtils.isEmpty(newValue) && property.valueChanged(updatedBean, oldValue)) {
        property.set(bean, this.validate(property, newValue));
      }
    }
    return bean;
  }

  protected Object validate(Property property, Object value) {
    if (property == null) {
      return value;
    }

    if (property.isCollection() && value instanceof Collection) {
      value =
          ((Collection<?>) value)
              .stream().map(item -> this.createOrFind(property, item)).collect(Collectors.toList());
    } else if (property.isReference()) {
      value = this.createOrFind(property, value);
    }

    return value;
  }

  protected Object createOrFind(Property property, Object item) {
    if (item == null) {
      return item;
    }

    final Long id = ((Model) item).getId();

    if (id == null || id <= 0) {
      return EntityHelper.getEntity(
          ContextHandlerFactory.newHandler(property.getTarget(), Resource.toMapCompact(item))
              .getProxy());
    }

    final Object bean = JPA.em().find(property.getTarget(), id);

    return bean;
  }
}
