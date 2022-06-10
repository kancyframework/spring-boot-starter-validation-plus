package javax.validation.constraints;

import com.github.kancyframework.validationplus.validator.NotNullOrEmptyConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>
 * 不为null且不为空
 * <p>
 *
 * @author: kancy
 * @date: 2020/4/20 11:05
 **/
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Repeatable(NotNullOrEmpty.List.class)
@Constraint(validatedBy = { NotNullOrEmptyConstraintValidator.class })
public @interface NotNullOrEmpty {

    String message() default "{NotNullOrEmpty.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        NotNullOrEmpty[] value();
    }
}

