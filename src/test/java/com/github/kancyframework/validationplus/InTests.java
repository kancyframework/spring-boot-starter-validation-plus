package com.github.kancyframework.validationplus;

import com.github.kancyframework.validationplus.utils.ValidationUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * InTests
 *
 * @author huangchengkang
 * @date 2022/6/21 21:47
 */
public class InTests {
    @Test
    public void test01(){
        String propertyName = "inChek";

        // 正确
        Assert.assertFalse(ValidationUtils.validateProperty(DataEntity.builder().inChek("A").build(), propertyName).hasErrors());

        // 错误
        ValidationUtils.ValidResult validResult = ValidationUtils.validateProperty(DataEntity.builder().inChek("D").build(), propertyName);
        System.out.println(validResult.getErrors());
        Assert.assertTrue(validResult.hasErrors());
    }
}
