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
import com.axelor.db.Model;
import com.axelor.db.Query;
import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "TEST_MOVE_REFERENCE")
public class MoveReference extends Model {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEST_MOVE_REFERENCE_SEQ")
  @SequenceGenerator(
      name = "TEST_MOVE_REFERENCE_SEQ",
      sequenceName = "TEST_MOVE_REFERENCE_SEQ",
      allocationSize = 1)
  private Long id;

  @NotNull private String code;

  @NotNull private String name;

  public MoveReference() {}

  public MoveReference(String name, String code) {
    this.name = name;
    this.code = code;
  }

  @Override
  public String toString() {
    ToStringHelper tsh = MoreObjects.toStringHelper(getClass());

    tsh.add("id", getId());
    tsh.add("code", code);
    tsh.add("name", name);

    return tsh.omitNullValues().toString();
  }

  public static Query<Country> all() {
    return JPA.all(Country.class);
  }
}
