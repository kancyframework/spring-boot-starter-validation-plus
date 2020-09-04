package com.kancy.validationplus.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.Assert;
import java.io.Serializable;

/**
 * 用户表(t_cust)实体类
 *
 * @author kancy
 * @since 2020-09-04 14:32:50
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_cust")
public class Cust extends Model<Cust> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 全局用户ID
     */
    @Assert(value = "vcreditCustId < 10")
    @Assert(value = "vcreditCustId > 5")
    @TableId
	private Long vcreditCustId;
    /**
     * 是否删除
     */
    private Integer isDeleted;

    @Valid
    private User user;

    @Data
    public static class User {

        @Assert("user.age > 18")
        private Integer age;

        private String sex;
    }
}