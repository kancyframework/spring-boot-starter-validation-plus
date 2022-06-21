package com.github.kancyframework.validationplus;

import com.github.kancyframework.validationplus.utils.ValidationUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * DateTimeCheckTests
 *
 * @author huangchengkang
 * @date 2022/6/21 21:47
 */
public class DateTimeCheckTests {
    @Test
    public void test01(){
        String propertyName = "datetimeCheck";

        // 正确
        Assert.assertFalse(ValidationUtils.validateProperty(DataEntity.builder().datetimeCheck("2022-01-01 12:00:01").build(), propertyName).hasErrors());

        // 错误
        ValidationUtils.ValidResult validResult = ValidationUtils.validateProperty(DataEntity.builder().datetimeCheck("2022/01/01 12:00:01").build(), propertyName);
        System.out.println(validResult.getErrors());
        Assert.assertTrue(validResult.hasErrors());
        // 错误
        validResult = ValidationUtils.validateProperty(DataEntity.builder().datetimeCheck("").build(), propertyName);
        System.out.println(validResult.getErrors());
        Assert.assertTrue(validResult.hasErrors());
    }
}
