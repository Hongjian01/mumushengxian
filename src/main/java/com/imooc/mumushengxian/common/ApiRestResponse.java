package com.imooc.mumushengxian.common;

import com.imooc.mumushengxian.exception.ImoocMallExceptionEnum;

/**
 * 描述：     通用返回对象
 */
public class ApiRestResponse <T>{
    private Integer code;
    private String msg;
    private T data;

    public ApiRestResponse(){
        this.code=10000;
        this.msg="success";
    }

    public ApiRestResponse(Integer code, String msg) {
        this.code=code;
        this.msg=msg;
    }

    public ApiRestResponse(Integer code,String msg,T data){
        this.code=code;
        this.msg=msg;
        this.data=data;
    }

    @Override
    public String toString() {
        return "ApiRestResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    //无数据的成功响应
    public static <T> ApiRestResponse<T> success(){
        return new ApiRestResponse<>();
    }

    //带数据的成功响应
    public static <T> ApiRestResponse<T> success(T data){
        return new ApiRestResponse<T>(10000,"success",data);
    }

    //自定义错误返回
    public static <T> ApiRestResponse<T> error(Integer code,String msg){
        return new ApiRestResponse<T>(code, msg);
    }

    //基于枚举的错误返回
    public static <T> ApiRestResponse<T> error(ImoocMallExceptionEnum ex){
        return new ApiRestResponse<>(ex.getCode(), ex.getMsg());
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
