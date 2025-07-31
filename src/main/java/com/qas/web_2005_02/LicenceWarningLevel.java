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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * Classe Java pour LicenceWarningLevel.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;simpleType name="LicenceWarningLevel"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="None"/&gt;
 *     &lt;enumeration value="DataExpiring"/&gt;
 *     &lt;enumeration value="LicenceExpiring"/&gt;
 *     &lt;enumeration value="ClicksLow"/&gt;
 *     &lt;enumeration value="Evaluation"/&gt;
 *     &lt;enumeration value="NoClicks"/&gt;
 *     &lt;enumeration value="DataExpired"/&gt;
 *     &lt;enumeration value="EvalLicenceExpired"/&gt;
 *     &lt;enumeration value="FullLicenceExpired"/&gt;
 *     &lt;enumeration value="LicenceNotFound"/&gt;
 *     &lt;enumeration value="DataUnreadable"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 */
@XmlType(name = "LicenceWarningLevel")
@XmlEnum
public enum LicenceWarningLevel {
  @XmlEnumValue("None")
  NONE("None"),
  @XmlEnumValue("DataExpiring")
  DATA_EXPIRING("DataExpiring"),
  @XmlEnumValue("LicenceExpiring")
  LICENCE_EXPIRING("LicenceExpiring"),
  @XmlEnumValue("ClicksLow")
  CLICKS_LOW("ClicksLow"),
  @XmlEnumValue("Evaluation")
  EVALUATION("Evaluation"),
  @XmlEnumValue("NoClicks")
  NO_CLICKS("NoClicks"),
  @XmlEnumValue("DataExpired")
  DATA_EXPIRED("DataExpired"),
  @XmlEnumValue("EvalLicenceExpired")
  EVAL_LICENCE_EXPIRED("EvalLicenceExpired"),
  @XmlEnumValue("FullLicenceExpired")
  FULL_LICENCE_EXPIRED("FullLicenceExpired"),
  @XmlEnumValue("LicenceNotFound")
  LICENCE_NOT_FOUND("LicenceNotFound"),
  @XmlEnumValue("DataUnreadable")
  DATA_UNREADABLE("DataUnreadable");
  private final String value;

  LicenceWarningLevel(String v) {
    value = v;
  }

  public String value() {
    return value;
  }

  public static LicenceWarningLevel fromValue(String v) {
    for (LicenceWarningLevel c : LicenceWarningLevel.values()) {
      if (c.value.equals(v)) {
        return c;
      }
    }
    throw new IllegalArgumentException(v);
  }
}
