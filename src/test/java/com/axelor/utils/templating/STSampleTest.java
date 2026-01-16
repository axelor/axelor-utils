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
package com.axelor.utils.templating;

import com.axelor.db.Model;
import com.google.common.base.Strings;
import java.util.Locale;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.stringtemplate.v4.AttributeRenderer;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

class STSampleTest {

  public final String template = "Hi $contact.name;format=\"upper\"$ $contact.lastName$";

  private static final int MAX_ITER = 5000;

  private static final char CHAR = '$';

  private STGroup stGroup;

  @Test
  void test() {
    stGroup = new STGroup(CHAR, CHAR);
    stGroup.registerRenderer(String.class, new BasicFormatRenderer<>());

    for (int i = 0; i < MAX_ITER; i++) {
      Contact contact = new Contact("Name" + i, "LastName" + i);
      String result = run(contact);

      String expected = "Hi " + contact.getName().toUpperCase() + " " + contact.getLastName();
      Assertions.assertEquals(expected, result);
    }
  }

  public String run(Contact o) {
    ST st = new ST(stGroup, template);
    st.add("contact", o);

    return st.render();
  }

  static class BasicFormatRenderer<T> implements AttributeRenderer<T> {

    public String toString(Object o) {
      return o.toString();
    }

    public String toString(Object o, String formatName) {

      if (Strings.isNullOrEmpty(formatName)) {
        return toString(o);
      }

      if (formatName.equals("upper")) {
        return o.toString().toUpperCase();
      } else if (formatName.equals("toLower")) {
        return o.toString().toLowerCase();
      }
      return toString(o);
    }

    @Override
    public String toString(Object o, String formatString, Locale locale) {
      if (o == null) {
        return null;
      }
      return toString(o, formatString);
    }
  }

  @Getter
  @Setter
  static class Contact extends Model {
    private Long id;
    private String name;
    private String lastName;

    public Contact(String name, String lastName) {
      this.name = name;
      this.lastName = lastName;
    }
  }
}
