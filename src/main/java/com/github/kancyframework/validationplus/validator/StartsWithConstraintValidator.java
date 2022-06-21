package com.github.kancyframework.validationplus.validator;

import javax.validation.constraints.HeadTail;
import javax.validation.constraints.StartsWith;
import java.util.regex.Pattern;

/**
 * <p>
 * ConstraintValidator
 * </p>
 *
 * @author: kancy
 * @date: 2019/12/11 10:40
 **/
public class StartsWithConstraintValidator extends CheckEmptyConstraintValidator<StartsWith, String> {

    /**
     * 验证的值不为空时，验证结果
     * @param value
     * @return
     */
    @Override
    protected boolean check(String value) {
        // 开头
        return value.startsWith(annotation.value());
    }

    /**
     * 验证的值为空时，返回结果
     *
     * @return
     */
    @Override
    protected boolean requestEmptyResult() {
        return !annotation.required();
    }
}
