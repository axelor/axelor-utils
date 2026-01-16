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
import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "CONTACT_ADDRESS")
public class Address extends JpaModel {

  @NotNull private String street;

  private String area;

  @NotNull private String city;

  @NotNull private String zip;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private Country country;

  @NotNull
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private Contact contact;

  public Address(String street, String area, String city) {
    this.street = street;
    this.area = area;
    this.city = city;
  }

  @Override
  public String toString() {
    ToStringHelper tsh = MoreObjects.toStringHelper(getClass());

    tsh.add("id", getId());
    tsh.add("contact", contact);
    tsh.add("street", street);
    tsh.add("area", area);
    tsh.add("city", city);
    tsh.add("zip", zip);
    tsh.add("country", country);

    return tsh.omitNullValues().toString();
  }

  public static Query<Address> all() {
    return JPA.all(Address.class);
  }
}
