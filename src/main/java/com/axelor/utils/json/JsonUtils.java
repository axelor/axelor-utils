package com.axelor.utils.json;

import com.axelor.db.EntityHelper;
import com.axelor.db.Model;
import com.axelor.db.mapper.Mapper;
import com.axelor.db.mapper.Property;
import com.axelor.db.mapper.PropertyType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;

/**
 * A utility class for parsing JSON strings and converting objects to JSON strings using the Jackson
 * ObjectMapper.
 */
public class JsonUtils {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  private static class ModelSerializer extends JsonSerializer<Model> {

    private Map<String, Object> processModel(Object fieldValue) {
      var map = new HashMap<String, Object>();
      map.put("id", ((Model) fieldValue).getId());
      map.put("version", ((Model) fieldValue).getVersion());
      map.put("selected", ((Model) fieldValue).isSelected());
      map.put("model", EntityHelper.getEntityClass(fieldValue));
      return map;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void serialize(Model value, JsonGenerator gen, SerializerProvider serializers)
        throws IOException {
      gen.writeStartObject();
      var mapper = Mapper.of(EntityHelper.getEntityClass(value));
      Property[] properties = mapper.getProperties();
      Arrays.sort(properties, Comparator.comparing(Property::getType));
      for (Property property : properties) {
        Object fieldValue = mapper.get(value, property.getName());
        if (fieldValue == null
            || (fieldValue instanceof Collection
                && CollectionUtils.isEmpty((Collection<?>) fieldValue))) {
          continue;
        }
        if (Objects.equals(property.getType(), PropertyType.MANY_TO_ONE)) {
          var map = processModel(fieldValue);
          gen.writeObjectField(property.getName(), map);
        } else if (Objects.equals(property.getType(), PropertyType.MANY_TO_MANY)
            && fieldValue instanceof Collection) {
          gen.writeObjectField(
              property.getName(),
              ((Set<Model>) fieldValue)
                  .stream().map(this::processModel).collect(Collectors.toSet()));
        } else {
          gen.writeObjectField(property.getName(), fieldValue);
        }
      }
      gen.writeEndObject();
    }
  }

  private static final class AopModule extends SimpleModule {
    @Override
    public void setupModule(SetupContext context) {
      addSerializer(Model.class, new ModelSerializer());
      super.setupModule(context);
    }
  }

  static {
    objectMapper.registerModule(new AopModule());
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    objectMapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
  }

  private JsonUtils() {
    // Utility class
  }

  /**
   * Returns the ObjectMapper instance used by this class. <br>
   * <br>
   * This ObjectMapper contains a custom serializer for Model objects that only serializes the ID
   * and model name for all ManyToOne and ManyToMany fields to avoid infinite recursion. <br>
   * <br>
   * This ObjectMapper also uses the JavaTimeModule to serialize dates to ISO format.
   *
   * @return the ObjectMapper instance
   */
  public static ObjectMapper getMapper() {
    return objectMapper;
  }

  /**
   * Parses the given JSON string into an object of the specified class.
   *
   * @param json the JSON string to parse
   * @param klass the class of the object to parse the JSON into
   * @return the parsed object
   * @throws IOException if there is an error parsing the JSON string
   */
  public static <T> T parse(String json, Class<T> klass) throws IOException {
    return getMapper().readValue(json, klass);
  }

  /**
   * Converts the given object to a JSON string.
   *
   * @param object the object to convert to JSON
   * @return the JSON string representation of the object
   * @throws JsonProcessingException if there is an error converting the object to JSON
   */
  public static <T> String toJson(T object) throws JsonProcessingException {
    return getMapper().writeValueAsString(object);
  }
}
