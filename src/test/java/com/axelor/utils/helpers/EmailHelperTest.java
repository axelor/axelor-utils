package com.axelor.utils.helpers;

import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class EmailHelperTest {

  @Test
  @DisplayName("Null email address should be invalid")
  void testNullEmail() {
    Assertions.assertFalse(EmailHelper.isValidEmailAddress(null), "Null email should be invalid");
  }

  @Test
  @DisplayName("Empty email address should be invalid")
  void testEmptyEmail() {
    Assertions.assertFalse(EmailHelper.isValidEmailAddress(""), "Empty email should be invalid");
  }

  @ParameterizedTest(name = "{0} should be valid")
  @MethodSource("validEmailProvider")
  @DisplayName("Valid email addresses should be accepted")
  void testValidEmails(String email, String description) {
    Assertions.assertTrue(
        EmailHelper.isValidEmailAddress(email), email + " should be valid (" + description + ")");
  }

  @ParameterizedTest(name = "{0} should be invalid")
  @MethodSource("invalidEmailProvider")
  @DisplayName("Invalid email addresses should be rejected")
  void testInvalidEmails(String email, String description) {
    Assertions.assertFalse(
        EmailHelper.isValidEmailAddress(email), email + " should be invalid (" + description + ")");
  }

  static Stream<Arguments> validEmailProvider() {
    return Stream.of(
        // Standard emails
        Arguments.of("test@example.com", "simple email"),
        Arguments.of("user@domain.org", "simple with .org TLD"),
        Arguments.of("name@company.co.uk", "two-part TLD"),

        // Emails with dashes in domain
        Arguments.of("admindv@hdf.my-company.fr", "dash in intermediate segment"),
        Arguments.of(
            "admindv@hdf.my-company-epargne.fr", "multiple dashes in intermediate segment"),
        Arguments.of("user@sub-domain.example.com", "dash in first domain segment"),
        Arguments.of("test@my-company.co.uk", "dash in domain with two-part TLD"),
        Arguments.of("info@a-b-c.d-e-f.example.com", "multiple dashes in multiple segments"),
        Arguments.of(
            "user@sub-one.sub-two.example.com", "multiple intermediate segments with dashes"),

        // Complex local parts
        Arguments.of("first.last@example.com", "dot in local part"),
        Arguments.of("user+tag@example.com", "plus sign in local part"),
        Arguments.of("user-name@example.com", "dash in local part"),
        Arguments.of("user_name@example.com", "underscore in local part"),

        // Multiple subdomains
        Arguments.of("user@a.b.c.example.com", "multiple subdomains"),
        Arguments.of("user@mail.sub.domain.example.org", "deep subdomain hierarchy"));
  }

  static Stream<Arguments> invalidEmailProvider() {
    return Stream.of(
        // Missing parts
        Arguments.of("plainaddress", "no @ symbol"),
        Arguments.of("@example.com", "no local part"),
        Arguments.of("user@", "no domain"),
        Arguments.of("user@.com", "domain starts with dot"),

        // Invalid characters
        Arguments.of("user name@example.com", "space in local part"),
        Arguments.of("user@exam ple.com", "space in domain"),

        // Invalid TLD
        Arguments.of("user@example.c", "single char TLD"));
  }
}
