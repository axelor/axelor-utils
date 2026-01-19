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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe Java pour EngineType complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="EngineType"&gt;
 *   &lt;simpleContent&gt;
 *     &lt;extension base="&lt;http://www.qas.com/web-2005-02&gt;EngineEnumType"&gt;
 *       &lt;attribute name="Flatten" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="Intensity" type="{http://www.qas.com/web-2005-02}EngineIntensityType" /&gt;
 *       &lt;attribute name="PromptSet" type="{http://www.qas.com/web-2005-02}PromptSetType" /&gt;
 *       &lt;attribute name="Threshold" type="{http://www.qas.com/web-2005-02}ThresholdType" /&gt;
 *       &lt;attribute name="Timeout" type="{http://www.qas.com/web-2005-02}TimeoutType" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/simpleContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "EngineType",
    propOrder = {"value"})
public class EngineType {

  @Getter @XmlValue protected EngineEnumType value;

  @XmlAttribute(name = "Flatten")
  protected Boolean flatten;

  @Getter
  @XmlAttribute(name = "Intensity")
  protected EngineIntensityType intensity;

  @Getter
  @XmlAttribute(name = "PromptSet")
  protected PromptSetType promptSet;

  @Getter
  @XmlAttribute(name = "Threshold")
  protected Integer threshold;

  @Getter
  @XmlAttribute(name = "Timeout")
  protected Integer timeout;

  public Boolean isFlatten() {
    return flatten;
  }
}
