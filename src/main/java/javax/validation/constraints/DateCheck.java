package javax.validation.constraints;

import com.github.kancyframework.validationplus.validator.DateCheckConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 日期格式校验
 * @author: kancy
 * @date: 2019/12/11 10:40
 **/
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { DateCheckConstraintValidator.class })
public @interface DateCheck {
    /**
     * 是否必填 默认是必填的
     * @return
     */
    boolean required() default true;

    String message() default "{DateCheck.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String format() default "yyyy-MM-dd";

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        DateCheck[] value();
    }
}