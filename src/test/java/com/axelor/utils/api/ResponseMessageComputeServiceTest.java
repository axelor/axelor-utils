package com.axelor.utils.api;

import com.axelor.utils.db.Contact;
import com.axelor.utils.db.Invoice;
import com.axelor.utils.junit.BaseTest;
import com.google.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ResponseMessageComputeServiceTest extends BaseTest {

  protected final ResponseMessageComputeService responseMessageComputeService;

  @Inject
  public ResponseMessageComputeServiceTest(
      ResponseMessageComputeService responseMessageComputeService) {
    this.responseMessageComputeService = responseMessageComputeService;
  }

  @Test
  void computeCreateMessage_modelWithNameColumn() {
    Contact contact = new Contact();
    contact.setFirstName("First");
    contact.setLastName("Last");
    Assertions.assertEquals(
        "The object Contact First Last has been created",
        responseMessageComputeService.computeCreateMessage(contact));
  }

  @Test
  void computeCreateMessage_modelWithoutNameColumn() {
    Invoice invoice = new Invoice();
    invoice.setId(1L);
    Assertions.assertEquals(
        "The object Invoice has been correctly created with the id : 1",
        responseMessageComputeService.computeCreateMessage(invoice));
  }
}
