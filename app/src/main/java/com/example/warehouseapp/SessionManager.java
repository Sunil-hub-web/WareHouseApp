package com.example.warehouseapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    SharedPreferences sharedprefernce;
    SharedPreferences.Editor editor;
    SharedPreferences sharedprefernceCoupon;
    SharedPreferences.Editor editorCoupon;
    Context context;
    int PRIVATE_MODE=0;
    private static final String PREF_NAME="sharedcheckLogin";
    private static final String PREF_NAME2="sharedcheckLogin2";
    private static final String FCMId="FCMId";

    public SessionManager(Context context){

        this.context =  context;
        sharedprefernce = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor=sharedprefernce.edit();

       /* sharedprefernceCoupon=context.getSharedPreferences(PREF_NAME2,PRIVATE_MODE);
        editorCoupon=sharedprefernceCoupon.edit();*/

    }

    public void setFCMId(String number){

        editorCoupon.putString(FCMId,number);
        editorCoupon.commit();
    }

    public String getFCMId(){

        return sharedprefernceCoupon.getString(FCMId,"");
    }


}
