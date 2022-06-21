package com.github.kancyframework.validationplus;

import com.github.kancyframework.validationplus.utils.ValidationUtils;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * AmountTests
 *
 * @author huangchengkang
 * @date 2022/6/21 21:47
 */
public class AmountTests {
    @Test
    public void test01(){
        String propertyName = "amount";

        // 正确
        Assert.assertFalse(ValidationUtils.validateProperty(DataEntity.builder().amount(new BigDecimal("200.5")).build(), propertyName).hasErrors());

        // 错误
        ValidationUtils.ValidResult validResult = ValidationUtils.validateProperty(DataEntity.builder().amount(new BigDecimal("200.125")).build(), propertyName);
        System.out.println(validResult.getErrors());
        Assert.assertTrue(validResult.hasErrors());
        // 错误
        validResult = ValidationUtils.validateProperty(DataEntity.builder().amount(null).build(), propertyName);
        System.out.println(validResult.getErrors());
        Assert.assertTrue(validResult.hasErrors());
    }
}
