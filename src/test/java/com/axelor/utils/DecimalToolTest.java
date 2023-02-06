package com.axelor.utils;

import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

class DecimalToolTest {

  @Test
  void isNullOrZero_when_decimal_is_null() {

    BigDecimal decimal = null;
    Assert.assertTrue(DecimalTool.isNullOrZero(decimal));
  }

  @Test
  void isNullOrZero_when_decimal_is_zero() {

    BigDecimal zeroFromBigDecimalConstant = BigDecimal.ZERO;
    Assert.assertTrue(DecimalTool.isNullOrZero(zeroFromBigDecimalConstant));

    BigDecimal zeroFromInt = BigDecimal.valueOf(0);
    Assert.assertTrue(DecimalTool.isNullOrZero(zeroFromInt));

    BigDecimal zeroFromString = new BigDecimal("0.00");
    Assert.assertTrue(DecimalTool.isNullOrZero(zeroFromString));
  }

  @Test
  void getZeroOrValue_when_decimal_is_null() {

    BigDecimal decimal = null;
    Assert.assertEquals(BigDecimal.ZERO, DecimalTool.getZeroOrValue(decimal));
  }

  @Test
  void getZeroOrValue_when_decimal_is_not_null() {

    BigDecimal zeroFromBigDecimalConstant = BigDecimal.ZERO;
    Assert.assertEquals(
        zeroFromBigDecimalConstant, DecimalTool.getZeroOrValue(zeroFromBigDecimalConstant));

    BigDecimal zeroFromInt = BigDecimal.valueOf(0);
    Assert.assertEquals(zeroFromInt, DecimalTool.getZeroOrValue(zeroFromInt));

    BigDecimal zeroFromString = new BigDecimal("0.00");
    Assert.assertEquals(zeroFromString, DecimalTool.getZeroOrValue(zeroFromString));

    BigDecimal oneFromBigDecimalConstant = BigDecimal.ONE;
    Assert.assertEquals(
        oneFromBigDecimalConstant, DecimalTool.getZeroOrValue(oneFromBigDecimalConstant));

    BigDecimal oneFromInt = BigDecimal.valueOf(1);
    Assert.assertEquals(oneFromInt, DecimalTool.getZeroOrValue(oneFromInt));

    BigDecimal oneFromString = new BigDecimal("1.00");
    Assert.assertEquals(oneFromString, DecimalTool.getZeroOrValue(oneFromString));
  }
}
