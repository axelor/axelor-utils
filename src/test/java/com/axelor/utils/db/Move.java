/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2025 Axelor (<http://axelor.com>).
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
import com.axelor.db.annotations.Widget;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "TEST_MOVE")
public class Move extends JpaModel {

  @Widget(readonly = true)
  @NotNull
  String code;

  @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "move",
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<MoveLine> moveLines;

  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private Invoice invoice;

  @OneToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private MoveReference moveReference;

  public Move persist() {
    return JPA.persist(this);
  }

  public Move merge() {
    return JPA.merge(this);
  }

  public Move save() {
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

  public static Move find(Long id) {
    return JPA.find(Move.class, id);
  }

  public static Query<Move> all() {
    return JPA.all(Move.class);
  }
}
