package com.github.kancyframework.validationplus;

import com.github.kancyframework.validationplus.utils.ValidationUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * HeadTailTests
 *
 * @author huangchengkang
 * @date 2022/6/21 21:47
 */
public class HeadTailTests {
    @Test
    public void test01(){
        String propertyName = "headTail";

        // 正确
        Assert.assertFalse(ValidationUtils.validateProperty(DataEntity.builder().headTail("abc").build(), propertyName).hasErrors());
        Assert.assertFalse(ValidationUtils.validateProperty(DataEntity.builder().headTail("ac").build(), propertyName).hasErrors());

        // 错误
        ValidationUtils.ValidResult validResult = ValidationUtils.validateProperty(DataEntity.builder().headTail("cba").build(), propertyName);
        System.out.println(validResult.getErrors());
        Assert.assertTrue(validResult.hasErrors());
        // 错误
        validResult = ValidationUtils.validateProperty(DataEntity.builder().headTail("").build(), propertyName);
        System.out.println(validResult.getErrors());
        Assert.assertTrue(validResult.hasErrors());
    }
}
