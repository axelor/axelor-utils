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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
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
 *         &lt;element name="Moniker" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Refinement" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="QAConfig" type="{http://www.qas.com/web-2005-02}QAConfigType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="Threshold" type="{http://www.qas.com/web-2005-02}ThresholdType" /&gt;
 *       &lt;attribute name="Timeout" type="{http://www.qas.com/web-2005-02}TimeoutType" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {"moniker", "refinement", "qaConfig"})
@XmlRootElement(name = "QARefine")
public class QARefine {

  @Setter
  @Getter
  @XmlElement(name = "Moniker", required = true)
  protected String moniker;

  @Setter
  @Getter
  @XmlElement(name = "Refinement", required = true)
  protected String refinement;

  @XmlElement(name = "QAConfig")
  protected QAConfigType qaConfig;

  @Setter
  @Getter
  @XmlAttribute(name = "Threshold")
  protected Integer threshold;

  @Setter
  @Getter
  @XmlAttribute(name = "Timeout")
  protected Integer timeout;

  public QAConfigType getQAConfig() {
    return qaConfig;
  }

  public void setQAConfig(QAConfigType value) {
    this.qaConfig = value;
  }
}
