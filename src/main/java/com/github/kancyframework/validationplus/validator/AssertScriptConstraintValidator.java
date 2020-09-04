package com.github.kancyframework.validationplus.validator;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Assert;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * AssertAop
 *
 * @author kancy
 * @date 2020/8/8 11:01
 */
public class AssertScriptConstraintValidator extends AbstractBeanFactoryAwareAdvisingPostProcessor
        implements MethodInterceptor, InitializingBean {

    /**
     * 创建ExpressionParser解析表达式
     */
    private final ExpressionParser parser = new SpelExpressionParser();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object[] arguments = invocation.getArguments();
        if (Objects.nonNull(arguments) || arguments.length == 1){

            Object argument = arguments[0];
            ReflectionUtils.doWithFields(argument.getClass(), field -> {
                //创建一个虚拟的容器EvaluationContext
                StandardEvaluationContext ctx = new StandardEvaluationContext();
                ReflectionUtils.doWithLocalMethods(argument.getClass(), method -> {
                    if (Objects.equals(method.getReturnType(), Boolean.class)
                            || Objects.equals(method.getReturnType(), boolean.class)){
                        ctx.registerFunction(method.getName(), method);
                    }
                });
                //setRootObject并非必须；一个EvaluationContext只能有一个RootObject，引用它的属性时，可以不加前缀
                ctx.setRootObject(argument);
                doCheck(ctx, field);

            }, field -> field.isAnnotationPresent(Assert.class) ||
                    field.isAnnotationPresent(Assert.List.class));
        }
        Object proceed = invocation.proceed();
        return proceed;
    }

    private void doCheck(StandardEvaluationContext ctx, Field field) {
        Assert[] assertList = field.getAnnotationsByType(Assert.class);
        for (Assert anAssert : assertList) {
            Assert annotation = anAssert;
            //表达式放置
            Expression exp = parser.parseExpression(annotation.value());
            //getValue有参数ctx，从新的容器中根据SpEL表达式获取所需的值
            boolean result = Objects.equals(exp.getValue(ctx, Boolean.class), annotation.result());
            if (!result){
                String message = annotation.message();
                if (StringUtils.isEmpty(message)){
                    message = String.format("参数[%s]不合法", field.getName());
                }
                throw new RuntimeException(message);
            }
        }

    }

    @Override
    public void afterPropertiesSet() {
        Class annotationClass = getAnnotationClass();
        if (Objects.nonNull(annotationClass)){
            Pointcut pointcut = new AnnotationMatchingPointcut(annotationClass, true);
            this.advisor = new DefaultPointcutAdvisor(pointcut, createMethodValidationAdvice());
        }
    }

    private Class<?> getAnnotationClass() {
        try {
            return ClassUtils.forName("org.springframework.web.bind.annotation.Controller", getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Advice createMethodValidationAdvice() {
        return this;
    }

}
