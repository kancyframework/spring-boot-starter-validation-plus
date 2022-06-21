package com.github.kancyframework.validationplus;

import com.github.kancyframework.validationplus.utils.ValidationUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * EmailTests
 *
 * @author huangchengkang
 * @date 2022/6/21 21:47
 */
public class EmailTests {
    @Test
    public void test01(){
        String propertyName = "email";

        // 正确
        Assert.assertFalse(ValidationUtils.validateProperty(DataEntity.builder().email("yyy@qq.com").build(), propertyName).hasErrors());

        // 错误
        ValidationUtils.ValidResult validResult = ValidationUtils.validateProperty(DataEntity.builder().email("yyyy.qq.com").build(), propertyName);
        System.out.println(validResult.getErrors());
        Assert.assertTrue(validResult.hasErrors());
    }
}
