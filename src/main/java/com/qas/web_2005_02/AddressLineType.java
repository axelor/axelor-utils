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
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe Java pour AddressLineType complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="AddressLineType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Label" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Line" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="LineContent" type="{http://www.qas.com/web-2005-02}LineContentType" default="Address" /&gt;
 *       &lt;attribute name="Overflow" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="Truncated" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "AddressLineType",
    propOrder = {"label", "line"})
public class AddressLineType {

  @Getter
  @XmlElement(name = "Label")
  protected String label;

  @Getter
  @XmlElement(name = "Line")
  protected String line;

  @XmlAttribute(name = "LineContent")
  protected LineContentType lineContent;

  @XmlAttribute(name = "Overflow")
  protected Boolean overflow;

  @XmlAttribute(name = "Truncated")
  protected Boolean truncated;

  public LineContentType getLineContent() {
    return Objects.requireNonNullElse(lineContent, LineContentType.ADDRESS);
  }

  public boolean isOverflow() {
    return Objects.requireNonNullElse(overflow, false);
  }

  public boolean isTruncated() {
    return Objects.requireNonNullElse(truncated, false);
  }
}
