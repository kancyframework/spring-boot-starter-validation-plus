package com.github.kancyframework.validationplus;

import com.github.kancyframework.validationplus.utils.ValidationUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * EndsWithTests
 *
 * @author huangchengkang
 * @date 2022/6/21 21:47
 */
public class EndsWithTests {
    @Test
    public void test01(){
        String propertyName = "endsWith";
        Assert.assertFalse(ValidationUtils.validateProperty(DataEntity.builder().endsWith("sdfgc").build(), propertyName).hasErrors());

        ValidationUtils.ValidResult validResult = ValidationUtils.validateProperty(DataEntity.builder().endsWith("cba").build(), propertyName);
        System.out.println(validResult.getErrors());
        Assert.assertTrue(validResult.hasErrors());
    }
}
