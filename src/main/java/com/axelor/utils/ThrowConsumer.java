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
package com.axelor.utils;

/**
 * Represents an operation that accepts one argument, returns no result and can throw exception.
 *
 * @param <T> the type of the input to the operation.
 */
@FunctionalInterface
public interface ThrowConsumer<T, E extends Exception> {
  /**
   * Performs this operation on the given argument.
   *
   * @param t the input argument.
   * @throws E the exception that may be thrown.
   */
  void accept(T t) throws E;
}
