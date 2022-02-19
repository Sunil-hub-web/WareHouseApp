package com.example.warehouseapp.modelclass;

public class OrderRequest_ModelClass {

    String orderId,coustomerName,OrderDate,paymentStatus,status,totalAmount;

    public OrderRequest_ModelClass(String orderId, String coustomerName, String orderDate, String paymentStatus,
                                   String status, String totalAmount) {
        this.orderId = orderId;
        this.coustomerName = coustomerName;
        OrderDate = orderDate;
        this.paymentStatus = paymentStatus;
        this.status = status;
        this.totalAmount = totalAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCoustomerName() {
        return coustomerName;
    }

    public void setCoustomerName(String coustomerName) {
        this.coustomerName = coustomerName;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
