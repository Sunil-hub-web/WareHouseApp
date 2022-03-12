package com.kisaanandfactory.warehouseapp.fragment;

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
import com.kisaanandfactory.warehouseapp.R;
import com.kisaanandfactory.warehouseapp.SharedPrefManager;
import com.kisaanandfactory.warehouseapp.adapter.OrderRequestAdapter;
import com.kisaanandfactory.warehouseapp.modelclass.AllProduct_ModelClass;
import com.kisaanandfactory.warehouseapp.modelclass.Image_ModelClass;
import com.kisaanandfactory.warehouseapp.modelclass.OrderRequest_ModelClass;
import com.kisaanandfactory.warehouseapp.modelclass.Weight_ModelClass;
import com.kisaanandfactory.warehouseapp.url.AppUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderRequest extends Fragment {

    RecyclerView recyclerOrderRequest;
    OrderRequestAdapter orderRequestAdapter;
    LinearLayoutManager linearLayoutManager;
    ArrayList<OrderRequest_ModelClass> orderrequest = new ArrayList<>();
    String token;

    ArrayList<AllProduct_ModelClass> product;
    ArrayList<Image_ModelClass> productimage;
    ArrayList<Weight_ModelClass> productweight;

    String productQuantity,title;

    @Nullable
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater,
                             @Nullable  ViewGroup container,
                             @Nullable  Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.orderrequest_fragment,container,false);

        recyclerOrderRequest = view.findViewById(R.id.recyclerOrderRequest);

        token = SharedPrefManager.getInstance(getActivity()).getUser().getToken();

        getOrderRequest(token);

        return view;
    }

    public void getOrderRequest(String token){

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        TextView textView = progressDialog.findViewById(R.id.text);
        textView.setText("Retrive Order Details Please wait...");
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrl.getAllOrder, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String err = jsonObject.getString("err");
                    if(err.equals("false")){

                        String data = jsonObject.getString("data");
                        JSONArray jsonArray_data = new JSONArray(data);

                        for(int i=0;i<jsonArray_data.length();i++){

                            JSONObject jsonObject_data = jsonArray_data.getJSONObject(i);

                            String paymentDetails = jsonObject_data.getString("paymentDetails");
                            String trackingDetails = jsonObject_data.getString("trackingDetails");
                            String shippingDetails = jsonObject_data.getString("shippingDetails");
                            String discountPrice = jsonObject_data.getString("discountPrice");
                            String id = jsonObject_data.getString("_id");
                            String oderedBy = jsonObject_data.getString("oderedBy");
                            String cart_id = jsonObject_data.getString("cart_id");
                            String orderImg = jsonObject_data.getString("orderImg");
                            String shippingCharge = jsonObject_data.getString("shippingCharge");
                            String totalAmount = jsonObject_data.getString("totalAmount");
                            String orderStatus = jsonObject_data.getString("orderStatus");
                            //String deliveryAgent = jsonObject_data.getString("deliveryAgent");
                            //String warehouse_id = jsonObject_data.getString("warehouse_id");
                            String products = jsonObject_data.getString("products");
                            String DriverDettails = jsonObject_data.getString("DriverDettails");


                            JSONObject jsonObject_paymentDetails = new JSONObject(paymentDetails);

                            String paymentMethod = jsonObject_paymentDetails.getString("paymentMethod");
                            String paymentStatus = jsonObject_paymentDetails.getString("paymentStatus");

                            JSONObject jsonObject_trackingDetails = new JSONObject(trackingDetails);

                            String ordered = jsonObject_trackingDetails.getString("ordered");
                            //String deliveryDate = jsonObject_trackingDetails.getString("deliveryDate");

                            JSONObject jsonObject_shippingDetails = new JSONObject(shippingDetails);

                            String name = jsonObject_shippingDetails.getString("name");
                            String addressID = jsonObject_shippingDetails.getString("addressID");
                            String contacts = jsonObject_shippingDetails.getString("contacts");
                            String address_details = jsonObject_shippingDetails.getString("address_details");

                            JSONObject jsonObject_address_details = new JSONObject(address_details);

                            String default1 = jsonObject_address_details.getString("default");
                            String id1 = jsonObject_address_details.getString("_id");
                            String userID = jsonObject_address_details.getString("userID");
                            String house = jsonObject_address_details.getString("house");
                            String street = jsonObject_address_details.getString("street");
                            String locality = jsonObject_address_details.getString("locality");
                            String city = jsonObject_address_details.getString("city");
                            String state = jsonObject_address_details.getString("state");
                            String country = jsonObject_address_details.getString("country");
                            String zip = jsonObject_address_details.getString("zip");
                            String longitude = jsonObject_address_details.getString("longitude");
                            String latitude = jsonObject_address_details.getString("latitude");

                            product = new ArrayList<>();

                            JSONArray jsonArray_products = new JSONArray(products);

                            for (int j = 0; j < jsonArray_products.length(); j++) {

                                JSONObject jsonObject_product = jsonArray_products.getJSONObject(j);

                                String proid = jsonObject_product.getString("_id");
                                String productId = jsonObject_product.getString("productId");
                                productQuantity = jsonObject_product.getString("productQuantity");
                                String product_pro = jsonObject_product.getString("product");

                                JSONObject jsonObject_product_pro = new JSONObject(product_pro);


                                String discount = jsonObject_product_pro.getString("discount");
                                String productid = jsonObject_product_pro.getString("_id");
                                title = jsonObject_product_pro.getString("title");
                                String price = jsonObject_product_pro.getString("price");
                                String type = jsonObject_product_pro.getString("type");
                                String description = jsonObject_product_pro.getString("description");
                                String totalRating = jsonObject_product_pro.getString("totalRating");
                                String inStock = jsonObject_product_pro.getString("inStock");
                                String experience = jsonObject_product_pro.getString("experience");
                                String subcategoryId = jsonObject_product_pro.getString("subcategoryId");
                                String soldBy = jsonObject_product_pro.getString("soldBy");
                                String createdAt = jsonObject_product_pro.getString("createdAt");


                                productimage = new ArrayList<>();

                                String images = jsonObject_product_pro.getString("images");

                                JSONArray jsonArray_images = new JSONArray(images);

                                for (int k = 0; k < jsonArray_images.length(); k++) {

                                    String image = jsonArray_images.get(k).toString();
                                    Log.d("Ranj_image",image);


                                    Image_ModelClass image_modelClass = new Image_ModelClass(
                                            image
                                    );

                                    productimage.add(image_modelClass);

                                }

                                Log.d("vegetableimage",productimage.toString());

                                productweight = new ArrayList<>();

                                String weight = jsonObject_product_pro.getString("weight");

                                JSONArray jsonArray_weight = new JSONArray(weight);


                                for (int l = 0; l < jsonArray_weight.length(); l++) {

                                    String str_weight = jsonArray_weight.get(l).toString();


                                    Weight_ModelClass weight_modelClass = new Weight_ModelClass(
                                            str_weight
                                    );
                                    productweight.add(weight_modelClass);

                                }

                                AllProduct_ModelClass allProduct_modelClass = new AllProduct_ModelClass(
                                        discount,id,title,price,type,description,totalRating,"",inStock,experience,soldBy,productimage,productweight,createdAt
                                );

                                product.add(allProduct_modelClass);

                            }

                            JSONArray jsonArray_DriverDettails = new JSONArray(DriverDettails);

                            for(int m=0;m<jsonArray_DriverDettails.length();m++){

                                JSONObject jsonObject_DriverDettails = jsonArray_DriverDettails.getJSONObject(m);
                                String status = jsonObject_DriverDettails.getString("status");
                                String _id = jsonObject_DriverDettails.getString("_id");
                            }

                            OrderRequest_ModelClass orderRequest_modelClass = new OrderRequest_ModelClass(
                                    oderedBy,name,ordered,paymentStatus,orderStatus,totalAmount,productQuantity,title,
                                    contacts,userID,house,street,locality,city,state,country,zip,paymentMethod,productimage

                            );

                            orderrequest.add(orderRequest_modelClass);

                        }

                        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                        linearLayoutManager.setReverseLayout(true);
                        linearLayoutManager.setStackFromEnd(true);
                        orderRequestAdapter = new OrderRequestAdapter(getActivity(),orderrequest);
                        recyclerOrderRequest.setLayoutManager(linearLayoutManager);
                        recyclerOrderRequest.setHasFixedSize(true);
                        recyclerOrderRequest.setItemAnimator(new DefaultItemAnimator());
                        recyclerOrderRequest.setAdapter(orderRequestAdapter);

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
                Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();

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
