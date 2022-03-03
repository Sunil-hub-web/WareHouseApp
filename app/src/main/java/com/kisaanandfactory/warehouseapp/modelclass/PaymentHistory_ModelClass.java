package com.kisaanandfactory.warehouseapp.modelclass;

public class PaymentHistory_ModelClass {

    String amount,orderId,datetime,createdAt;

    public PaymentHistory_ModelClass(String amount, String orderId, String datetime,String createdAt) {
        this.amount = amount;
        this.orderId = orderId;
        this.datetime = datetime;
        this.createdAt = createdAt;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
