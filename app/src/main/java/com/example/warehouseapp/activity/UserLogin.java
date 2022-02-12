package com.example.warehouseapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.warehouseapp.R;
import com.example.warehouseapp.SharedPrefManager;
import com.example.warehouseapp.modelclass.Login_ModelClass;
import com.example.warehouseapp.url.AppUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserLogin extends AppCompatActivity {

    EditText eidt_mobileNumber,edit_Password;
    Button btn_Login;
    TextView text_signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        eidt_mobileNumber = findViewById(R.id.eidt_mobileNumber);
        edit_Password = findViewById(R.id.edit_Password);
        btn_Login = findViewById(R.id.btn_Login);
        text_signUp = findViewById(R.id.text_signUp);

        text_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserLogin.this,UserRegister.class);
                startActivity(intent);
                finish();
            }
        });

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String emailid = eidt_mobileNumber.getText().toString().trim();
                String password = edit_Password.getText().toString().trim();

                //Long mobile_no = Long.valueOf(mobile);

                if(TextUtils.isEmpty(eidt_mobileNumber.getText())){

                    eidt_mobileNumber.setError("Please enter EmailId no");

                }else if(TextUtils.isEmpty(edit_Password.getText())){

                    edit_Password.setError("Please enter password");

                }else {

                    userLogin(emailid, password);

                    Intent intent = new Intent(UserLogin.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void userLogin(String emailid,String password){

        ProgressDialog progressDialog = new ProgressDialog(UserLogin.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        TextView textView = progressDialog.findViewById(R.id.text);
        textView.setText("Login Please wait...");
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);


        Map<String,Object> params = new HashMap<>();

        params.put("emailID",emailid);
        params.put("password",password);
        params.put("fcm_id","adsasadasdd");

        JSONObject jsonObject1 = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppUrl.loginUser, jsonObject1, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Ranj_Login_response",response.toString());

                progressDialog.dismiss();

                try {
                    String message = response.getString("msg");

                    if(message.equals("Successfully logged in!")){

                        Toast.makeText(UserLogin.this, message, Toast.LENGTH_SHORT).show();

                        String mobile = response.getString("mobile");
                        String email = response.getString("emailID");
                        String token = response.getString("token");
                        //String password = edit_Password.getText().toString().trim();

                        Login_ModelClass login_modelClass = new Login_ModelClass(mobile,email,token,password);

                        SharedPrefManager.getInstance(UserLogin.this).userLogin(login_modelClass);

                        Intent intent = new Intent(UserLogin.this, MainActivity.class);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss ();

                /*error.printStackTrace();
                Log.d("Ranj_Login_error",error.toString());
                Toast.makeText (UserLoginPage.this, ""+error+"User Name password Not Match", Toast.LENGTH_LONG).show ( );*/

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(UserLogin.this, "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                }else {

                    Log.d("successresponceVolley", "" + error.networkResponse);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);
//                            if (error.networkResponse.statusCode == 400) {
                            String data = jsonError.getString("msg");
                            Toast.makeText(UserLogin.this, data, Toast.LENGTH_SHORT).show();

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
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(UserLogin.this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (SharedPrefManager.getInstance(UserLogin.this).isLoggedIn()) {

            Intent intent = new Intent(UserLogin.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
            this.finish();
    }
}