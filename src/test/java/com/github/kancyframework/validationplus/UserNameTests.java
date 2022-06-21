package com.github.kancyframework.validationplus;

import com.github.kancyframework.validationplus.utils.ValidationUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * UserNameTests
 *
 * @author huangchengkang
 * @date 2022/6/21 21:47
 */
public class UserNameTests {
    @Test
    public void test01(){
        String propertyName = "userName";

        // 正确
        Assert.assertFalse(ValidationUtils.validateProperty(DataEntity.builder().userName("李四").build(), propertyName).hasErrors());

        // 错误
        ValidationUtils.ValidResult validResult = ValidationUtils.validateProperty(DataEntity.builder().userName("李").build(), propertyName);
        System.out.println(validResult.getErrors());
        Assert.assertTrue(validResult.hasErrors());
    }
}
