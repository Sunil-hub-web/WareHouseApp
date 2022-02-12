package com.example.warehouseapp.modelclass;

public class PaymentHistory_ModelClass {

    String amount,orderId,datetime;

    public PaymentHistory_ModelClass(String amount, String orderId, String datetime) {
        this.amount = amount;
        this.orderId = orderId;
        this.datetime = datetime;
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
}
