package com.axelor.utils;

import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DecimalToolTest {

  @Test
  void isNullOrZero_when_decimal_is_null() {

    Assertions.assertTrue(DecimalTool.isNullOrZero(null));
  }

  @Test
  void isNullOrZero_when_decimal_is_zero() {

    BigDecimal zeroFromBigDecimalConstant = BigDecimal.ZERO;
    Assertions.assertTrue(DecimalTool.isNullOrZero(zeroFromBigDecimalConstant));

    BigDecimal zeroFromInt = BigDecimal.valueOf(0);
    Assertions.assertTrue(DecimalTool.isNullOrZero(zeroFromInt));

    BigDecimal zeroFromString = new BigDecimal("0.00");
    Assertions.assertTrue(DecimalTool.isNullOrZero(zeroFromString));
  }

  @Test
  void getZeroOrValue_when_decimal_is_null() {

    BigDecimal decimal = null;
    Assertions.assertEquals(BigDecimal.ZERO, DecimalTool.getZeroOrValue(decimal));
  }

  @Test
  void getZeroOrValue_when_decimal_is_not_null() {

    BigDecimal zeroFromBigDecimalConstant = BigDecimal.ZERO;
    Assertions.assertEquals(
        zeroFromBigDecimalConstant, DecimalTool.getZeroOrValue(zeroFromBigDecimalConstant));

    BigDecimal zeroFromInt = BigDecimal.valueOf(0);
    Assertions.assertEquals(zeroFromInt, DecimalTool.getZeroOrValue(zeroFromInt));

    BigDecimal zeroFromString = new BigDecimal("0.00");
    Assertions.assertEquals(zeroFromString, DecimalTool.getZeroOrValue(zeroFromString));

    BigDecimal oneFromBigDecimalConstant = BigDecimal.ONE;
    Assertions.assertEquals(
        oneFromBigDecimalConstant, DecimalTool.getZeroOrValue(oneFromBigDecimalConstant));

    BigDecimal oneFromInt = BigDecimal.valueOf(1);
    Assertions.assertEquals(oneFromInt, DecimalTool.getZeroOrValue(oneFromInt));

    BigDecimal oneFromString = new BigDecimal("1.00");
    Assertions.assertEquals(oneFromString, DecimalTool.getZeroOrValue(oneFromString));
  }
}
