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
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe Java pour QAPicklistType complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="QAPicklistType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="FullPicklistMoniker" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="PicklistEntry" type="{http://www.qas.com/web-2005-02}PicklistEntryType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="Prompt" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Total" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="AutoFormatSafe" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="AutoFormatPastClose" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="AutoStepinSafe" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="AutoStepinPastClose" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="LargePotential" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="MaxMatches" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="MoreOtherMatches" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="OverThreshold" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="Timeout" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "QAPicklistType",
    propOrder = {"fullPicklistMoniker", "picklistEntry", "prompt", "total"})
public class QAPicklistType {

  @Setter
  @Getter
  @XmlElement(name = "FullPicklistMoniker", required = true)
  protected String fullPicklistMoniker;

  @XmlElement(name = "PicklistEntry")
  protected List<PicklistEntryType> picklistEntry;

  @Setter
  @Getter
  @XmlElement(name = "Prompt", required = true)
  protected String prompt;

  @Setter
  @Getter
  @XmlElement(name = "Total", required = true)
  @XmlSchemaType(name = "nonNegativeInteger")
  protected BigInteger total;

  @Setter
  @XmlAttribute(name = "AutoFormatSafe")
  protected Boolean autoFormatSafe;

  @Setter
  @XmlAttribute(name = "AutoFormatPastClose")
  protected Boolean autoFormatPastClose;

  @Setter
  @XmlAttribute(name = "AutoStepinSafe")
  protected Boolean autoStepinSafe;

  @Setter
  @XmlAttribute(name = "AutoStepinPastClose")
  protected Boolean autoStepinPastClose;

  @Setter
  @XmlAttribute(name = "LargePotential")
  protected Boolean largePotential;

  @Setter
  @XmlAttribute(name = "MaxMatches")
  protected Boolean maxMatches;

  @Setter
  @XmlAttribute(name = "MoreOtherMatches")
  protected Boolean moreOtherMatches;

  @Setter
  @XmlAttribute(name = "OverThreshold")
  protected Boolean overThreshold;

  @Setter
  @XmlAttribute(name = "Timeout")
  protected Boolean timeout;

  /**
   * Gets the value of the picklistEntry property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the picklistEntry property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   *    getPicklistEntry().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link PicklistEntryType }
   *
   * @return All picklist entries.
   */
  public List<PicklistEntryType> getPicklistEntry() {
    if (picklistEntry == null) {
      picklistEntry = new ArrayList<>();
    }
    return this.picklistEntry;
  }

  public boolean isAutoFormatSafe() {
    return Objects.requireNonNullElse(autoFormatSafe, false);
  }

  public boolean isAutoFormatPastClose() {
    return Objects.requireNonNullElse(autoFormatPastClose, false);
  }

  public boolean isAutoStepinSafe() {
    return Objects.requireNonNullElse(autoStepinSafe, false);
  }

  public boolean isAutoStepinPastClose() {
    return Objects.requireNonNullElse(autoStepinPastClose, false);
  }

  public boolean isLargePotential() {
    return Objects.requireNonNullElse(largePotential, false);
  }

  public boolean isMaxMatches() {
    return Objects.requireNonNullElse(maxMatches, false);
  }

  public boolean isMoreOtherMatches() {
    return Objects.requireNonNullElse(moreOtherMatches, false);
  }

  public boolean isOverThreshold() {
    return Objects.requireNonNullElse(overThreshold, false);
  }

  public boolean isTimeout() {
    return Objects.requireNonNullElse(timeout, false);
  }
}
