package com.github.kancyframework.validationplus;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DataEntity
 *
 * @author huangchengkang
 * @date 2022/6/21 21:48
 */
@Builder
@Data
public class DataEntity {
    @DataId
    private Long dataId = 1L;

    @StartsWith("a")
    private String startsWith = "abc";

    @EndsWith("c")
    private String endsWith = "abc";

    @YesOrNo(yes = "y", no = "n")
    private String yesOrNo = "y";

    @ZipCode
    private String zipCode = "123456";

    @UserName
    private String userName = "李四";

    @TimeCheck
    private String timeCheck = "12:00:01";

    @DateCheck
    private String dateCheck = "2022-01-01";

    @DateTimeCheck
    private String datetimeCheck = "2022-01-01 12:00:01";

    @Md5(regexp = "^([a-zA-Z0-9]{16}|[a-zA-Z0-9]{32})$")
    private String md5Check;

    @In("A,B,C")
    private String inChek = "A";

    @NotIn({"A","B","C"})
    private String notInChek = "D";

    @MobilePhone
    private String mobile = "18079637001";

    @Numeric(min = 100, max = 200)
    private String numeric = "90.2";

    @HeadTail(head = "a", tail = "c")
    private String headTail = "abc";

    @NotNull
    @SimpleEmail
    private String email = "yyy@qq.com";

    @BankCard
    private String bankCard = "1234567890123456789";

    @Amount(fraction = 2)
    private BigDecimal amount = new BigDecimal("200.5");

    @AreaCode
    private String areaCode = "360421";

    @NotNullOrBlank
    private String notNullOrBlank = "1";

    @NotNullOrEmpty
    private String notNullOrEmpty = "1";

}
