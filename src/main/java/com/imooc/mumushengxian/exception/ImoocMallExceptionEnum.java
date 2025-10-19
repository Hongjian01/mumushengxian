package com.imooc.mumushengxian.exception;

/**
 * 描述：     异常枚举类
 */
public enum ImoocMallExceptionEnum {
    NEED_USER_NAME(10001, "用户名不能为空"),
    NEED_PASSWORD(10002, "密码不能为空"),
    USER_NOT_EXIST(10003, "用户不存在"),
    WRONG_PASSWORD(10004, "密码错误"),
    USER_REGISTER_FAILED(10005, "用户注册失败"),
    USER_ALREADY_EXISTED(10006, "用户已存在"),
    USER_NEED_LOGIN(10007, "用户需要登录"),
    UPDATE_FAILED(10008, "用户信息更新失败"),
    NEED_ADMIN(10009, "需要管理员登陆"),
    CATEGORY_ALREADY_EXISTED(10010, "商品目录已经存在"),
    CATEGORY_ADD_FAILED(10011, "商品目录添加失败"),
    REQUEST_PARAM_ERROR(10012, "参数错误"),
    CATEGORY_NOT_EXISTED(10013, "商品目录不存在"),
    CATEGORY_UPDATE_FAILED(10014, "商品目录更新失败"),
    CATEGORY_DELETE_FAILED(10015, "商品目录删除失败"),
    PRODUCT_ADD_FAILED(10016, "添加商品失败"),
    PRODUCT_UPDATE_FAILED(10017, "更新商品失败"),
    PRODUCT_NOT_EXISTED(10018, "商品不存在"),
    PRODUCT_DELETE_FAILED(10019, "商品删除失败"),
    BATCH_UPDATE_FAILED(10020, "批量更新商品售卖状态失败"),
    PARAMETER_ERROR(10021, "数组参数错误"),
    CART_NOT_EXISTED(10022, "购物车不存在"),
    Cart_DELETED_FAILED(10023, "购物车删除失败"),
    CART_SELECTED_FAILED(10024, "购物车选中失败"),
    CART_EMPTY(10025, "购物车为空"),
    CART_PRODUCT_EMPTY(10026, "购物车中没有被选中的商品"),
    PRODUCT_NOT_ENOUGH(10027, "商品库存不足"),
    PRODUCT_NOT_SAIL(10028, "商品为非售卖状态"),
    NO_ENUM(10029, "未找到对应的枚举"),
    NO_ORDER(10030, "无订单"),
    NOT_YOUR_ORDER(10031, "不是你的订单"),
    WRONG_ORDER_STATUS(10032, "订单状态不符"),

    SYSTEM_ERROR(20000, "系统异常，请从控制台或日志中查看具体错误信息"),
    ;
    /**
     * 异常码
     */
    Integer code;
    /**
     * 异常信息
     */
    String msg;

    ImoocMallExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
