package com.axelor.utils.db;

import com.axelor.db.JpaModel;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "TEST_INVOICE_LINE")
public class InvoiceLine extends JpaModel {

  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private Invoice invoice;

  @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "parentInvoiceLine",
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<InvoiceLine> invoiceLines;

  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private InvoiceLine parentInvoiceLine;

  public void addInvoiceLineListItem(InvoiceLine item) {
    if (getInvoiceLines() == null) {
      setInvoiceLines(new ArrayList<>());
    }
    getInvoiceLines().add(item);
    item.setParentInvoiceLine(this);
  }
}
