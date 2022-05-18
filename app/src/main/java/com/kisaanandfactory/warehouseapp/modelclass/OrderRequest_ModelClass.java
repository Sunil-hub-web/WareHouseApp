package com.kisaanandfactory.warehouseapp.modelclass;

import java.util.ArrayList;

public class OrderRequest_ModelClass {

    String orderId, coustomerName, OrderDate, paymentStatus, status, totalAmount, productQuantity, title,
            contacts, userID, house, street, locality, city, state, country, zip,paymentMethod,str_weight;

    ArrayList<Image_ModelClass> image_modelClasses;

    private boolean expanded;

    public OrderRequest_ModelClass(String orderId, String coustomerName, String orderDate, String paymentStatus,
                                   String status, String totalAmount, String productQuantity, String title,
                                   String contacts, String userID, String house, String street, String locality,
                                   String city, String state, String country, String zip,String paymentMethod,
                                   ArrayList<Image_ModelClass> image_modelClasses,String str_weight) {
        this.orderId = orderId;
        this.coustomerName = coustomerName;
        this.OrderDate = orderDate;
        this.paymentStatus = paymentStatus;
        this.status = status;
        this.totalAmount = totalAmount;
        this.productQuantity = productQuantity;
        this.title = title;
        this.contacts = contacts;
        this.userID = userID;
        this.house = house;
        this.street = street;
        this.locality = locality;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zip = zip;
        this.paymentMethod = paymentMethod;
        this.image_modelClasses = image_modelClasses;
        this.expanded = false;
        this.str_weight = str_weight;
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

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public ArrayList<Image_ModelClass> getImage_modelClasses() {
        return image_modelClasses;
    }

    public void setImage_modelClasses(ArrayList<Image_ModelClass> image_modelClasses) {
        this.image_modelClasses = image_modelClasses;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStr_weight() {
        return str_weight;
    }

    public void setStr_weight(String str_weight) {
        this.str_weight = str_weight;
    }
}