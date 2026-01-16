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
package com.qas.web_2005_02;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Classe Java pour PromptSetType.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;simpleType name="PromptSetType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="OneLine"/&gt;
 *     &lt;enumeration value="Default"/&gt;
 *     &lt;enumeration value="Generic"/&gt;
 *     &lt;enumeration value="Optimal"/&gt;
 *     &lt;enumeration value="Alternate"/&gt;
 *     &lt;enumeration value="Alternate2"/&gt;
 *     &lt;enumeration value="Alternate3"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 */
@XmlType(name = "PromptSetType")
@XmlEnum
public enum PromptSetType {
  @XmlEnumValue("OneLine")
  ONE_LINE("OneLine"),
  @XmlEnumValue("Default")
  DEFAULT("Default"),
  @XmlEnumValue("Generic")
  GENERIC("Generic"),
  @XmlEnumValue("Optimal")
  OPTIMAL("Optimal"),
  @XmlEnumValue("Alternate")
  ALTERNATE("Alternate"),
  @XmlEnumValue("Alternate2")
  ALTERNATE_2("Alternate2"),
  @XmlEnumValue("Alternate3")
  ALTERNATE_3("Alternate3");
  private final String value;

  PromptSetType(String v) {
    value = v;
  }

  public String value() {
    return value;
  }

  public static PromptSetType fromValue(String v) {
    for (PromptSetType c : PromptSetType.values()) {
      if (c.value.equals(v)) {
        return c;
      }
    }
    throw new IllegalArgumentException(v);
  }
}
