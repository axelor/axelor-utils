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

import java.math.BigInteger;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe Java pour PicklistEntryType complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="PicklistEntryType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Moniker" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="PartialAddress" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Picklist" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Postcode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Score" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="FullAddress" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="Multiples" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="CanStep" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="AliasMatch" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="PostcodeRecoded" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="CrossBorderMatch" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="DummyPOBox" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="Name" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="Information" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="WarnInformation" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="IncompleteAddr" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="UnresolvableRange" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="PhantomPrimaryPoint" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "PicklistEntryType",
    propOrder = {"moniker", "partialAddress", "picklist", "postcode", "score"})
public class PicklistEntryType {

  @Getter
  @XmlElement(name = "Moniker", required = true)
  protected String moniker;

  @Getter
  @XmlElement(name = "PartialAddress", required = true)
  protected String partialAddress;

  @Getter
  @XmlElement(name = "Picklist", required = true)
  protected String picklist;

  @Getter
  @XmlElement(name = "Postcode", required = true)
  protected String postcode;

  @Getter
  @XmlElement(name = "Score", required = true)
  @XmlSchemaType(name = "nonNegativeInteger")
  protected BigInteger score;

  @XmlAttribute(name = "FullAddress")
  protected Boolean fullAddress;

  @XmlAttribute(name = "Multiples")
  protected Boolean multiples;

  @XmlAttribute(name = "CanStep")
  protected Boolean canStep;

  @XmlAttribute(name = "AliasMatch")
  protected Boolean aliasMatch;

  @XmlAttribute(name = "PostcodeRecoded")
  protected Boolean postcodeRecoded;

  @XmlAttribute(name = "CrossBorderMatch")
  protected Boolean crossBorderMatch;

  @XmlAttribute(name = "DummyPOBox")
  protected Boolean dummyPOBox;

  @XmlAttribute(name = "Name")
  protected Boolean name;

  @XmlAttribute(name = "Information")
  protected Boolean information;

  @XmlAttribute(name = "WarnInformation")
  protected Boolean warnInformation;

  @XmlAttribute(name = "IncompleteAddr")
  protected Boolean incompleteAddr;

  @XmlAttribute(name = "UnresolvableRange")
  protected Boolean unresolvableRange;

  @XmlAttribute(name = "PhantomPrimaryPoint")
  protected Boolean phantomPrimaryPoint;

  public boolean isFullAddress() {
    return Objects.requireNonNullElse(fullAddress, false);
  }

  public boolean isMultiples() {
    return Objects.requireNonNullElse(multiples, false);
  }

  public boolean isCanStep() {
    return Objects.requireNonNullElse(canStep, false);
  }

  public boolean isAliasMatch() {
    return Objects.requireNonNullElse(aliasMatch, false);
  }

  public boolean isPostcodeRecoded() {
    return Objects.requireNonNullElse(postcodeRecoded, false);
  }

  public boolean isCrossBorderMatch() {
    return Objects.requireNonNullElse(crossBorderMatch, false);
  }

  public boolean isDummyPOBox() {
    return Objects.requireNonNullElse(dummyPOBox, false);
  }

  public boolean isName() {
    if (name == null) {
      return false;
    } else {
      return name;
    }
  }

  public boolean isInformation() {
    return Objects.requireNonNullElse(information, false);
  }

  public boolean isWarnInformation() {
    return Objects.requireNonNullElse(warnInformation, false);
  }

  public boolean isIncompleteAddr() {
    return Objects.requireNonNullElse(incompleteAddr, false);
  }

  public boolean isUnresolvableRange() {
    return Objects.requireNonNullElse(unresolvableRange, false);
  }

  public boolean isPhantomPrimaryPoint() {
    return Objects.requireNonNullElse(phantomPrimaryPoint, false);
  }
}
