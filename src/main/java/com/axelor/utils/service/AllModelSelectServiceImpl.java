package com.axelor.utils.service;

import com.axelor.db.JPA;
import com.axelor.event.Observes;
import com.axelor.events.StartupEvent;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.MetaSelect;
import com.axelor.meta.db.MetaSelectItem;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.axelor.meta.db.repo.MetaSelectRepository;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility for managing and creating a selection of all metaModels in the system. It handles the
 * creation and updating of a MetaSelect instance named "all.model.reference.select", where the
 * items are the MetaModels present in the system.
 */
public class AllModelSelectServiceImpl implements AllModelSelectService {
  public static final String ALL_MODEL_SELECT = "all.model.reference.select";
  private static final Logger LOGGER = LoggerFactory.getLogger(AllModelSelectServiceImpl.class);
  private final MetaSelectRepository metaSelectRepo;
  private final MetaModelRepository metaModelRepo;

  @Inject
  public AllModelSelectServiceImpl(
      MetaSelectRepository metaSelectRepo, MetaModelRepository metaModelRepo) {
    this.metaSelectRepo = metaSelectRepo;
    this.metaModelRepo = metaModelRepo;
  }

  /**
   * Creates or updates the selection of all models when a StartupEvent is observed.
   *
   * @param event the startup event
   */
  @Transactional
  public void createSelectionOfAllModels(@Observes StartupEvent event) {
    MetaSelect allModelSelect = metaSelectRepo.findByName(ALL_MODEL_SELECT);

    if (allModelSelect == null) {
      allModelSelect = new MetaSelect();
      allModelSelect.setName(ALL_MODEL_SELECT);
      List<MetaModel> metaModelList = metaModelRepo.all().order("name").fetch();
      createMetaSelectItemsFromModels(metaModelList, allModelSelect);
    } else {
      updateMetaSelectItems(allModelSelect);
    }

    metaSelectRepo.save(allModelSelect);
  }

  /**
   * Updates the items in the specified MetaSelect by adding models that are not currently included
   * and removing items that are no longer present in the models.
   *
   * @param allModelSelect the MetaSelect to be updated
   */
  private void updateMetaSelectItems(MetaSelect allModelSelect) {

    List<MetaModel> modelsToAdd = findModelsNotInSelect(allModelSelect);
    if (!modelsToAdd.isEmpty()) {
      createMetaSelectItemsFromModels(modelsToAdd, allModelSelect);
      LOGGER.info("Added models to MetaSelect '{}'", allModelSelect.getName());
    }
    List<MetaSelectItem> itemsToRemove = findItemsNotInMetaModel(allModelSelect);
    if (!itemsToRemove.isEmpty()) {
      allModelSelect.getItems().removeAll(itemsToRemove);
      LOGGER.info("Removed items from MetaSelect '{}'", allModelSelect.getName());
    }
  }

  /**
   * Finds MetaModel instances that are present in the system and not included in the specified
   * MetaSelect instance.
   *
   * @param allModelSelect the MetaSelect to be checked
   * @return a list of MetaModel instances not in the MetaSelect
   */
  private List<MetaModel> findModelsNotInSelect(MetaSelect allModelSelect) {
    EntityManager em = JPA.em();

    CriteriaBuilder cb = em.getCriteriaBuilder();

    CriteriaQuery<MetaModel> cq = cb.createQuery(MetaModel.class);

    Root<MetaModel> metaModel = cq.from(MetaModel.class);

    Subquery<String> subquery = cq.subquery(String.class);
    Root<MetaSelectItem> metaSelectItem = subquery.from(MetaSelectItem.class);
    subquery
        .select(metaSelectItem.get("value"))
        .where(cb.equal(metaSelectItem.get("select"), allModelSelect));

    cq.select(metaModel).where(cb.not(metaModel.get("fullName").in(subquery)));

    return JPA.em().createQuery(cq).getResultList();
  }

  /**
   * Finds MetaSelectItem instances that are not associated with any MetaModel instances in the
   * specified MetaSelect.
   *
   * @param allModelSelect the MetaSelect to be checked
   * @return a list of MetaSelectItem instances not associated with any MetaModel
   */
  private List<MetaSelectItem> findItemsNotInMetaModel(MetaSelect allModelSelect) {

    CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
    CriteriaQuery<MetaSelectItem> cq = cb.createQuery(MetaSelectItem.class);
    Root<MetaSelectItem> metaSelectItem = cq.from(MetaSelectItem.class);

    Subquery<String> subquery = cq.subquery(String.class);
    Root<MetaModel> metaModel = subquery.from(MetaModel.class);
    subquery.select(metaModel.get("fullName"));

    cq.select(metaSelectItem)
        .where(
            cb.equal(metaSelectItem.get("select"), allModelSelect.getId()),
            cb.not(metaSelectItem.get("value").in(subquery)));

    return JPA.em().createQuery(cq).getResultList();
  }

  /**
   * Creates MetaSelectItem instances from a list of MetaModel and adds them to the specified
   * MetaSelect.
   *
   * @param metaModelList the list of MetaModel to create items from
   * @param metaSelect the MetaSelect to add the items to
   */
  private void createMetaSelectItemsFromModels(
      List<MetaModel> metaModelList, MetaSelect metaSelect) {
    metaModelList.forEach(
        model -> {
          MetaSelectItem item = new MetaSelectItem();
          item.setTitle(model.getName());
          item.setValue(model.getFullName());
          item.setSelect(metaSelect);
          metaSelect.addItem(item);
        });
  }
}
