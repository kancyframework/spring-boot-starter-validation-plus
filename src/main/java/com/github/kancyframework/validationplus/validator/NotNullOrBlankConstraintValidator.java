package com.github.kancyframework.validationplus.validator;

import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNullOrBlank;

/**
 * <p>
 * BankCardConstraintValidator
 * <p>
 *
 * @author: kancy
 * @date: 2020/4/20 11:08
 **/

public class NotNullOrBlankConstraintValidator extends CheckEmptyConstraintValidator<NotNullOrBlank, Object>{
    /**
     * 验证的值不为空时，验证结果
     *
     * @param value
     * @return
     */
    @Override
    protected boolean check(Object value) {
        return StringUtils.hasText(String.valueOf(value));
    }

    /**
     * 验证的值为空时，返回结果
     *
     * @return
     */
    @Override
    protected boolean requestEmptyResult() {
        return false;
    }
}
