package com.kancy.validationplus.demo.controller;

import com.kancy.validationplus.demo.entity.Cust;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Assert;
import java.util.List;

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
public class CustController {

    @PostMapping("/test")
    public R test(@RequestBody Cust cust, String name){
        return R.success();
    }

    @GetMapping("/test2")
    public R test2(Cust cust, @Assert("name != null") String name){
        return R.success();
    }
}