package com.kisaanandfactory.warehouseapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    SharedPreferences sharedprefernce;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE=0;
    private static final String PREF_NAME="sharedcheckLogin";
    private static final String PREF_NAME2="sharedcheckLogin2";
    private static final String FCMId="FCMId";
    private static final String USERID="userid";
    private static final String KEY_ZipCode = "zipcode";

    public SessionManager(Context context){

        this.context =  context;
        sharedprefernce = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedprefernce.edit();

       /* sharedprefernceCoupon=context.getSharedPreferences(PREF_NAME2,PRIVATE_MODE);
        editorCoupon=sharedprefernceCoupon.edit();*/

    }

    public String getUserId(){

        return sharedprefernce.getString(FCMId,"");
    }

    public void setUserId(String number){

        editor.putString(FCMId,number);
        editor.commit();
    }

    public String getUserId_Delivery(){

        return sharedprefernce.getString(USERID,"");
    }

    public void setUserId_Delivery(String number){

        editor.putString(USERID,number);
        editor.commit();
    }

    public String getZipCode() {
        return sharedprefernce.getString(KEY_ZipCode,"defvalur");
    }

    public void setZipCode(String zipCode) {

        editor.putString(KEY_ZipCode,zipCode);
        editor.commit();

    }

}
