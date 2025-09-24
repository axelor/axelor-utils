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
package com.qas.web_2005_02;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Classe Java pour LineContentType.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;simpleType name="LineContentType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="None"/&gt;
 *     &lt;enumeration value="Address"/&gt;
 *     &lt;enumeration value="Name"/&gt;
 *     &lt;enumeration value="Ancillary"/&gt;
 *     &lt;enumeration value="DataPlus"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 */
@XmlType(name = "LineContentType")
@XmlEnum
public enum LineContentType {
  @XmlEnumValue("None")
  NONE("None"),
  @XmlEnumValue("Address")
  ADDRESS("Address"),
  @XmlEnumValue("Name")
  NAME("Name"),
  @XmlEnumValue("Ancillary")
  ANCILLARY("Ancillary"),
  @XmlEnumValue("DataPlus")
  DATA_PLUS("DataPlus");
  private final String value;

  LineContentType(String v) {
    value = v;
  }

  public String value() {
    return value;
  }

  public static LineContentType fromValue(String v) {
    for (LineContentType c : LineContentType.values()) {
      if (c.value.equals(v)) {
        return c;
      }
    }
    throw new IllegalArgumentException(v);
  }
}
