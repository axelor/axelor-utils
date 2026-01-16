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
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import java.math.BigInteger;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe Java pour QALicensedSet complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="QALicensedSet"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Copyright" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Version" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="BaseCountry" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Status" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Server" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="WarningLevel" type="{http://www.qas.com/web-2005-02}LicenceWarningLevel"/&gt;
 *         &lt;element name="DaysLeft" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/&gt;
 *         &lt;element name="DataDaysLeft" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/&gt;
 *         &lt;element name="LicenceDaysLeft" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "QALicensedSet",
    propOrder = {
      "id",
      "description",
      "copyright",
      "version",
      "baseCountry",
      "status",
      "server",
      "warningLevel",
      "daysLeft",
      "dataDaysLeft",
      "licenceDaysLeft"
    })
public class QALicensedSet {

  @XmlElement(name = "ID", required = true)
  protected String id;

  @Setter
  @Getter
  @XmlElement(name = "Description", required = true)
  protected String description;

  @Setter
  @Getter
  @XmlElement(name = "Copyright", required = true)
  protected String copyright;

  @Setter
  @Getter
  @XmlElement(name = "Version", required = true)
  protected String version;

  @Setter
  @Getter
  @XmlElement(name = "BaseCountry", required = true)
  protected String baseCountry;

  @Setter
  @Getter
  @XmlElement(name = "Status", required = true)
  protected String status;

  @Setter
  @Getter
  @XmlElement(name = "Server", required = true)
  protected String server;

  @Setter
  @Getter
  @XmlElement(name = "WarningLevel", required = true)
  protected LicenceWarningLevel warningLevel;

  @Setter
  @Getter
  @XmlElement(name = "DaysLeft", required = true)
  @XmlSchemaType(name = "nonNegativeInteger")
  protected BigInteger daysLeft;

  @Setter
  @Getter
  @XmlElement(name = "DataDaysLeft", required = true)
  @XmlSchemaType(name = "nonNegativeInteger")
  protected BigInteger dataDaysLeft;

  @Setter
  @Getter
  @XmlElement(name = "LicenceDaysLeft", required = true)
  @XmlSchemaType(name = "nonNegativeInteger")
  protected BigInteger licenceDaysLeft;

  public String getID() {
    return id;
  }

  public void setID(String value) {
    this.id = value;
  }
}
