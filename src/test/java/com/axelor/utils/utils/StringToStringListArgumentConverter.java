package com.axelor.utils.utils;

import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.TypedArgumentConverter;
import java.util.List;

@SuppressWarnings("rawtypes")
public class StringToStringListArgumentConverter
    extends TypedArgumentConverter<String, List> {
  protected StringToStringListArgumentConverter() {
    super(String.class, List.class);
  }

  @Override
  protected List convert(String source) throws ArgumentConversionException {
    if (source == null) {
      return null;
    } else if (source.isEmpty()) {
      return List.of();
    } else {
      return List.of(source.split(","));
    }
  }
}
