package com.github.kancyframework.validationplus.validator;

import javax.validation.constraints.NotIn;
import java.util.Arrays;
import java.util.Objects;

/**
 * <p>
 * ConstraintValidator
 * </p>
 *
 * @author: kancy
 * @date: 2019/12/11 10:40
 **/
public class NotInConstraintValidator extends CheckEmptyConstraintValidator<NotIn, String> {

    /**
     * 验证的值不为空时，验证结果
     * @param value
     * @return
     */
    @Override
    protected boolean check(String value) {
        return !in(value);
    }

    private boolean in(String value) {
        String[] items = annotation.value();
        if (items.length == 1){
            String listStr = items[0];
            if (listStr.contains(",")){
                return Arrays.asList(listStr.split(",")).contains(value);
            }
            if (listStr.contains("|")){
                return Arrays.asList(listStr.split("[|]")).contains(value);
            }
            return Objects.equals(value, items);
        }
        if (items.length > 0){
            return Arrays.asList(items).contains(value);
        }
        return false;
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
