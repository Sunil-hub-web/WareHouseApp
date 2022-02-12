package com.example.warehouseapp.modelclass;

public class Login_ModelClass {

    String mobileNo,emailId,token,password;

    public Login_ModelClass(String mobileNo, String emailId, String token, String password) {
        this.mobileNo = mobileNo;
        this.emailId = emailId;
        this.token = token;
        this.password = password;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
