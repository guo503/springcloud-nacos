package com.nacos.common.response.enums;

/**
 * api请求错误码
 *
 * @author caiLinFeng
 * @date 2018年2月2日
 */
public enum ErrorCodeEnum {
    OK(0, "success"),
    UNDEFINE_ERROR(101, "未定义的错误");

    ErrorCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;

    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
