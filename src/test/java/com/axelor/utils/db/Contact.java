/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2026 Axelor (<http://axelor.com>).
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
import com.axelor.db.annotations.NameColumn;
import com.axelor.db.annotations.VirtualColumn;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Entity
@Table(name = "CONTACT_CONTACT")
public class Contact extends JpaModel {

  @Getter
  @ManyToOne(
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      fetch = FetchType.LAZY)
  private Title title;

  @Getter @NotNull private String firstName;

  @Getter @NotNull private String lastName;

  @Widget(
      search = {"firstName", "lastName"},
      readonly = true)
  @NameColumn
  @VirtualColumn
  @Access(AccessType.PROPERTY)
  private String fullName;

  @Getter @NotNull private String email;

  @Getter private String phone;

  @Getter private LocalDate dateOfBirth;

  @Getter
  @OneToMany(
      mappedBy = "contact",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  private List<Address> addresses;

  @Getter
  @Widget(title = "Photo", help = "Max size 4MB.")
  @Lob
  @Basic(fetch = FetchType.LAZY)
  private byte[] image;

  @Getter
  @Widget(multiline = true)
  private String notes;

  @Getter private BigDecimal payeurQuality;

  @Getter
  @Widget(selection = "select.language")
  private String language;

  public Contact() {}

  public Contact(String firstName) {
    this.firstName = firstName;
  }

  public Contact(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public String getFullName() {
    return fullName = calculateFullName();
  }

  protected String calculateFullName() {
    fullName = firstName + " " + lastName;
    if (this.title != null) {
      return this.title.getName() + " " + fullName;
    }
    return fullName;
  }

  @Override
  public String toString() {
    ToStringHelper tsh = MoreObjects.toStringHelper(getClass());

    tsh.add("id", getId());
    tsh.add("fullName", getFirstName());
    tsh.add("email", getEmail());

    return tsh.omitNullValues().toString();
  }

  public Contact find(Long id) {
    return JPA.find(Contact.class, id);
  }

  public static Contact edit(Map<String, Object> values) {
    return JPA.edit(Contact.class, values);
  }

  public static Query<Contact> all() {
    return JPA.all(Contact.class);
  }
}
