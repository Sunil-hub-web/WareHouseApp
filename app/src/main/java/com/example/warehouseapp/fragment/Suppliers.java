package com.example.warehouseapp.fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
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
import com.example.warehouseapp.adapter.SuppliersAdapter;
import com.example.warehouseapp.modelclass.Suppliers_ModelClass;
import com.example.warehouseapp.url.AppUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Suppliers extends Fragment {

    TextView text_All,text_Packed,text_Shipped;
    RecyclerView recyclerSuppliers;
    LinearLayoutManager linearLayoutManager;
    SuppliersAdapter suppliersAdapter;

    ArrayList<Suppliers_ModelClass> suppliers = new ArrayList<>();

    String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater,
                             @Nullable  ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.suppliers_fragment,container,false);

        text_All = view.findViewById(R.id.text_All);
        text_Packed = view.findViewById(R.id.text_Packed);
        text_Shipped = view.findViewById(R.id.text_Shipped);
        recyclerSuppliers = view.findViewById(R.id.recyclerSuppliers);

        token = SharedPrefManager.getInstance(getActivity()).getUser().getToken();

        text_Shipped.setBackgroundResource(R.drawable.edittextback);
        text_Packed.setBackgroundResource(R.drawable.edittextback);
        text_All.setBackgroundResource(R.drawable.button_back);
        text_All.setTextColor(Color.WHITE);

        getAllorder(token);

        text_Shipped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text_Shipped.setBackgroundResource(R.drawable.button_back);
                text_Shipped.setTextColor(Color.WHITE);
                text_Packed.setBackgroundResource(R.drawable.edittextback);
                text_Packed.setTextColor(Color.BLACK);
                text_All.setBackgroundResource(R.drawable.edittextback);
                text_All.setTextColor(Color.BLACK);

                getAllsuppliers(token);
            }
        });

        text_All.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text_Shipped.setBackgroundResource(R.drawable.edittextback);
                text_Shipped.setTextColor(Color.BLACK);
                text_Packed.setBackgroundResource(R.drawable.edittextback);
                text_Packed.setTextColor(Color.BLACK);
                text_All.setBackgroundResource(R.drawable.button_back);
                text_All.setTextColor(Color.WHITE);

                getAllorder(token);
            }
        });

        text_Packed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text_Shipped.setBackgroundResource(R.drawable.edittextback);
                text_Shipped.setTextColor(Color.BLACK);
                text_Packed.setBackgroundResource(R.drawable.button_back);
                text_Packed.setTextColor(Color.WHITE);
                text_All.setBackgroundResource(R.drawable.edittextback);
                text_All.setTextColor(Color.BLACK);

                suppliers.clear();

                getAllPacked(token);
            }
        });


        return view;
    }

    public void getAllsuppliers(String token){

        suppliers.clear();

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        TextView textView = progressDialog.findViewById(R.id.text);
        textView.setText("Shipped Details Please wait...");
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrl.getAllshipped, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String err = jsonObject.getString("err");

                    //suppliers = new ArrayList<>();

                    if(err.equals("false")){

                        String shippedOrders = jsonObject.getString("shippedOrders");
                        JSONArray jsonArray_shippedOrders = new JSONArray(shippedOrders);

                        for(int i=0;i<jsonArray_shippedOrders.length();i++){

                            JSONObject jsonObject_shippedOrders = jsonArray_shippedOrders.getJSONObject(i);

                            String paymentDetails = jsonObject_shippedOrders.getString("paymentDetails");
                            String trackingDetails = jsonObject_shippedOrders.getString("trackingDetails");
                            String shippingDetails = jsonObject_shippedOrders.getString("shippingDetails");
                            String expectedDelivery = jsonObject_shippedOrders.getString("expectedDelivery");
                            String _id = jsonObject_shippedOrders.getString("_id");
                            String products = jsonObject_shippedOrders.getString("products");
                            String totalAmount = jsonObject_shippedOrders.getString("totalAmount");
                            String orderStatus = jsonObject_shippedOrders.getString("orderStatus");

                            Suppliers_ModelClass suppliers_modelClass = new Suppliers_ModelClass(
                                    _id,orderStatus
                            );

                            suppliers.add(suppliers_modelClass);
                        }

                        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                        linearLayoutManager.setReverseLayout(true);
                        linearLayoutManager.setStackFromEnd(true);
                        suppliersAdapter = new SuppliersAdapter(getActivity(),suppliers);
                        recyclerSuppliers.setLayoutManager(linearLayoutManager);
                        recyclerSuppliers.setHasFixedSize(true);
                        recyclerSuppliers.setItemAnimator(new DefaultItemAnimator());
                        recyclerSuppliers.setAdapter(suppliersAdapter);

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

    public void getAllorder(String token){

        //suppliers.clear();

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        TextView textView = progressDialog.findViewById(R.id.text);
        textView.setText("Shipped Details Please wait...");
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrl.getAllOrder, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String err = jsonObject.getString("err");

                    //suppliers = new ArrayList<>();

                    if(err.equals("false")){

                        String data = jsonObject.getString("data");
                        JSONArray jsonArray_data = new JSONArray(data);

                        for(int i=0;i<jsonArray_data.length();i++){

                            JSONObject jsonObject_data = jsonArray_data.getJSONObject(i);

                            String paymentDetails = jsonObject_data.getString("paymentDetails");
                            String trackingDetails = jsonObject_data.getString("trackingDetails");
                            String shippingDetails = jsonObject_data.getString("shippingDetails");
                            String expectedDelivery = jsonObject_data.getString("expectedDelivery");
                            String discountPrice = jsonObject_data.getString("discountPrice");
                            String _id = jsonObject_data.getString("_id");
                            String oderedBy = jsonObject_data.getString("oderedBy");
                            String cart_id = jsonObject_data.getString("cart_id");
                            String shippingCharge = jsonObject_data.getString("shippingCharge");
                            String totalAmount = jsonObject_data.getString("totalAmount");
                            String seller = jsonObject_data.getString("seller");
                            String products = jsonObject_data.getString("products");
                            String orderStatus = jsonObject_data.getString("orderStatus");

                            Suppliers_ModelClass suppliers_modelClass = new Suppliers_ModelClass(
                                    _id,orderStatus
                            );

                            suppliers.add(suppliers_modelClass);
                        }

                        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                        linearLayoutManager.setReverseLayout(true);
                        linearLayoutManager.setStackFromEnd(true);
                        suppliersAdapter = new SuppliersAdapter(getActivity(),suppliers);
                        recyclerSuppliers.setLayoutManager(linearLayoutManager);
                        recyclerSuppliers.setHasFixedSize(true);
                        recyclerSuppliers.setItemAnimator(new DefaultItemAnimator());
                        recyclerSuppliers.setAdapter(suppliersAdapter);

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

    public void getAllPacked(String token){

        suppliers.clear();

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        TextView textView = progressDialog.findViewById(R.id.text);
        textView.setText("Packed Details Please wait...");
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrl.getAllpacked, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String err = jsonObject.getString("err");

                    //suppliers = new ArrayList<>();

                    if(err.equals("false")){

                        String msg = jsonObject.getString("msg");
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                        //JSONArray jsonArray_data = new JSONArray(data);

                       /* for(int i=0;i<jsonArray_data.length();i++){

                            JSONObject jsonObject_data = jsonArray_data.getJSONObject(i);

                            String paymentDetails = jsonObject_data.getString("paymentDetails");
                            String trackingDetails = jsonObject_data.getString("trackingDetails");
                            String shippingDetails = jsonObject_data.getString("shippingDetails");
                            String expectedDelivery = jsonObject_data.getString("expectedDelivery");
                            String discountPrice = jsonObject_data.getString("discountPrice");
                            String _id = jsonObject_data.getString("_id");
                            String oderedBy = jsonObject_data.getString("oderedBy");
                            String cart_id = jsonObject_data.getString("cart_id");
                            String shippingCharge = jsonObject_data.getString("shippingCharge");
                            String totalAmount = jsonObject_data.getString("totalAmount");
                            String seller = jsonObject_data.getString("seller");
                            String products = jsonObject_data.getString("products");
                            String orderStatus = jsonObject_data.getString("orderStatus");

                            Suppliers_ModelClass suppliers_modelClass = new Suppliers_ModelClass(
                                    _id,orderStatus
                            );

                            suppliers.add(suppliers_modelClass);
                        }

                        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                         linearLayoutManager.setReverseLayout(true);
                        linearLayoutManager.setStackFromEnd(true);
                        suppliersAdapter = new SuppliersAdapter(getActivity(),suppliers);
                        recyclerSuppliers.setLayoutManager(linearLayoutManager);
                        recyclerSuppliers.setHasFixedSize(true);
                        recyclerSuppliers.setItemAnimator(new DefaultItemAnimator());
                        recyclerSuppliers.setAdapter(suppliersAdapter);
*/
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
