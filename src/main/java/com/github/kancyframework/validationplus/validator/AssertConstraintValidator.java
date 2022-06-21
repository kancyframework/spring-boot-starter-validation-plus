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
import org.springframework.context.expression.MapAccessor;
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
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.Assert;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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

    /**
     * 缓存
     */
    private static final Map<Class, List<AssertMetaData>> ASSERT_META_DATA_MAP = new HashMap<>();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object[] arguments = invocation.getArguments();
        if (Objects.isNull(arguments) || arguments.length == 0){
            return invocation.proceed();
        }

        Method invocationMethod = invocation.getMethod();
        Parameter[] parameters = invocationMethod.getParameters();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(invocationMethod);

        Map<String, Object> rootMap = new HashMap<>(parameters.length);
        for (int i = 0; i < parameters.length; i++) {
            if (Objects.nonNull(parameterNames)){
                rootMap.put(parameterNames[i], arguments[i]);
            }
            RequestParam annotation = parameters[i].getAnnotation(RequestParam.class);
            if (Objects.nonNull(annotation)){
                String parameterName = annotation.name();
                if (StringUtils.hasText(parameterName)){
                    rootMap.put(parameterName, arguments[i]);
                }
            }
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
                StandardEvaluationContext ctx = initStandardEvaluationContext();
                ctx.setRootObject(rootMap);
                // 这里很关键，如果没有配置MapAccessor，那么只能用['c']['a']这种解析方式
                ctx.addPropertyAccessor(new MapAccessor());
                doCheckParameter(ctx, invocation.getMethod().getParameters()[i], parameterNames[i]);
                continue;
            }

            // 实体
            List<AssertMetaData> metaDataList = getAssertMetaDatas(argumentClass);
            if (!CollectionUtils.isEmpty(metaDataList)) {
                // 创建一个虚拟的容器EvaluationContext
                StandardEvaluationContext ctx = initStandardEvaluationContext();
                // setRootObject并非必须；
                // 一个EvaluationContext只能有一个RootObject，引用它的属性时，可以不加前缀
                ctx.setRootObject(argument);

                for (AssertMetaData metaData : metaDataList) {
                    for (Assert annotation : metaData.getAsserts()) {
                        //表达式放置
                        Expression exp = parser.parseExpression(annotation.value());
                        //getValue有参数ctx，从新的容器中根据SpEL表达式获取所需的值
                        boolean result = Objects.equals(exp.getValue(ctx, Boolean.class), annotation.result());
                        if (!result) {
                            String message = annotation.message();
                            if (StringUtils.isEmpty(message)) {
                                message = String.format("参数[%s]不合法", metaData.getParamName());
                            }
                            throw new IllegalArgumentException(message);
                        }
                    }
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

    private StandardEvaluationContext initStandardEvaluationContext() {
        StandardEvaluationContext ctx = new StandardEvaluationContext();
        ReflectionUtils.doWithLocalMethods(SpelTools.class, method -> {
            ctx.registerFunction(method.getName(), method);
            ctx.registerFunction(method.getName().toLowerCase(), method);
            ctx.registerFunction(method.getName().toUpperCase(), method);
        });
        return ctx;
    }

    private List<AssertMetaData> getAssertMetaDatas(Class<?> argumentClass) {
        List<AssertMetaData> assertMetaDataList = ASSERT_META_DATA_MAP.get(argumentClass);
        if (Objects.isNull(assertMetaDataList)){
            synchronized (ASSERT_META_DATA_MAP){
                assertMetaDataList = ASSERT_META_DATA_MAP.get(argumentClass);
                if (Objects.isNull(assertMetaDataList)){
                    assertMetaDataList = new ArrayList<>();
                    findAssertMetaDataList(argumentClass, assertMetaDataList, false);
                }
                if (CollectionUtils.isEmpty(assertMetaDataList)){
                    assertMetaDataList = Collections.emptyList();
                }
                ASSERT_META_DATA_MAP.put(argumentClass, assertMetaDataList);
            }
        }
        return assertMetaDataList;
    }

    private void findAssertMetaDataList(Class<?> argumentClass, List<AssertMetaData> metaDataList, boolean isRecursion) {
        Field[] fields = argumentClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Assert.class) || field.isAnnotationPresent(Assert.List.class)){
                AssertMetaData metaData = new AssertMetaData();
                metaData.setAsserts(field.getAnnotationsByType(Assert.class));
                metaData.setParamName(field.getName());
                metaData.setValidObject(isRecursion);
                metaDataList.add(metaData);
            }

            if (field.isAnnotationPresent(Valid.class) || field.getType().isAnnotationPresent(Valid.class)){
                findAssertMetaDataList(field.getType(), metaDataList, true);
            }
        }
    }


    private void doCheckParameter(StandardEvaluationContext ctx, Parameter parameter,String parameterName) {
        for (Assert annotation : parameter.getAnnotationsByType(Assert.class)) {
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


    public static class AssertMetaData {

        private boolean validObject;

        private String paramName;

        private Assert[] asserts;

        public String getParamName() {
            return paramName;
        }

        public void setParamName(String paramName) {
            this.paramName = paramName;
        }

        public Assert[] getAsserts() {
            return asserts;
        }

        public void setAsserts(Assert[] asserts) {
            this.asserts = asserts;
        }

        public boolean isValidObject() {
            return validObject;
        }

        public void setValidObject(boolean validObject) {
            this.validObject = validObject;
        }
    }

    public static class SpelTools{

        public static boolean start(String object, Object sub){
            return object.startsWith(String.valueOf(sub));
        }
        public static boolean end(String object, Object sub){
            return object.endsWith(String.valueOf(sub));
        }
        public static boolean startsWith(String object, Object sub){
            return object.startsWith(String.valueOf(sub));
        }
        public static boolean endsWith(String object, Object sub){
            return object.endsWith(String.valueOf(sub));
        }
        public static boolean startWith(String object, Object sub){
            return object.startsWith(String.valueOf(sub));
        }
        public static boolean endWith(String object, Object sub){
            return object.endsWith(String.valueOf(sub));
        }

        public static boolean contains(String object, Object sub){
            return object.contains(String.valueOf(sub));
        }
        public static boolean notContains(String object, Object sub){
            return !contains(object, sub);
        }

        public static boolean in(Object object, Object list){
            String value = String.valueOf(object);
            if (list instanceof Collection){
                Collection collection = (Collection) list;
                return collection.stream().anyMatch(item->Objects.equals(value, String.valueOf(item)));
            }
            if (list instanceof String){
                String collection = (String) list;
                if (collection.contains(",")){
                    return Arrays.asList(collection.split(",")).contains(value);
                }
                if (collection.contains("|")){
                    return Arrays.asList(collection.split("[|]")).contains(value);
                }
                return Objects.equals(value, collection);
            }
            return true;
        }

        public static boolean notIn(Object object, Object list){
            return !in(object, list);
        }

        public static boolean notNull(Object object){
            return Objects.nonNull(object);
        }
        public static boolean notEmpty(Object object){
            return StringUtils.hasLength(String.valueOf(object));
        }
        public static boolean notBlank(Object object){
            return StringUtils.hasText(String.valueOf(object));
        }
        public static boolean range(Number object, Number start, Number end){
            return object.doubleValue() >= start.doubleValue()
                    && object.doubleValue() <= end.doubleValue();
        }
        public static boolean inRange(Number object, Number start, Number end){
            return range(object, start, end);
        }
        public static boolean notRange(Number object, Number start, Number end){
            return !range(object, start, end);
        }
        public static boolean notInRange(Number object, Number start, Number end){
            return !range(object, start, end);
        }
    }
}
