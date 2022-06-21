package javax.validation.constraints;

import com.github.kancyframework.validationplus.validator.NotInConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 不在什么范围
 * @author: kancy
 * @date: 2019/12/11 10:40
 **/
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { NotInConstraintValidator.class })
public @interface NotIn {
    /**
     * 是否必填 默认是必填的
     * @return
     */
    boolean required() default true;

    String message() default "{NotIn.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 选项
     * @return
     */
    String[] value();

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        NotIn[] value();
    }
}
