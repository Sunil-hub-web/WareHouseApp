package com.example.warehouseapp.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.warehouseapp.R;
import com.example.warehouseapp.url.AppUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.aabhasjindal.otptextview.OtpTextView;

public class OtpVerification extends AppCompatActivity {

    TextView text_back,text_timer,text_resend;
    Button btn_Submit;
    OtpTextView otp_view;
    String str_UserName,str_MobileNumber,str_EMail,str_Password,str_CountryCode,verify_otp;
    long millisUntilFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        btn_Submit = findViewById(R.id.btn_Submit);
        text_back = findViewById(R.id.text_back);
        text_timer = findViewById(R.id.text_timer);
        otp_view = findViewById(R.id.otp_view);
        text_resend = findViewById(R.id.text_resend);

        timer();

        Intent intent = getIntent();
        str_MobileNumber = intent.getStringExtra("mobile");
        str_CountryCode = intent.getStringExtra("countryCode");


        text_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timer();
            }
        });

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(otp_view.getOTP().length() < 4){

                    Toast.makeText(OtpVerification.this, "Fill All Fields", Toast.LENGTH_LONG).show();

                }else{

                    verify_otp = otp_view.getOTP();

                    verifayOtp(str_CountryCode,str_MobileNumber,verify_otp);
                }
            }
        });
    }

    public void verifayOtp(String countryCode,String Mobile,String Otp){

        ProgressDialog progressDialog = new ProgressDialog(OtpVerification.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        TextView textView = progressDialog.findViewById(R.id.text);
        textView.setText("Verifay Otp  Please wait...");
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        Map<String,String> params = new HashMap<>(  );

        params.put("countryCode",countryCode);
        params.put("mobile",Mobile);
        params.put("otp",Otp);

        JSONObject jsonObject1 = new JSONObject ( params );

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest (Request.Method.PUT, AppUrl.verifyotp, jsonObject1, new Response.Listener<JSONObject> ( ) {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {

                progressDialog.dismiss ();

                try {

                    String message = response.getString("msg");
                    Toast.makeText(OtpVerification.this, message, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(OtpVerification.this, UserLogin.class);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace ( );
                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss ();
                error.printStackTrace();
                Toast.makeText (OtpVerification.this, ""+error, Toast.LENGTH_SHORT).show ( );


            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,3,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue (OtpVerification.this);
        requestQueue.add (jsonObjectRequest);

    }

    public void ResendtOtp(String mobileNo){

        ProgressDialog progressDialog = new ProgressDialog(OtpVerification.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        TextView textView = progressDialog.findViewById(R.id.text);
        textView.setText("Otp Send  Please wait...");
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        Map<String,String> params = new HashMap<>(  );

        params.put("mobile",mobileNo);
        params.put("countryCode",str_CountryCode);

        JSONObject jsonObject = new JSONObject ( params );

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest (Request.Method.POST, AppUrl.resendotp, jsonObject, new Response.Listener<JSONObject> ( ) {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {

                progressDialog.dismiss ();

                try {
                    String msg = response.getString ("msg");

                    Toast.makeText(OtpVerification.this, msg, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace ( );
                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss ();
                error.printStackTrace();
                Toast.makeText (OtpVerification.this, ""+error, Toast.LENGTH_SHORT).show ( );


            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,3,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue (OtpVerification.this);
        requestQueue.add (jsonObjectRequest);

    }

    public void timer(){

        //Initialize time duration
        //long duration = TimeUnit.MINUTES.toMillis(1);
        //Initialize countdown timer

        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {

                text_timer.setText(millisUntilFinished/1000+"s");
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {

                text_timer.setText("00");
            }

        }.start();
    }
}