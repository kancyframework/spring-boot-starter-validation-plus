package com.github.kancyframework.validationplus;

import com.github.kancyframework.validationplus.utils.ValidationUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * DataIdTests
 *
 * @author huangchengkang
 * @date 2022/6/21 21:47
 */
public class DataIdTests {
    @Test
    public void test01(){
        String propertyName = "dataId";

        // 正确
        Assert.assertFalse(ValidationUtils.validateProperty(DataEntity.builder().dataId(123L).build(), propertyName).hasErrors());

        // 错误
        ValidationUtils.ValidResult validResult = ValidationUtils.validateProperty(DataEntity.builder().dataId(-9L).build(), propertyName);
        System.out.println(validResult.getErrors());
        Assert.assertTrue(validResult.hasErrors());
        // 错误
        validResult = ValidationUtils.validateProperty(DataEntity.builder().dataId(null).build(), propertyName);
        System.out.println(validResult.getErrors());
        Assert.assertTrue(validResult.hasErrors());
    }
}
