package com.imooc.mumushengxian.model.request;

import jakarta.validation.constraints.NotBlank;

public class CreateOrderReq {
    @NotBlank(message = "收件人不能为空")
    private String receiverName;

    @NotBlank(message = "收件人电话不能为空")
    private String receiverMobile;

    @NotBlank(message = "收件地址不能为空")
    private String receiverAddress;

    //邮费
    private Integer postage = 0;

    //支付类型：微信 or 支付宝
    private Integer paymentType = 1;

    @Override
    public String toString() {
        return "CreateOrderReq{" +
                "receiverName='" + receiverName + '\'' +
                ", receiverMobile='" + receiverMobile + '\'' +
                ", receiverAddress='" + receiverAddress + '\'' +
                ", postage=" + postage +
                ", paymentType=" + paymentType +
                '}';
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public Integer getPostage() {
        return postage;
    }

    public void setPostage(Integer postage) {
        this.postage = postage;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }
}
