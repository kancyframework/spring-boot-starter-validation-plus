package javax.validation.constraints;

/**
 * Regexps
 *
 * @author huangchengkang
 * @date 2022/6/21 22:35
 */
public interface Regexps {
    String zipCode = "^\\d{6}$";
    String areaCode = "^\\d{6}$";
    String mobile = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";
    String idCardNo = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
    String idCardNo18 = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
    String idCardNo15 = "^[1-9]\\d{5}\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{2}[0-9Xx]$";
    String email = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    String userName = "^(([a-zA-Z+\\.?\\·?a-zA-Z+]{2,30}$)|([\\u4e00-\\u9fa5+\\·?\\u4e00-\\u9fa5+]{2,30}$))";
    String zhUserName = "^[\\u4e00-\\u9fa5+\\·?\\u4e00-\\u9fa5+]{2,30}$";
    String enUserName = "^[a-zA-Z+\\.?\\·?a-zA-Z+]{2,30}$";
    String md5 = "^[a-zA-Z0-9]{32}$";
}
