package com.github.kancyframework.validationplus;

import com.github.kancyframework.validationplus.utils.ValidationUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * NotNullOrEmptyTests
 *
 * @author huangchengkang
 * @date 2022/6/21 21:47
 */
public class NotNullOrEmptyTests {
    @Test
    public void test01(){
        String propertyName = "notNullOrEmpty";

        // 正确
        Assert.assertFalse(ValidationUtils.validateProperty(DataEntity.builder().notNullOrEmpty("123456").build(), propertyName).hasErrors());
        Assert.assertFalse(ValidationUtils.validateProperty(DataEntity.builder().notNullOrEmpty("  ").build(), propertyName).hasErrors());

        // 错误
        ValidationUtils.ValidResult validResult = ValidationUtils.validateProperty(DataEntity.builder().notNullOrEmpty("").build(), propertyName);
        System.out.println(validResult.getErrors());
        Assert.assertTrue(validResult.hasErrors());
        // 错误
        validResult = ValidationUtils.validateProperty(DataEntity.builder().build(), propertyName);
        System.out.println(validResult.getErrors());
        Assert.assertTrue(validResult.hasErrors());
    }
}
