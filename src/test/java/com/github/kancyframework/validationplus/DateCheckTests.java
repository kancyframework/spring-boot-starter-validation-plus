package com.github.kancyframework.validationplus;

import com.github.kancyframework.validationplus.utils.ValidationUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * DateCheckTests
 *
 * @author huangchengkang
 * @date 2022/6/21 21:47
 */
public class DateCheckTests {
    @Test
    public void test01(){
        String propertyName = "dateCheck";

        // 正确
        Assert.assertFalse(ValidationUtils.validateProperty(DataEntity.builder().dateCheck("2022-01-01").build(), propertyName).hasErrors());

        // 错误
        ValidationUtils.ValidResult validResult = ValidationUtils.validateProperty(DataEntity.builder().dateCheck("2022/01/01").build(), propertyName);
        System.out.println(validResult.getErrors());
        Assert.assertTrue(validResult.hasErrors());
        // 错误
        validResult = ValidationUtils.validateProperty(DataEntity.builder().dateCheck("").build(), propertyName);
        System.out.println(validResult.getErrors());
        Assert.assertTrue(validResult.hasErrors());
    }
}
