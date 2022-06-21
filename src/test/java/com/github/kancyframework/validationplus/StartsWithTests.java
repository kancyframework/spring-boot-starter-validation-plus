package com.github.kancyframework.validationplus;

import com.github.kancyframework.validationplus.utils.ValidationUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * StartsWithTests
 *
 * @author huangchengkang
 * @date 2022/6/21 21:47
 */
public class StartsWithTests {
    @Test
    public void test01(){
        String propertyName = "startsWith";
        Assert.assertFalse(ValidationUtils.validateProperty(DataEntity.builder().startsWith("aqwe").build(), propertyName).hasErrors());

        ValidationUtils.ValidResult validResult = ValidationUtils.validateProperty(DataEntity.builder().startsWith("cba").build(), propertyName);
        System.out.println(validResult.getErrors());
        Assert.assertTrue(validResult.hasErrors());
    }
}
