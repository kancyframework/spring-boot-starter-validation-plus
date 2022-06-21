package com.kancy.validationplus.demo.controller;

import com.kancy.validationplus.demo.entity.Cust;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Assert;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNullOrBlank;

/**
 * 用户表服务控制器
 *
 * @author kancy
 * @since 2020-09-04 14:32:50
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
public class CustController implements ExceptionHandlerController{

    @PostMapping("/test")
    public R<Object> test(@Validated @RequestBody Cust cust,
                          @RequestParam @Assert("name != null") String name){
        return R.success();
    }

    @PostMapping("/test2")
    public R<Object> test(@RequestParam @Assert("name != null") String name){
        return R.success();
    }

    @PostMapping("/test3")
    public R<Object> test3(@RequestParam @NotBlank String name){
        return R.success();
    }
    @PostMapping("/test4")
    public R<Object> test4(@RequestParam @Assert("#notin(name, 'A,B,9') and #start(name, 'a') and #end(name, 'b')") String name){
        return R.success();
    }
    @PostMapping("/test5")
    public R<Object> test5(@RequestParam("name") @NotNullOrBlank(message = "名称不能为空") String name){
        return R.success();
    }

}