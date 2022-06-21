package com.github.kancyframework.validationplus;

import com.github.kancyframework.validationplus.utils.ValidationUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Md5Tests
 *
 * @author huangchengkang
 * @date 2022/6/21 21:47
 */
public class Md5Tests {
    @Test
    public void test01(){
        String propertyName = "md5Check";

        // 正确
        Assert.assertFalse(ValidationUtils.validateProperty(DataEntity.builder().md5Check("4124bc0a9335c27f086f24ba207a4912").build(), propertyName).hasErrors());
        Assert.assertFalse(ValidationUtils.validateProperty(DataEntity.builder().md5Check("4124BC0A9335C27F086F24BA207A4912").build(), propertyName).hasErrors());

        // 错误
        ValidationUtils.ValidResult validResult = ValidationUtils.validateProperty(DataEntity.builder().md5Check("123").build(), propertyName);
        System.out.println(validResult.getErrors());
        Assert.assertTrue(validResult.hasErrors());
        // 错误
        validResult = ValidationUtils.validateProperty(DataEntity.builder().md5Check("").build(), propertyName);
        System.out.println(validResult.getErrors());
        Assert.assertTrue(validResult.hasErrors());
    }
}
