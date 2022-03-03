package com.kisaanandfactory.warehouseapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kisaanandfactory.warehouseapp.R;
import com.kisaanandfactory.warehouseapp.SharedPrefManager;
import com.kisaanandfactory.warehouseapp.url.AppUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomePage extends Fragment {

    TextView complaints_number,complaints,bottomtotalproduct,bottominstock,bottompending,
            totalordertxt,pendingorderstxt,dispatchedordertxt;

    String token;
    ProgressBar totalordersprogressbar,pendingordersprogressbar,dispatchedordersprogressbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater,
                             @Nullable  ViewGroup container,
                             @Nullable  Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.homepagedashboard,container,false);

        complaints = view.findViewById(R.id.complaints);
        complaints_number = view.findViewById(R.id.complaints_number);
        bottomtotalproduct = view.findViewById(R.id.bottomtotalproduct);
        bottominstock = view.findViewById(R.id.bottominstock);
        bottompending = view.findViewById(R.id.bottompending);
        totalordertxt = view.findViewById(R.id.totalordertxt);
        pendingorderstxt = view.findViewById(R.id.pendingorderstxt);
        dispatchedordertxt = view.findViewById(R.id.dispatchedordertxt);
        dispatchedordersprogressbar = view.findViewById(R.id.dispatchedordersprogressbar);
        pendingordersprogressbar = view.findViewById(R.id.pendingordersprogressbar);
        totalordersprogressbar = view.findViewById(R.id.totalordersprogressbar);

        token = SharedPrefManager.getInstance(getActivity()).getUser().getToken();
        getHomeDetails(token);

        return view;
    }

    public void getcomplaints(String token){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrl.getAllcomplaints, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String err = jsonObject.getString("err");

                    if(err.equals("false")){

                        String message = jsonObject.getString("msg");

                        complaints_number.setText(message);
                        complaints.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();

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
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    public void getHomeDetails(String token){

        String url = AppUrl.getHomeDetailsZip + "122001";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String err = jsonObject.getString("err");

                    if(err.equals("false")){

                        getcomplaints(token);

                        String message = jsonObject.getString("msg");
                        String data = jsonObject.getString("data");

                        JSONObject jsonObject_data = new JSONObject(data);

                        String totalOrder = jsonObject_data.getString("totalOrder");
                        String Pending = jsonObject_data.getString("Pending");
                        String Dispatched = jsonObject_data.getString("Dispatched");
                        String totalProduct = jsonObject_data.getString("totalProduct");
                        String OutOfStock = jsonObject_data.getString("OutOfStock");

                        bottomtotalproduct.setText(totalProduct);
                        //bottominstock.setText(totalOrder);
                        bottompending.setText(Pending);


                        float toto = Float.valueOf(totalOrder);
                        float peno = Float.valueOf(Pending);
                        int diso = Integer.parseInt(Dispatched);
                        float penavg,disavg;
                        int penavg1,disavg1;

                        if(toto==0 || peno==0) {
                            penavg = 0;
                        }else{
                            penavg = (peno / toto) * 100;
                            Log.d("penavg", String.valueOf(penavg));
                        }

                        if(toto==0 || diso==0) {
                            disavg = 0;
                        }else{
                            disavg = (diso/toto) * 100;
                        }

                        penavg1 = (int)Math.round(penavg);
                        disavg1 = (int)Math.round(disavg);

                       /* penavg1 = (int)penavg;
                        disavg1 = (int)disavg;*/

                        totalordertxt.setText("100%");
                        pendingorderstxt.setText(penavg1+"%");
                        dispatchedordertxt.setText(disavg1+"%");

                        pendingordersprogressbar.setProgress(penavg1);
                        dispatchedordersprogressbar.setProgress(disavg1);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
