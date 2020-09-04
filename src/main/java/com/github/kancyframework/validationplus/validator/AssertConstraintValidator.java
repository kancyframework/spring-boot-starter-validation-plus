package com.github.kancyframework.validationplus.validator;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Assert;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * AssertConstraintValidator
 *
 * @author kancy
 * @date 2020/8/8 11:01
 */
public class AssertConstraintValidator extends AbstractBeanFactoryAwareAdvisingPostProcessor
        implements MethodInterceptor, InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(AssertConstraintValidator.class);
    /**
     * 创建ExpressionParser解析表达式
     */
    private final ExpressionParser parser = new SpelExpressionParser();

    /**
     * 参数名处理器
     */
    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object[] arguments = invocation.getArguments();
        if (Objects.isNull(arguments) || arguments.length == 0){
            return invocation.proceed();
        }
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(invocation.getMethod());

        Map<String, Object> rootMap = new HashMap<>(parameterNames.length);
        for (int i = 0; i < parameterNames.length; i++) {
            rootMap.put(parameterNames[i], arguments[i]);
        }

        for (int i = 0; i < arguments.length; i++) {
            Object argument = arguments[i];
            Class<?> argumentClass = argument.getClass();

            // 排除
            if (List.class.isAssignableFrom(argumentClass) || argumentClass.isArray()){
                continue;
            }

            // 基本类型
            if (ClassUtils.isPrimitiveOrWrapper(argumentClass)
                    || Objects.equals(argumentClass, String.class)){
                // 创建一个虚拟的容器EvaluationContext
                StandardEvaluationContext ctx = new StandardEvaluationContext();
                ctx.setRootObject(rootMap);
                doCheckParameter(ctx, invocation.getMethod().getParameters()[i], parameterNames[i]);
                continue;
            }

            // 实体
            List<Field> assertFields = new ArrayList<>();
            ReflectionUtils.doWithFields(argument.getClass(), field -> assertFields.add(field),
                    field -> field.isAnnotationPresent(Assert.class) || field.isAnnotationPresent(Assert.List.class));

            if (!CollectionUtils.isEmpty(assertFields)){
                // 创建一个虚拟的容器EvaluationContext
                StandardEvaluationContext ctx = new StandardEvaluationContext();
                ReflectionUtils.doWithLocalMethods(argument.getClass(), method -> {
                    if (Objects.equals(method.getReturnType(), Boolean.class)
                            || Objects.equals(method.getReturnType(), boolean.class)){
                        ctx.registerFunction(method.getName(), method);
                    }
                });
                // setRootObject并非必须；
                // 一个EvaluationContext只能有一个RootObject，引用它的属性时，可以不加前缀
                ctx.setRootObject(argument);
                for (Field field : assertFields) {
                    doCheckField(ctx, field);
                }
            }

            // 钩子方法
            if (argument instanceof ValidationAware){
                ValidationAware validationAware = (ValidationAware) argument;
                List<String> errors = new ArrayList<>();
                if (!validationAware.validate(argument, errors) && !CollectionUtils.isEmpty(errors)){
                    throw new IllegalArgumentException(errors.get(0));
                }
            }
        }
        return invocation.proceed();
    }

    private void doCheckParameter(StandardEvaluationContext ctx, Parameter parameter,String parameterName) {
        doCheckBySpel(ctx, parameterName, parameter.getAnnotationsByType(Assert.class));
    }

    private void doCheckField(StandardEvaluationContext ctx, Field field) {
        doCheckBySpel(ctx, field.getName(), field.getAnnotationsByType(Assert.class));
    }

    private void doCheckBySpel(StandardEvaluationContext ctx, String parameterName, Assert[] assertList) {
        for (Assert annotation : assertList) {
            //表达式放置
            Expression exp = parser.parseExpression(annotation.value());
            //getValue有参数ctx，从新的容器中根据SpEL表达式获取所需的值
            boolean result = Objects.equals(exp.getValue(ctx, Boolean.class), annotation.result());
            if (!result) {
                String message = annotation.message();
                if (StringUtils.isEmpty(message)) {
                    message = String.format("参数[%s]不合法", parameterName);
                }
                throw new IllegalArgumentException(message);
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
            return ClassUtils.forName("org.springframework.stereotype.Controller", getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    protected Advice createMethodValidationAdvice() {
        return this;
    }

}
