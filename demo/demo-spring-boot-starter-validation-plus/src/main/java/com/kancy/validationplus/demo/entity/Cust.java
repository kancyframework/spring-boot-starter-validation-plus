package com.kancy.validationplus.demo.entity;

import com.kancy.validationplus.demo.enums.ItemEnum;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * 用户表(t_cust)实体类
 *
 * @author kancy
 * @since 2020-09-04 14:32:50
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
public class Cust implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 全局用户ID
     */
    @Assert(value = "vcreditCustId < 10")
    @Assert(value = "vcreditCustId > 5")
	private Long vcreditCustId;

    /**
     * 是否删除
     */
    private Integer isDeleted;

    @NotNullOrEmpty
    private String notEmpty;

    @NotNullOrBlank
    private String notBlank;

    @NotEmpty
    private String notEmpty1;

    @NotBlank
    private String notBlank2;


    @EnumCheck(enumClass = ItemEnum.class, useEnumName = true)
    private String item;

    @Valid
    private User user;

    @Data
    public static class User {

        @Assert("user.age > 18")
        private Integer age;

        private String sex;
    }
}