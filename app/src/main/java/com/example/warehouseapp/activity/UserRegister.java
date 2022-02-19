package com.example.warehouseapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.warehouseapp.R;
import com.example.warehouseapp.url.AppUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserRegister extends AppCompatActivity {

    EditText edit_UserName, edit_MobileNumber, edit_EMail, edit_Password, edit_ConfirmPassword;
    String str_UserName, str_MobileNumber, str_EMail, str_Password, str_CountryCode;
    Button btn_CreateAccount;
    AwesomeValidation awesomeValidation;
    int country_code;
    TextView text_LoginPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        edit_UserName = findViewById(R.id.edit_UserName);
        edit_MobileNumber = findViewById(R.id.edit_MobileNumber);
        edit_Password = findViewById(R.id.edit_Password);
        edit_ConfirmPassword = findViewById(R.id.edit_ConfirmPassword);
        edit_EMail = findViewById(R.id.edit_EMail);
        btn_CreateAccount = findViewById(R.id.btn_CreateAccount);
        text_LoginPage = findViewById(R.id.text_LoginPage);

        str_CountryCode = "91";
        country_code = Integer.valueOf(str_CountryCode);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(UserRegister.this, R.id.edit_UserName, "^[A-Za-z\\s]{5,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.entername);
        awesomeValidation.addValidation(UserRegister.this, R.id.edit_MobileNumber, "^[0-9]{10}$", R.string.entercontact);
        awesomeValidation.addValidation(UserRegister.this, R.id.edit_EMail, Patterns.EMAIL_ADDRESS, R.string.enteremail);
        //awesomeValidation.addValidation (UserSignUpPage.this,R.id.edit_Password,"^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$",R.string.enterpassword);
        //awesomeValidation.addValidation (UserSignUpPage.this,R.id.edit_ConfirmPassword,"^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$",R.string.enterpassword);


        btn_CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    str_UserName = edit_UserName.getText().toString().trim();
                    str_MobileNumber = edit_MobileNumber.getText().toString().trim();
                    str_EMail = edit_EMail.getText().toString().trim();
                    str_Password = edit_Password.getText().toString().trim();

                    Long mobile = Long.valueOf(str_MobileNumber);

                    if (awesomeValidation.validate()) {

                        if (edit_Password.getText().toString().trim().equals(edit_ConfirmPassword.getText().toString().trim())) {

                            // RequestOtp(str_MobileNumber);

                            //Toast.makeText(UserRegister.this, "Success", Toast.LENGTH_SHORT).show();

                            userRegister(str_UserName, mobile, str_EMail, str_Password, country_code);


                        } else {

                            edit_ConfirmPassword.setError("Password not match");
                            //edit_Password.setError("password not match");
                        }

                    } else {

                        Toast.makeText(UserRegister.this, "Write Proper Format", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }


            }
        });

        text_LoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserRegister.this, UserLogin.class);
                startActivity(intent);
            }
        });

    }

    public void userRegister(String userName, Long mobileNo, String emailid, String password, int countryCode) {

        ProgressDialog progressDialog = new ProgressDialog(UserRegister.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        TextView textView = progressDialog.findViewById(R.id.text);
        textView.setText("Register  Please wait...");
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        JSONObject jsonObjectRegister = new JSONObject();
        try {


            jsonObjectRegister.put("name", userName);
            jsonObjectRegister.put("mobile", mobileNo);
            jsonObjectRegister.put("emailID", emailid);
            jsonObjectRegister.put("password", password);
            jsonObjectRegister.put("countryCode", 91);

        } catch (Exception e) {

        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppUrl.registerUser, jsonObjectRegister, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Ranj_Register", response.toString());

                progressDialog.dismiss();

                try {

                    String message = response.getString("msg");
                    Toast.makeText(UserRegister.this, message, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(UserRegister.this, OtpVerification.class);
                    intent.putExtra("countryCode", "91");
                    intent.putExtra("mobile", mobileNo.toString());
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);
//                            if (error.networkResponse.statusCode == 400) {
                            String data = jsonError.getString("msg");
                            Toast.makeText(UserRegister.this, data, Toast.LENGTH_SHORT).show();

//                            } else if (error.networkResponse.statusCode == 404) {
//                                JSONArray data = jsonError.getJSONArray("msg");
//                                JSONObject jsonitemChild = data.getJSONObject(0);
//                                String ms = jsonitemChild.toString();
//                                Toast.makeText(RegisterActivity.this, ms, Toast.LENGTH_SHORT).show();
//
//                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("successresponceVolley", "" + e);
                        }
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> param = new HashMap<String, String>();

                return param;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(UserRegister.this);
        requestQueue.add(jsonObjectRequest);

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}