package javax.validation.constraints;

import com.github.kancyframework.validationplus.validator.NotNullOrBlankConstraintValidator;

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
 * 不为null和不为空白
 * <p>
 *
 * @author: kancy
 * @date: 2020/4/20 11:05
 **/
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Repeatable(NotNullOrBlank.List.class)
@Constraint(validatedBy = { NotNullOrBlankConstraintValidator.class })
public @interface NotNullOrBlank {

    String message() default "{NotNullOrBlank.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        NotNullOrBlank[] value();
    }
}

