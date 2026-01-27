package com.axelor.utils.utils;

import com.axelor.common.ResourceUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Singleton;
import jakarta.inject.Inject;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.InputStream;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
@Singleton
public class TestingHelper {

  protected final ObjectMapper mapper;

  @Inject
  public TestingHelper(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  protected InputStream read(String resource) {
    return ResourceUtils.getResourceStream(resource);
  }

  protected ObjectMapper getObjectMapper() {
    return mapper;
  }

  @SuppressWarnings("unchecked")
  public <T> T unmarshal(String resource, Class<T> type) throws JAXBException {
    JAXBContext context = JAXBContext.newInstance(type);
    Unmarshaller unmarshaller = context.createUnmarshaller();
    return (T) unmarshaller.unmarshal(read(resource));
  }
}
