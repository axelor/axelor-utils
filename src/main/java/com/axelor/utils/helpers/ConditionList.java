package com.axelor.utils.helpers;

import com.axelor.common.Inflector;
import com.axelor.common.StringUtils;
import com.axelor.db.EntityHelper;
import com.axelor.db.JPA;
import com.axelor.db.JpaRepository;
import com.axelor.db.Model;
import com.axelor.db.mapper.Mapper;
import com.axelor.db.mapper.Property;
import com.axelor.i18n.I18n;
import com.axelor.meta.db.MetaField;
import com.axelor.meta.db.MetaModel;
import com.axelor.utils.exception.UtilsExceptionMessage;
import jakarta.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;

public class ConditionList {

  private int elementNbr;
  private final StringJoiner joiner;
  private final String footerFormat;
  private final String headerFormat;

  private ConditionList(StringJoiner joiner, String headerFormat, String footerFormat) {
    this.joiner = joiner;
    this.elementNbr = 0;
    this.headerFormat = headerFormat;
    this.footerFormat = footerFormat;
  }

  public static class Builder {
    private String delimiter = "";
    private String prefix = "";
    private String suffix = "";
    private String header = "";
    private String footer = "";

    public Builder delimiter(@Nonnull String delimiter) {
      this.delimiter = delimiter;
      return this;
    }

    public Builder prefix(@Nonnull String prefix) {
      this.prefix = prefix;
      return this;
    }

    public Builder suffix(@Nonnull String suffix) {
      this.suffix = suffix;
      return this;
    }

    public Builder header(@Nonnull String header) {
      this.header = header;
      return this;
    }

    public Builder footer(@Nonnull String footer) {
      this.footer = footer;
      return this;
    }

    public ConditionList build() {
      return new ConditionList(new StringJoiner(delimiter, prefix, suffix), header, footer);
    }
  }

  public static Builder builder() {
    return new Builder();
  }

  /**
   * This static factory method create a ConditionList built to format an HTML message.
   *
   * @return an HTML ConditionList
   */
  public static ConditionList html() {
    return builder()
        .delimiter("</li><li>")
        .prefix("<ul><li>")
        .suffix("</li></ul>")
        .header("<b>%s</b>")
        .footer("<BR>%s")
        .build();
  }

  /**
   * This static factory method create a ConditionList built to format a plain text message.
   *
   * @return a plain text ConditionList
   */
  public static ConditionList plain() {
    return builder().delimiter(" ; ").header("%s — ").footer(" — %s").build();
  }

  /**
   * This method will throw a plain text formatted exception in case of required null field in the
   * given record.
   *
   * @param object the record to check.
   * @param conditionListSupplier the supplier to create a new condition list to use.
   * @param <T> the given record class type.
   * @throws IllegalStateException listing all required null fields.
   */
  public static <T extends Model> void checkRequiredFields(
      T object, Supplier<ConditionList> conditionListSupplier) {
    ConditionList conditionList = conditionListSupplier.get();
    Mapper mapper = Mapper.of(object.getClass());
    List<String> requiredFields =
        Arrays.stream(mapper.getProperties())
            .filter(Property::isRequired)
            .map(Property::getName)
            .collect(Collectors.toList());
    if (CollectionUtils.isNotEmpty(requiredFields)) {
      conditionList.parse(object, null, requiredFields);
    }
    if (conditionList.isNotEmpty()) {
      String humanClassName =
          I18n.get(Inflector.getInstance().humanize(object.getClass().getSimpleName()));
      throw new IllegalStateException(
          conditionList.format(
              String.format(
                  I18n.get(UtilsExceptionMessage.MISSING_REQUIRED_FIELDS),
                  humanClassName,
                  name(mapper, object)),
              null));
    }
  }

  private static <T extends Model> String name(Mapper mapper, T object) {
    return mapper.getNameField() != null && mapper.getNameField().get(object) != null
        ? mapper.getNameField().get(object).toString()
        : String.format("ID %d", object.getId());
  }

