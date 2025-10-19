package com.imooc.mumushengxian.exception;

/**
 * 描述：     统一异常:自定义业务异常
 */
public class ImoocMallException extends Exception {
    private static Integer code;
    private static String msg;

    public ImoocMallException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ImoocMallException(ImoocMallExceptionEnum ex){
        this(ex.getCode(), ex.getMsg());
    }

    public static Integer getCode() {
        return code;
    }

    public static String getMsg() {
        return msg;
    }
}
