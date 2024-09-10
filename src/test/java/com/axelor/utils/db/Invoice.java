/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2022 Axelor (<http://axelor.com>).
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axelor.utils.db;

import com.axelor.db.JPA;
import com.axelor.db.JpaModel;
import com.axelor.db.Query;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "TEST_INVOICE")
public class Invoice extends JpaModel {

  private String code;

  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private Move move;

  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private Move oldMove;

  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private MoveLine rejectMoveLine;

  private LocalDate date;

  private LocalDate dueDate;

  @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "invoice",
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<InvoiceLine> invoiceLines;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Move getMove() {
    return move;
  }

  public void setMove(Move move) {
    this.move = move;
  }

  public Move getOldMove() {
    return oldMove;
  }

  public void setOldMove(Move oldMove) {
    this.oldMove = oldMove;
  }

  public MoveLine getRejectMoveLine() {
    return rejectMoveLine;
  }

  public void setRejectMoveLine(MoveLine rejectMoveLine) {
    this.rejectMoveLine = rejectMoveLine;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
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
    item.setInvoice(this);
  }

  public Invoice persist() {
    return JPA.persist(this);
  }

  public Invoice merge() {
    return JPA.merge(this);
  }

  public Invoice save() {
    return JPA.save(this);
  }

  public void remove() {
    JPA.remove(this);
  }

  public void refresh() {
    JPA.refresh(this);
  }

  public void flush() {
    JPA.flush();
  }

  public static Invoice find(Long id) {
    return JPA.find(Invoice.class, id);
  }

  public static Query<Invoice> all() {
    return JPA.all(Invoice.class);
  }
}
