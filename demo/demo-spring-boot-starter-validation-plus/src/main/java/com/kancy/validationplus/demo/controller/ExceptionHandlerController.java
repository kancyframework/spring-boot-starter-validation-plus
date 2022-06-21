package com.kancy.validationplus.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

/**
 * 全局异常处理
 * @author tangdandan
 * @see 2019-05-27
 */
public interface ExceptionHandlerController {

     Logger log = LoggerFactory.getLogger(ExceptionHandlerController.class);

    /**
     * 全局异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(Throwable.class)
    default ResponseEntity handleException(Throwable e) {
        log.error("GlobalExceptionAspect print Throwable :",e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    /**
     * IllegalArgumentException异常处理
     * @param ex
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    default ResponseEntity handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("参数验证失败:{}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(ex.getMessage());
    }

    /**
     * HttpMessageConversionException
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageConversionException.class)
    default ResponseEntity handleHttpMessageConversionException(HttpMessageConversionException e) {
        log.error("GlobalExceptionAspect print HttpMessageConversionException : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
    }

    /**
     * MethodArgumentTypeMismatchException
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    default ResponseEntity handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("GlobalExceptionAspect print MethodArgumentTypeMismatchException : {}",e.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
    }

    /**
     * @RequestParam
     * MissingServletRequestParameterException
     *
     * @param e 异常对象
     * @return Result
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    default ResponseEntity handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("GlobalExceptionAspect print MissingServletRequestParameterException : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(String.format("参数[%s]输入不合法", e.getParameterName()));
    }

    /**
     * 参数验证异常处理
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    default ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.debug("GlobalExceptionAspect print MethodArgumentNotValidException :{}" + ex.getMessage());

        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();
        // 只返回一个错误
        if (bindingResult.hasErrors()){
            ObjectError firstError = bindingResult.getAllErrors().get(0);
            stringBuilder.append(StringUtils.isEmpty(firstError.getDefaultMessage()) ? "param is invalid" : firstError.getDefaultMessage());
        }

        String errorMessage = stringBuilder.toString();

        return ResponseEntity.status(HttpStatus.OK).body(errorMessage);
    }

    /**
     * ValidationException
     */
    @ExceptionHandler(ValidationException.class)
    default ResponseEntity handleValidationException(ValidationException e) {
        log.error("GlobalExceptionAspect print ValidationException : {}", e);
        return ResponseEntity.status(HttpStatus.OK).body(String.format("字段验证异常：%s" , e.getMessage()));
    }

    /**
     * ConstraintViolationException
     */
    @ExceptionHandler(ConstraintViolationException.class)
    default ResponseEntity handleConstraintViolationException(ConstraintViolationException e) {
        log.error("GlobalExceptionAspect print ConstraintViolationException : {}", e);
        return ResponseEntity.status(HttpStatus.OK).body(e.getConstraintViolations().stream().iterator().next().getMessage());
    }

}
