package com.github.kancyframework.validationplus;

import com.github.kancyframework.validationplus.utils.ValidationUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * NumericTests
 *
 * @author huangchengkang
 * @date 2022/6/21 21:47
 */
public class NumericTests {
    @Test
    public void test01(){
        String propertyName = "numeric";

        // 正确
        Assert.assertFalse(ValidationUtils.validateProperty(DataEntity.builder().numeric("123").build(), propertyName).hasErrors());

        // 错误
        ValidationUtils.ValidResult validResult = ValidationUtils.validateProperty(DataEntity.builder().numeric("12.9999").build(), propertyName);
        System.out.println(validResult.getErrors());
        Assert.assertTrue(validResult.hasErrors());
        // 错误
        validResult = ValidationUtils.validateProperty(DataEntity.builder().numeric("abc").build(), propertyName);
        System.out.println(validResult.getErrors());
        Assert.assertTrue(validResult.hasErrors());
    }
}
