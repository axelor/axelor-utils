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
package com.axelor.utils.api;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.ws.rs.BadRequestException;
import java.util.Set;

public class RequestValidator {

  public static void validateBody(RequestStructure body) {
    Validator validator;
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
    }

    Set<ConstraintViolation<RequestStructure>> constraintViolations = validator.validate(body);

    if (!constraintViolations.isEmpty()) {
      StringBuilder errorMsg = new StringBuilder("Some constraints are invalid : ");
      for (ConstraintViolation<RequestStructure> constraint : constraintViolations) {
        errorMsg
            .append(constraint.getPropertyPath())
            .append(" ")
            .append(constraint.getMessage())
            .append("; ");
      }
      throw new BadRequestException(String.valueOf(errorMsg));
    }
  }

  public static void validateBody(RequestPostStructure body) {
    Validator validator;
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
    }

    Set<ConstraintViolation<RequestPostStructure>> constraintViolations = validator.validate(body);

    if (!constraintViolations.isEmpty()) {
      StringBuilder errorMsg = new StringBuilder("Some constraints are invalid : ");
      for (ConstraintViolation<RequestPostStructure> constraint : constraintViolations) {
        errorMsg
            .append(constraint.getPropertyPath())
            .append(" ")
            .append(constraint.getMessage())
            .append("; ");
      }
      throw new BadRequestException(String.valueOf(errorMsg));
    }
  }
}
