package com.github.kancyframework.validationplus;

import com.github.kancyframework.validationplus.utils.ValidationUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * TimeCheckTests
 *
 * @author huangchengkang
 * @date 2022/6/21 21:47
 */
public class TimeCheckTests {
    @Test
    public void test01(){
        String propertyName = "timeCheck";

        // 正确
        Assert.assertFalse(ValidationUtils.validateProperty(DataEntity.builder().timeCheck("12:00:01").build(), propertyName).hasErrors());

        // 错误
        ValidationUtils.ValidResult validResult = ValidationUtils.validateProperty(DataEntity.builder().timeCheck("12:0:1").build(), propertyName);
        System.out.println(validResult.getErrors());
        Assert.assertTrue(validResult.hasErrors());
        // 错误
        validResult = ValidationUtils.validateProperty(DataEntity.builder().build(), propertyName);
        System.out.println(validResult.getErrors());
        Assert.assertTrue(validResult.hasErrors());
    }
}
