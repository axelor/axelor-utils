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

import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
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
 *         &lt;element name="QAPicklist" type="{http://www.qas.com/web-2005-02}QAPicklistType" minOccurs="0"/&gt;
 *         &lt;element name="QAAddress" type="{http://www.qas.com/web-2005-02}QAAddressType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="VerifyLevel" type="{http://www.qas.com/web-2005-02}VerifyLevelType" default="None" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {"qaPicklist", "qaAddress"})
@XmlRootElement(name = "QASearchResult")
public class QASearchResult {

  @XmlElement(name = "QAPicklist")
  protected QAPicklistType qaPicklist;

  @XmlElement(name = "QAAddress")
  protected QAAddressType qaAddress;

  @Setter
  @XmlAttribute(name = "VerifyLevel")
  protected VerifyLevelType verifyLevel;

  public QAPicklistType getQAPicklist() {
    return qaPicklist;
  }

  public void setQAPicklist(QAPicklistType value) {
    this.qaPicklist = value;
  }

  public QAAddressType getQAAddress() {
    return qaAddress;
  }

  public void setQAAddress(QAAddressType value) {
    this.qaAddress = value;
  }

  public VerifyLevelType getVerifyLevel() {
    return Objects.requireNonNullElse(verifyLevel, VerifyLevelType.NONE);
  }
}
