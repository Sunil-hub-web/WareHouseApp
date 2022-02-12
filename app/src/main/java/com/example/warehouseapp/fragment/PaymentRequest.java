package com.example.warehouseapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.warehouseapp.R;
import com.example.warehouseapp.SharedPrefManager;
import com.example.warehouseapp.activity.ViewUserProfile;
import com.example.warehouseapp.adapter.PaymentRequestAdapter;
import com.example.warehouseapp.modelclass.PaymentHistory_ModelClass;
import com.example.warehouseapp.url.AppUrl;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PaymentRequest extends Fragment {

    RecyclerView recyclerPaymentRequest;
    ArrayList<PaymentHistory_ModelClass> payment = new ArrayList<>();
    PaymentRequestAdapter paymentRequestAdapter;
    LinearLayoutManager linearLayoutManager;
    String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater,
                             @Nullable  ViewGroup container,
                             @Nullable  Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.paymentrequest_fragment,container,false);

        recyclerPaymentRequest = view.findViewById(R.id.recyclerPaymentRequest);

        token = SharedPrefManager.getInstance(getActivity()).getUser().getToken();

        paymentRequest(token);

        return view;
    }

    public void paymentRequest(String token){

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        TextView textView = progressDialog.findViewById(R.id.text);
        textView.setText("Get Payment Details Please wait...");
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrl.paymenthistorys, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String err = jsonObject.getString("err");
                    if(err.equals("false")){

                        String message = jsonObject.getString("msg");
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                        String data = jsonObject.getString("data");
                        JSONArray jsonArray_data = new JSONArray(data);

                        for (int i=0;i<jsonArray_data.length();i++){

                            JSONObject jsonObject_data = jsonArray_data.getJSONObject(i);

                            String id = jsonObject_data.getString("_id");
                            String user_id = jsonObject_data.getString("user_id");
                            String cart_id = jsonObject_data.getString("cart_id");
                            String amount = jsonObject_data.getString("amount");
                            //String success = jsonObject_data.getString("success");
                            //String payment_status = jsonObject_data.getString("payment_status");

                           /* JSONObject jsonObject_payment_status = new JSONObject(payment_status);

                            String payment_id = jsonObject_payment_status.getString("id");
                            String entity = jsonObject_payment_status.getString("entity");
                            String payment_amount = jsonObject_payment_status.getString("amount");
                            String currency = jsonObject_payment_status.getString("currency");
                            String status = jsonObject_payment_status.getString("status");
                            String order_id = jsonObject_payment_status.getString("order_id");
                            String invoice_id = jsonObject_payment_status.getString("invoice_id");
                            String international = jsonObject_payment_status.getString("international");
                            String method = jsonObject_payment_status.getString("method");
                            String amount_refunded = jsonObject_payment_status.getString("amount_refunded");
                            String wallet = jsonObject_payment_status.getString("wallet");
                            String description = jsonObject_payment_status.getString("description");*/

                            PaymentHistory_ModelClass paymentHistory_modelClass = new PaymentHistory_ModelClass(
                                     amount,user_id,"08-02-2022 : 19:45"
                            );

                            payment.add(paymentHistory_modelClass);

                            Log.d("payment",payment.toString());
                        }

                        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                        //recyclerPaymentRequest.setItemAnimator(new DefaultItemAnimator());
                        paymentRequestAdapter = new PaymentRequestAdapter(payment,getActivity());
                        recyclerPaymentRequest.setLayoutManager(linearLayoutManager);
                        recyclerPaymentRequest.setHasFixedSize(true);
                        recyclerPaymentRequest.setAdapter(paymentRequestAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                error.printStackTrace();
                Toast.makeText(getActivity(), error+"", Toast.LENGTH_SHORT).show();

            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String,String> header = new HashMap<>();
                header.put("auth-token",token);
                return header;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,3,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}
