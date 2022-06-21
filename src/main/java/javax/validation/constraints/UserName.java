package javax.validation.constraints;

import com.github.kancyframework.validationplus.validator.UserNameConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author huangchengkang
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { UserNameConstraintValidator.class })
public @interface UserName {
    /**
     * 是否必填 默认是必填的
     * @return
     */
    boolean required() default true;

    String message() default "{UserName.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return the regular expression to match
     *
     * 中文：大卫·波菲尔、王林 、上官飞燕等纯中文以及圆点组合（名字的长度可以自用限制）
     * ^[\u4e00-\u9fa5+\·?\u4e00-\u9fa5+]{2,30}$
     *
     * 英文：Mr.li 等一系列
     * ^[a-zA-Z+\.?\·?a-zA-Z+]{2,30}$
     *
     * 中英文：
     * ^(([a-zA-Z+\.?\·?a-zA-Z+]{2,30}$)|([\u4e00-\u9fa5+\·?\u4e00-\u9fa5+]{2,30}$))
     *
     */
    String regexp() default "";

    /**
     * 最小长度
     * @return
     */
    int min() default 2;

    /**
     * 最大长度
     * @return
     */
    int max() default 20;

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        UserName[] value();
    }
}