  /**
   * This method will throw a plain text formatted exception in case of reference of the given
   * record in any other records of any models.
   *
   * @param object the record to check for references.
   * @param conditionListSupplier the supplier to create a new condition list to use.
   * @param ignores a list of model and field names to ignore. These parameters follow the format
   *     'Model:field' (for example: Partner:employee).
   * @param <T> the given record class type.
   * @throws IllegalStateException in case of found reference.
   */
  public static <T extends Model> void checkReferences(
      T object, Supplier<ConditionList> conditionListSupplier, String... ignores) {
    Long objectId = object.getId();
    ConditionList conditionList = conditionListSupplier.get();
    Class<T> klass = EntityHelper.getEntityClass(object);
    Mapper mapper = Mapper.of(klass);
    List<MetaField> metaFields =
        JPA.all(MetaField.class)
            .filter("self.typeName = :typeName")
            .bind("typeName", klass.getSimpleName())
            .autoFlush(false)
            .fetch();
    for (MetaField metaField : metaFields) {
      MetaModel metaModel = metaField.getMetaModel();
      if (metaModel == null
          || ignores != null
              && Arrays.stream(ignores)
                  .anyMatch(
                      it -> it.contains(metaModel.getName()) && it.contains(metaField.getName()))) {
        continue;
      }
      Class<? extends Model> modelClass = ModelHelper.findModelClass(metaModel.getFullName());
      String humanClassName =
          I18n.get(Inflector.getInstance().humanize(modelClass.getSimpleName()));
      Mapper modelMapper = Mapper.of(modelClass);
      List<? extends Model> models =
          JpaRepository.of(modelClass)
              .all()
              .filter(String.format("self.%s.id = :id", metaField.getName()))
              .bind("id", objectId)
              .autoFlush(false)
              .fetch();
      for (Model model : models) {
        conditionList.check(
            true,
            String.format(
                I18n.get("Reference on %s ID %s (%s) :: %s"),
                humanClassName,
                model.getId(),
                name(modelMapper, model),
                metaField.getName(),
                I18n.get(metaField.getLabel())));
      }
    }
    if (conditionList.isNotEmpty()) {
      String humanClassName = I18n.get(Inflector.getInstance().humanize(klass.getSimpleName()));
      throw new IllegalStateException(
          conditionList.format(
              String.format(
                  I18n.get(UtilsExceptionMessage.EXISTING_REFERENCES),
                  humanClassName,
                  name(mapper, object)),
              null));
    }
  }

  public <T extends Model> ConditionList parse(
      T object, Object value, List<String> requiredFields) {
    Map<String, Object> map = Mapper.toMap(object);
    Class<? extends Model> klass = object.getClass();
    for (String requiredField : requiredFields) {
      parse(map, klass, value, requiredField);
    }
    return this;
  }

  public <T extends Model> ConditionList parse(T object, Object value, String... requiredFields) {
    return parse(object, value, Arrays.asList(requiredFields));
  }

  public <T extends Model> ConditionList parse(T object, Object value, String requiredField) {
    return parse(Mapper.toMap(object), object.getClass(), value, requiredField);
  }

  protected ConditionList parse(
      Map<String, Object> map, Class<?> klass, Object value, String requiredField) {
    Mapper mapper = Mapper.of(klass);
    Property property = mapper.getProperty(requiredField);
    if (property != null
        && StringUtils.notBlank(property.getName())
        && Objects.equals(map.get(property.getName()), value)) {
      joiner.add(I18n.get(property.getTitle()));
      elementNbr++;
    }
    return this;
  }

  public ConditionList check(boolean condition, String errorMessage) {
    if (condition) {
      joiner.add(errorMessage);
      elementNbr++;
    }
    return this;
  }

  public boolean isEmpty() {
    return elementNbr == 0;
  }

  public boolean isNotEmpty() {
    return !this.isEmpty();
  }

  public String format(String headerMessage, String footerMessage) {
    StringBuilder stringBuilder = new StringBuilder();
    if (StringUtils.notBlank(headerMessage)) {
      stringBuilder.append(String.format(headerFormat, headerMessage));
    }
    if (elementNbr != 0) {
      stringBuilder.append(joiner);
    }
    if (StringUtils.notBlank(footerMessage)) {
      stringBuilder.append(String.format(footerFormat, footerMessage));
    }
    return stringBuilder.toString();
  }
}
