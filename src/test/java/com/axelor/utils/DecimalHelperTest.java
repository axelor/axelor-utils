package com.axelor.utils;

import com.axelor.utils.helpers.DecimalHelper;
import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DecimalHelperTest {

  @Test
  void isNullOrZero_when_decimal_is_null() {

    Assertions.assertTrue(DecimalHelper.isNullOrZero(null));
  }

  @Test
  void isNullOrZero_when_decimal_is_zero() {

    BigDecimal zeroFromBigDecimalConstant = BigDecimal.ZERO;
    Assertions.assertTrue(DecimalHelper.isNullOrZero(zeroFromBigDecimalConstant));

    BigDecimal zeroFromInt = BigDecimal.valueOf(0);
    Assertions.assertTrue(DecimalHelper.isNullOrZero(zeroFromInt));

    BigDecimal zeroFromString = new BigDecimal("0.00");
    Assertions.assertTrue(DecimalHelper.isNullOrZero(zeroFromString));
  }

  @Test
  void getZeroOrValue_when_decimal_is_null() {

    BigDecimal decimal = null;
    Assertions.assertEquals(BigDecimal.ZERO, DecimalHelper.getZeroOrValue(decimal));
  }

  @Test
  void getZeroOrValue_when_decimal_is_not_null() {

    BigDecimal zeroFromBigDecimalConstant = BigDecimal.ZERO;
    Assertions.assertEquals(
        zeroFromBigDecimalConstant, DecimalHelper.getZeroOrValue(zeroFromBigDecimalConstant));

    BigDecimal zeroFromInt = BigDecimal.valueOf(0);
    Assertions.assertEquals(zeroFromInt, DecimalHelper.getZeroOrValue(zeroFromInt));

    BigDecimal zeroFromString = new BigDecimal("0.00");
    Assertions.assertEquals(zeroFromString, DecimalHelper.getZeroOrValue(zeroFromString));

    BigDecimal oneFromBigDecimalConstant = BigDecimal.ONE;
    Assertions.assertEquals(
        oneFromBigDecimalConstant, DecimalHelper.getZeroOrValue(oneFromBigDecimalConstant));

    BigDecimal oneFromInt = BigDecimal.valueOf(1);
    Assertions.assertEquals(oneFromInt, DecimalHelper.getZeroOrValue(oneFromInt));

    BigDecimal oneFromString = new BigDecimal("1.00");
    Assertions.assertEquals(oneFromString, DecimalHelper.getZeroOrValue(oneFromString));
  }
}
