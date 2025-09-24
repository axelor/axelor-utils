package com.axelor.utils.helpers;

import com.axelor.db.JPA;
import com.axelor.db.Model;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import org.hibernate.query.criteria.JpaSelection;

/**
 * CriteriaHelper is a utility class that provides methods to manipulate Criteria API in a more
 * convenient way.
 */
public class CriteriaHelper {

  private CriteriaHelper() {
    throw new IllegalStateException("Helper class");
  }

  /**
   * Count the number of entries of the given model class without any filter.
   *
   * @param <M> The model type
   * @param modelClass The model class
   * @return long the count of elements of type T
   */
  public static <M extends Model> long count(Class<M> modelClass) {
    return builder(modelClass, Long.class).select(CriteriaBuilder::count).build().getSingleResult();
  }

  /**
   * Fetch the single record of the given model class if there's only one in the database. Else,
   * return an empty Optional.
   *
   * @param <M> The model type
   * @param modelClass The model class
   * @return T the element of type T from modelClass if there's only one, else null
   */
  public static <M extends Model> Optional<M> fetch(Class<M> modelClass) {
    long count = count(modelClass);
    if (count != 1) {
      return Optional.empty();
    }
    return Optional.of(builder(modelClass).build().getSingleResult());
  }

  /**
   * Create a new {@link Builder} instance for the given model class.
   *
   * @param <M> The model type
   * @param modelClass The model class
   * @return {@link Builder} instance
   */
  public static <M extends Model> Builder<M, M> builder(Class<M> modelClass) {
    return builder(modelClass, modelClass).select((cb, root) -> root);
  }

  /**
   * Create a new {@link Builder} instance for the given root model class and query result class.
   *
   * @param <R> The root model type
   * @param <Q> The query result type
   * @param rootClass The root model class. Define on which model class to apply the query.
   * @param queryClass The query result class. Define the type of the result.
   * @return {@link Builder} instance
   */
  public static <R extends Model, Q> Builder<R, Q> builder(
      Class<R> rootClass, Class<Q> queryClass) {
    return new Builder<>(rootClass, queryClass);
  }

  /**
   * Builder class for creating queries with filters.
   *
   * @param <R> The root model type
   * @param <Q> The query result type
   */
  public static class Builder<R, Q> {
    private final CriteriaBuilder criteriaBuilder;
    private final CriteriaQuery<Q> criteriaQuery;
    private final Root<R> root;
    private final List<Predicate> filters;

    private Builder(Class<R> rootClass, Class<Q> queryClass) {
      this.criteriaBuilder = JPA.em().getCriteriaBuilder();
      this.criteriaQuery = criteriaBuilder.createQuery(queryClass);
      this.filters = new ArrayList<>();
      this.root = criteriaQuery.from(rootClass);
    }

    /**
     * Define the selection for the query.
     *
     * @param filterFunction Function to create the selection
     * @return {@link Builder} instance
     */
    public Builder<R, Q> select(BiFunction<CriteriaBuilder, Root<R>, Selection<Q>> filterFunction) {
      criteriaQuery.select(filterFunction.apply(criteriaBuilder, root));
      return this;
    }

    /**
     * Add a filter to the query.
     *
     * @param filterFunction Function to create the filter predicate
     * @return {@link Builder} instance
     */
    public Builder<R, Q> filter(BiFunction<CriteriaBuilder, Root<R>, Predicate> filterFunction) {
      filters.add(filterFunction.apply(criteriaBuilder, root));
      return this;
    }

    /**
     * Build the query with the added filters.
     *
     * @return {@link TypedQuery} instance
     */
    public TypedQuery<Q> build() {
      Selection<Q> selection = criteriaQuery.getSelection();
      if (selection == null
          || (selection.isCompoundSelection()
              && ((JpaSelection<?>) selection).getSelectionItems().isEmpty())) {
        throw new IllegalStateException("You must define a selection to build the final query");
      }
      if (!filters.isEmpty()) {
        criteriaQuery.where(criteriaBuilder.and(filters.toArray(new Predicate[0])));
      }
      return JPA.em().createQuery(criteriaQuery);
    }
  }
}
