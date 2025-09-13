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
package com.axelor.utils.service;

public interface TranslationService {
  /**
   * Update formated value translation.
   *
   * @param oldKey The old key to be updated.
   * @param format The new format.
   * @param args The format arguments.
   */
  void updateFormatedValueTranslations(String oldKey, String format, Object... args);

  /**
   * Create formated value translation.
   *
   * @param format The format.
   * @param args The format arguments.
   */
  void createFormatedValueTranslations(String format, Object... args);

  /**
   * Remove value translation.
   *
   * @param key The key to be removed.
   */
  void removeValueTranslations(String key);

  /**
   * Get the translation of the given key.
   *
   * @param key The key to be translated.
   * @param language The language.
   * @return The translation.
   */
  String getTranslation(String key, String language);

  /**
   * Get the translation key of the given message.
   *
   * @param message The message to be translated.
   * @param language The language.
   * @return The translation key.
   */
  String getTranslationKey(String message, String language);

  /**
   * Get the translation of the given value key.
   *
   * @param key The key to be translated.
   * @param language The language.
   * @return The translation.
   */
  String getValueTranslation(String key, String language);
}
