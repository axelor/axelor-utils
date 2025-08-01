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
package com.axelor.utils.helpers;

import com.axelor.utils.helpers.date.LocalDateHelper;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Helper to simplify the use of numbers. */
public final class DecimalHelper {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private DecimalHelper() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Check if the decimal is null or equal to 0.
   *
   * @param decimal The decimal to check.
   * @return True if the decimal is null or equal to 0.
   */
  public static boolean isNullOrZero(BigDecimal decimal) {
    return decimal == null || decimal.signum() == 0;
  }

  /**
   * If the decimal is null, return 0. Otherwise, return its value.
   *
   * @param decimal The decimal to check.
   * @return 0 if the decimal is null, otherwise its value.
   */
  public static BigDecimal getZeroOrValue(BigDecimal decimal) {
    return decimal == null ? BigDecimal.ZERO : decimal;
  }

  /**
   * Répartir proportionnellement selon un prorata une valeur en fonction de dates.
   *
   * @param fromDate Date de début de la période de conso.
   * @param toDate Date de fin de la période de conso.
   * @param date Date de référence.
   * @param value Valeur initiale.
   * @param scale Précision.
   * @return La quantité répartie.
   */
  public static BigDecimal prorata(
      LocalDate fromDate, LocalDate toDate, LocalDate date, BigDecimal value, int scale) {

    BigDecimal prorataValue = BigDecimal.ZERO;

    if (fromDate == null || toDate == null || date == null) {
      return prorataValue;
    }

    BigDecimal totalDays = new BigDecimal(LocalDateHelper.daysBetween(fromDate, toDate, false));
    BigDecimal days = new BigDecimal(LocalDateHelper.daysBetween(date, toDate, false));

    prorataValue = prorata(totalDays, days, value, scale);

    LOG.debug("Prorata ({} from {} to {}) at {} : {}", value, fromDate, toDate, date, prorataValue);

    return prorataValue;
  }

  /**
   * Répartir proportionnellement selon un prorata une valeur en fonction du nombre de jours. (Règle
   * de 3)
   *
   * @param totalDays Le nombre total de jours.
   * @param days Le nombre de jours.
   * @param value La valeur initiale.
   * @param scale Précision.
   * @return La valeur répartie.
   */
  public static BigDecimal prorata(
      BigDecimal totalDays, BigDecimal days, BigDecimal value, int scale) {

    BigDecimal prorataValue = BigDecimal.ZERO;

    if (totalDays.compareTo(prorataValue) == 0) {
      return prorataValue;
    } else {
      prorataValue =
          (days.multiply(value).divide(totalDays, scale, RoundingMode.HALF_UP))
              .setScale(scale, RoundingMode.HALF_UP);
    }

    LOG.debug(
        "Prorata of a value on a total days of {} for {} days and a value of {} : {}",
        totalDays,
        days,
        value,
        prorataValue);

    return prorataValue;
  }

  public static BigDecimal prorata(
      LocalDate fromDate, LocalDate toDate, LocalDate date, BigDecimal value) {

    return prorata(fromDate, toDate, date, value, 2);
  }

  public static BigDecimal prorata(BigDecimal totalDays, BigDecimal days, BigDecimal value) {

    return prorata(totalDays, days, value, 2);
  }

  /**
   * Fonction permettant d'obtenir le pourcentage d'une valeur.
   *
   * @param value Valeur initiale.
   * @param percent Pourcentage (format : 10%).
   * @param scale Précision.
   * @return Le pourcentage de la valeur initiale.
   */
  public static BigDecimal percent(BigDecimal value, BigDecimal percent, int scale) {

    return value.multiply(percent).divide(new BigDecimal("100"), scale, RoundingMode.HALF_UP);
  }
}
