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

import com.axelor.utils.db.Contact;
import com.axelor.utils.db.Title;
import com.axelor.utils.junit.BaseTest;
import com.axelor.utils.template.TemplateMaker;
import com.google.common.collect.Maps;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class STTest extends BaseTest {

  private static final String DATE_FORMAT_PATTERN = "dd/MM/yyyy";
  private static final String TIME_FORMAT_PATTERN = "HH";

  public Contact contact;
  public String contentFinal;
  public final Map<String, Object> map = Maps.newHashMap();
  public final String content =
      """
    <h1>About Me ($contact.lastName;format="upper"$)</h1>
    <hr />
    <p><strong>PayeurQuality:</strong> $contact.payeurQuality;format="%,2.3f"$</p>
    <p><strong>Title:</strong> $contact.title$</p>
    <p><strong>First Name:</strong> $contact.firstName$</p>
    <p><strong>Last Name:</strong> $contact.lastName;format="upper"$</p>
    <p><strong>DateOfBirth:</strong> $contact.dateOfBirth;format="dd/MM/yyyy"$</p>
    <p>&nbsp;</p>
    <p><em>Contact me:</em>&nbsp;<a href='mailto:$contact.email$' target='_blank'>$contact.fullName$</a></p>
    <hr />$__time__;format="HH"$
    <ul>$__date__$
    <li>Java</li>
    <li>JavaScript</li>
    <li>Groovy</li>
    <li>HTML5</li>
    </ul>
    <pre>public class Hello {<br />
      private String testKey1 = $testKey1$<br />
      private String testKey2 = $testKey2$<br />
      private String testKey3 = $testKey3$<br />
    }</pre>
    """;

  @BeforeEach
  void prepareTest() {
    contact = new Contact("Doe", "John");
    contact.setEmail("john.doe@axelor.com");
    contact.setFullName(contact.getFullName());
    contact.setDateOfBirth(LocalDate.now());
    contact.setPayeurQuality(new BigDecimal("2.2569"));
    contact.setLanguage("fr");

    Title title = new Title("TitleName", "TitleCode");
    contact.setTitle(title);

    map.put("testKey1", "This is the key 1");
    map.put("testKey2", "This is the key 2");
    map.put("testKey3", "This is the key 3");

    final String HTML_TEMPLATE =
        """
      <h1>About Me (JOHN)</h1>
      <hr />
      <p><strong>PayeurQuality:</strong> 2,257</p>
      <p><strong>Title:</strong> %s</p>
      <p><strong>First Name:</strong> %s</p>
      <p><strong>Last Name:</strong> %s</p>
      <p><strong>DateOfBirth:</strong> %s</p>
      <p>&nbsp;</p>
      <p><em>Contact me:</em>&nbsp;<a href='mailto:%s' target='_blank'>%s</a></p>
      <hr />%s
      <ul>%s
      <li>Java</li>
      <li>JavaScript</li>
      <li>Groovy</li>
      <li>HTML5</li>
      </ul>
      <pre>public class Hello {<br />
        private String testKey1 = %s<br />
        private String testKey2 = %s<br />
        private String testKey3 = %s<br />
      }</pre>
      """;

    contentFinal =
        String.format(
            HTML_TEMPLATE,
            title,
            contact.getFirstName(),
            contact.getLastName().toUpperCase(),
            contact.getDateOfBirth().format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN)),
            contact.getEmail(),
            contact.getFullName(),
            LocalTime.now().format(DateTimeFormatter.ofPattern(TIME_FORMAT_PATTERN)),
            LocalDate.now(),
            map.get("testKey1"),
            map.get("testKey2"),
            map.get("testKey3"));
  }

  @Test
  void test1() {
    TemplateMaker maker = new TemplateMaker("Europe/Paris", Locale.FRENCH, '$', '$');

    maker.setTemplate(content);
    maker.setContext(contact, map, "contact");
    String result = maker.make();
    Assertions.assertNotNull(result);
    Assertions.assertEquals(contentFinal, result);
  }

  @Test
  void test2() {
    long start = System.currentTimeMillis();

    TemplateMaker maker = new TemplateMaker("Europe/Paris", Locale.FRENCH, '$', '$');

    for (int i = 0; i < 10000; i++) {
      maker.setTemplate(content);
      maker.setContext(contact, map, "contact");
      String result = maker.make();

      Assertions.assertNotNull(result);
      Assertions.assertEquals(contentFinal, result);
    }

    // Assert test total time < 15s
    Assertions.assertTrue(((System.currentTimeMillis() - start) / 1000) < 15);
  }

  @Test
  void test3() {
    for (int i = 0; i < 10; i++) {
      ThreadTest thread = new ThreadTest();
      thread.start();
    }
  }

  class ThreadTest extends Thread {
    @Override
    public void run() {
      long start = System.currentTimeMillis();

      TemplateMaker maker = new TemplateMaker("Europe/Paris", Locale.FRENCH, '$', '$');

      for (int i = 0; i < 10000; i++) {
        maker.setTemplate(content);
        maker.setContext(contact, map, "contact");
        String result = maker.make();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(contentFinal, result);
      }

      // Assert test total time < 15s
      Assertions.assertTrue(((System.currentTimeMillis() - start) / 1000) < 15);
    }
  }
}
