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
import com.kisaanandfactory.warehouseapp.adapter.AllProductAdapter;
import com.kisaanandfactory.warehouseapp.modelclass.AllProduct_ModelClass;
import com.kisaanandfactory.warehouseapp.modelclass.Image_ModelClass;
import com.kisaanandfactory.warehouseapp.modelclass.Weight_ModelClass;
import com.kisaanandfactory.warehouseapp.url.AppUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Product extends Fragment {

    RecyclerView recyclerProduct;
    ArrayList<AllProduct_ModelClass> vegetable;
    ArrayList<Image_ModelClass> vegetableimage;
    ArrayList<Weight_ModelClass> vegetableweight;
    LinearLayoutManager linearLayoutManager;
    AllProductAdapter allProductAdapter;

    String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater,
                             @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.product_fragment,container,false);

        recyclerProduct = view.findViewById(R.id.recyclerProduct);

        token = SharedPrefManager.getInstance(getActivity()).getUser().getToken();

        getAllProduct(token);

        return view;
    }

    public void getAllProduct(String token){

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        TextView textView = progressDialog.findViewById(R.id.text);
        textView.setText("Get Product Details Please wait...");
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrl.getAllProduct, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String err = jsonObject.getString("err");

                    if(err.equals("false")){

                        vegetable = new ArrayList<>();

                        String products = jsonObject.getString("products");

                        JSONArray jsonArray_vegetables = new JSONArray(products);


                        for (int i = 0; i < jsonArray_vegetables.length(); i++) {

                            JSONObject jsonObject_product = jsonArray_vegetables.getJSONObject(i);

                            String discount = jsonObject_product.getString("discount");
                            String id = jsonObject_product.getString("_id");
                            String title = jsonObject_product.getString("title");
                            String price = jsonObject_product.getString("price");
                            String type = jsonObject_product.getString("type");
                            String description = jsonObject_product.getString("description");
                            String totalRating = jsonObject_product.getString("totalRating");
                            String inStock = jsonObject_product.getString("inStock");
                            String experience = jsonObject_product.getString("experience");
                            String subcategoryId = jsonObject_product.getString("subcategoryId");
                            String soldBy = jsonObject_product.getString("soldBy");
                            String createdAt = jsonObject_product.getString("createdAt");

                            vegetableimage = new ArrayList<>();

                            String images = jsonObject_product.getString("images");

                            JSONArray jsonArray_images = new JSONArray(images);

                            for (int j = 0; j < jsonArray_images.length(); j++) {

                                String image = jsonArray_images.get(j).toString();
                                Log.d("Ranj_image",image);


                                Image_ModelClass image_modelClass = new Image_ModelClass(
                                        image
                                );

                                vegetableimage.add(image_modelClass);

                            }

                            Log.d("vegetableimage",vegetableimage.toString());

                            vegetableweight = new ArrayList<>();

                            String weight = jsonObject_product.getString("weight");

                            JSONArray jsonArray_weight = new JSONArray(weight);


                            for (int k = 0; k < jsonArray_weight.length(); k++) {

                                String str_weight = jsonArray_weight.get(k).toString();


                                Weight_ModelClass weight_modelClass = new Weight_ModelClass(
                                        str_weight
                                );
                                vegetableweight.add(weight_modelClass);

                            }

                            AllProduct_ModelClass allProduct_modelClass = new AllProduct_ModelClass(
                                   discount,id,title,price,type,description,totalRating,"",inStock,experience,soldBy,vegetableimage,vegetableweight,createdAt
                            );

                            vegetable.add(allProduct_modelClass);

                        }

                        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        allProductAdapter = new AllProductAdapter(getContext(), vegetable,vegetableimage);
                        recyclerProduct.setLayoutManager(linearLayoutManager);
                        recyclerProduct.setHasFixedSize(true);
                        recyclerProduct.setAdapter(allProductAdapter);


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
