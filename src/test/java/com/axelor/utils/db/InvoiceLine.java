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

  public Invoice getInvoice() {
    return invoice;
  }

  public void setInvoice(Invoice invoice) {
    this.invoice = invoice;
  }

  public List<InvoiceLine> getInvoiceLines() {
    return invoiceLines;
  }

  public void setInvoiceLines(List<InvoiceLine> invoiceLines) {
    this.invoiceLines = invoiceLines;
  }

  public void addInvoiceLineListItem(InvoiceLine item) {
    if (getInvoiceLines() == null) {
      setInvoiceLines(new ArrayList<>());
    }
    getInvoiceLines().add(item);
    item.setParentInvoiceLine(this);
  }

  public InvoiceLine getParentInvoiceLine() {
    return parentInvoiceLine;
  }

  public void setParentInvoiceLine(InvoiceLine parentInvoiceLine) {
    this.parentInvoiceLine = parentInvoiceLine;
  }
}
