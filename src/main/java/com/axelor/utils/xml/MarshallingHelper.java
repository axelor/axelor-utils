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
package com.axelor.utils.xml;

import com.axelor.utils.helpers.file.FileHelper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import org.eclipse.persistence.internal.oxm.NamespacePrefixMapper;

public final class MarshallingHelper {

  private MarshallingHelper() {}

  public static void marshallOutputStream(Object jaxbElement, String context) throws JAXBException {

    JAXBContext jaxbContext = JAXBContext.newInstance(context);
    marshallOutputStream(jaxbElement, jaxbContext);
  }

  public static void marshallOutputStream(Object jaxbElement, JAXBContext jaxbContext)
      throws JAXBException {

    Marshaller marshaller = jaxbContext.createMarshaller();

    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    marshaller.marshal(jaxbElement, System.out);
  }

  public static void marshall(Object jaxbElement, String context, StringWriter writer)
      throws JAXBException {

    JAXBContext jaxbContext = JAXBContext.newInstance(context);
    marshall(jaxbElement, jaxbContext, writer);
  }

  public static void marshall(Object jaxbElement, JAXBContext jaxbContext, StringWriter sw)
      throws JAXBException {

    Marshaller marshaller = jaxbContext.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
    sw.write("<?xml version=\"1.0\" encoding=\"utf-8\"?> \n");
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    marshaller.setProperty(
        "com.sun.xml.bind.namespacePrefixMapper", new CustomNamespacePrefixMapper());
    marshaller.marshal(jaxbElement, sw);
  }

  public static File marshallFile(
      Object jaxbElement, String context, String destinationFolder, String fileName)
      throws JAXBException {

    JAXBContext jaxbContext = JAXBContext.newInstance(context);
    return marshallFile(jaxbElement, jaxbContext, destinationFolder, fileName);
  }

  public static File marshallFile(
      Object jaxbElement, JAXBContext jaxbContext, String destinationFolder, String fileName)
      throws JAXBException {

    Marshaller marshaller = jaxbContext.createMarshaller();

    marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

    File file = FileHelper.create(destinationFolder, fileName);
    marshaller.marshal(jaxbElement, file);

    return file;
  }

  public static Object unmarshallFile(String context, String data) throws JAXBException {

    JAXBContext jc = JAXBContext.newInstance(context);

    return unmarshallFile(jc, data);
  }

  public static Object unmarshallFile(JAXBContext jaxbContext, String data) throws JAXBException {

    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
    StringReader reader = new StringReader(data);

    return unmarshaller.unmarshal(reader);
  }

  /**
   * Coerces the JAXB marshaller to declare the "xsi" and "xsd" namespaces at the root element
   * instead of putting them inline on each element that uses one of the namespaces.
   */
  private static class CustomNamespacePrefixMapper extends NamespacePrefixMapper {

    @Override
    public String getPreferredPrefix(
        String namespaceUri, String suggestion, boolean requirePrefix) {
      return suggestion;
    }

    @Override
    public String[] getPreDeclaredNamespaceUris2() {
      return new String[] {
        "xsi",
        "http://www.w3.org/2001/XMLSchema-instance",
        "xsd",
        "http://www.w3.org/2001/XMLSchema"
      };
    }
  }
}
