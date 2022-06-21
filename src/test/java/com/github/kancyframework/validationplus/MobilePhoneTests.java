package com.github.kancyframework.validationplus;

import com.github.kancyframework.validationplus.utils.ValidationUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * MobilePhoneTests
 *
 * @author huangchengkang
 * @date 2022/6/21 21:47
 */
public class MobilePhoneTests {
    @Test
    public void test01(){
        String propertyName = "mobile";

        // 正确
        Assert.assertFalse(ValidationUtils.validateProperty(DataEntity.builder().mobile("18079637001").build(), propertyName).hasErrors());

        // 错误
        ValidationUtils.ValidResult validResult = ValidationUtils.validateProperty(DataEntity.builder().mobile("28079637001").build(), propertyName);
        System.out.println(validResult.getErrors());
        Assert.assertTrue(validResult.hasErrors());
    }
}
