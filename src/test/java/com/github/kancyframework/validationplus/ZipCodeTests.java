package com.github.kancyframework.validationplus;

import com.github.kancyframework.validationplus.utils.ValidationUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * ZipCodeTests
 *
 * @author huangchengkang
 * @date 2022/6/21 21:47
 */
public class ZipCodeTests {
    @Test
    public void test01(){
        String propertyName = "zipCode";

        // 正确
        Assert.assertFalse(ValidationUtils.validateProperty(DataEntity.builder().zipCode("123456").build(), propertyName).hasErrors());

        // 错误
        ValidationUtils.ValidResult validResult = ValidationUtils.validateProperty(DataEntity.builder().zipCode("123456789").build(), propertyName);
        System.out.println(validResult.getErrors());
        Assert.assertTrue(validResult.hasErrors());
    }
}
