package com.axelor.utils.helpers.context;

import com.axelor.db.JPA;
import com.axelor.i18n.I18n;
import com.axelor.meta.db.MetaView;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.meta.schema.actions.ActionView.ActionViewBuilder;
import jakarta.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class ActionViewHelper {

  /**
   * Utility method to fetch the given field value of the given meta view in database using a fine
   * criteria query.
   *
   * @param name The name of the view
   * @param selectFieldClass The class of the field to fetch
   * @param fieldName The name of the field to fetch
   * @return The field value
   * @param <T> The type of the field to fetch class
   */
  public static <T> Optional<T> fetchViewField(
      String name, Class<T> selectFieldClass, String fieldName) {
    try {
      CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
      CriteriaQuery<T> cr = cb.createQuery(selectFieldClass);
      Root<MetaView> root = cr.from(MetaView.class);
      cr.select(root.get(fieldName));
      cr.where(cb.equal(root.get("name"), name));
      return Optional.ofNullable(JPA.em().createQuery(cr).setMaxResults(1).getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  /**
   * Utility method to build an ActionViewBuilder with minimal input information.
   *
   * @param klass The class of the action view model
   * @param domain The domain of the action view if any
   * @param views All views to add into the builder
   * @return The built ActionViewBuilder ready to be completed
   */
  public static ActionViewBuilder build(
      @Nonnull Class<?> klass, String domain, @Nonnull String... views) {
    List<String> metaViews = Arrays.asList(views);
    String firstViewName = metaViews.get(0);
    Optional<String> title = fetchViewField(firstViewName, String.class, "title");
    if (title.isEmpty()) {
      throw new IllegalArgumentException(
          String.format(I18n.get("No title found for the view '%s'."), firstViewName));
    }
    ActionViewBuilder actionViewBuilder =
        ActionView.define(I18n.get(title.get())).model(klass.getName());
    for (String metaView : metaViews) {
      fetchViewField(metaView, String.class, "type")
          .ifPresent(it -> actionViewBuilder.add(it, metaView));
    }
    if (domain != null) {
      actionViewBuilder.domain(domain);
    }
    return actionViewBuilder;
  }
}
