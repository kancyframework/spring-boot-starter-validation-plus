package com.github.kancyframework.validationplus;

import com.github.kancyframework.validationplus.utils.ValidationUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * NotInTests
 *
 * @author huangchengkang
 * @date 2022/6/21 21:47
 */
public class NotInTests {
    @Test
    public void test01(){
        String propertyName = "notInChek";

        // 正确
        Assert.assertFalse(ValidationUtils.validateProperty(DataEntity.builder().notInChek("D").build(), propertyName).hasErrors());

        // 错误
        ValidationUtils.ValidResult validResult = ValidationUtils.validateProperty(DataEntity.builder().notInChek("B").build(), propertyName);
        System.out.println(validResult.getErrors());
        Assert.assertTrue(validResult.hasErrors());
    }
}
