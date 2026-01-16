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

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Setter;

/**
 * Classe Java pour QAAddressType complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="QAAddressType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AddressLine" type="{http://www.qas.com/web-2005-02}AddressLineType" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="Overflow" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="Truncated" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "QAAddressType",
    propOrder = {"addressLine"})
public class QAAddressType {

  @XmlElement(name = "AddressLine", required = true)
  protected List<AddressLineType> addressLine;

  @Setter
  @XmlAttribute(name = "Overflow")
  protected Boolean overflow;

  @Setter
  @XmlAttribute(name = "Truncated")
  protected Boolean truncated;

  /**
   * Gets the value of the addressLine property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the addressLine property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   *    getAddressLine().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link AddressLineType }
   *
   * @return All address lines.
   */
  public List<AddressLineType> getAddressLine() {
    if (addressLine == null) {
      addressLine = new ArrayList<>();
    }
    return this.addressLine;
  }

  public boolean isOverflow() {
    return Objects.requireNonNullElse(overflow, false);
  }

  public boolean isTruncated() {
    return Objects.requireNonNullElse(truncated, false);
  }
}
