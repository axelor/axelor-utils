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

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe Java pour anonymous complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="WarningLevel" type="{http://www.qas.com/web-2005-02}LicenceWarningLevel"/&gt;
 *         &lt;element name="LicensedSet" type="{http://www.qas.com/web-2005-02}QALicensedSet" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {"warningLevel", "licensedSet"})
@XmlRootElement(name = "QALicenceInfo")
public class QALicenceInfo {

  @Setter
  @Getter
  @XmlElement(name = "WarningLevel", required = true)
  protected LicenceWarningLevel warningLevel;

  @XmlElement(name = "LicensedSet")
  protected List<QALicensedSet> licensedSet;

  /**
   * Gets the value of the licensedSet property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the licensedSet property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   *    getLicensedSet().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link QALicensedSet }
   *
   * @return All licensed sets.
   */
  public List<QALicensedSet> getLicensedSet() {
    if (licensedSet == null) {
      licensedSet = new ArrayList<>();
    }
    return this.licensedSet;
  }
}
