package com.example.warehouseapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import com.example.warehouseapp.R;
import com.example.warehouseapp.SharedPrefManager;
import com.example.warehouseapp.activity.ViewUserProfile;
import com.example.warehouseapp.adapter.CategoriesAdapter;
import com.example.warehouseapp.adapter.SubCategoriesAdapter;
import com.example.warehouseapp.modelclass.Category_ModelClass;
import com.example.warehouseapp.modelclass.SubCategory_ModelClass;
import com.example.warehouseapp.url.AppUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateProduct extends Fragment {

    Spinner Categories,SubCategories;
    ArrayList<Category_ModelClass> categories = new ArrayList<>();
    ArrayList<SubCategory_ModelClass> subcategories = new ArrayList<>();
    String cate_Name,cate_Id,subcate_Name,subcate_Id;

    String[] Working_city = {"--Select Working_city--", "Bhubaneswar", "Cuttack", "Puri"};

    @Nullable
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.addnewproduct,container,false);

        Categories = view.findViewById(R.id.Categories);
        SubCategories = view.findViewById(R.id.SubCategories);

        String token = SharedPrefManager.getInstance(getActivity()).getUser().getToken();


        getCategoryDetails(token);

        return view;
    }

    public void getCategoryDetails(String token){

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        TextView textView = progressDialog.findViewById(R.id.text);
        textView.setText("Categories Details Please wait...");
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrl.getCategory, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String err = jsonObject.getString("err");
                    String data = jsonObject.getString("data");

                    if(err.equals("false")){

                        String message = jsonObject.getString("msg");

                        JSONArray jsonArray_data = new JSONArray(data);

                        for (int i=0;i<jsonArray_data.length();i++){

                            JSONObject jsonObject_data = jsonArray_data.getJSONObject(i);

                            String catid = jsonObject_data.getString("_id");
                            //String CategoryUID = jsonObject_data.getString("CategoryUID");
                            String name = jsonObject_data.getString("name");
                            String productType = jsonObject_data.getString("productType");

                            Category_ModelClass category_modelClass = new Category_ModelClass(
                                    catid,"",name,productType
                            );

                            categories.add(category_modelClass);
                        }

                        CategoriesAdapter adapterCategories = new CategoriesAdapter(getActivity(),R.layout.spinneritem,categories);
                        adapterCategories.setDropDownViewResource(R.layout.spinnerdropdownitem);
                        Categories.setAdapter(adapterCategories);

                        Log.d("citylist", categories.toString());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Categories.setSelection(-1, true);

                Categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        try {

                            Category_ModelClass mystate = (Category_ModelClass) parent.getSelectedItem();

                            cate_Name = mystate.getCatname();
                            cate_Id = mystate.getCatId();

                            Log.d("R_Pincode", cate_Id);

                            GetSubCategories(cate_Id,token);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } // to close the onItemSelected

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
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
        requestQueue.add(stringRequest);
    }


    public void GetSubCategories(String cateid,String token){

        subcategories.clear();

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        TextView textView = progressDialog.findViewById(R.id.text);
        textView.setText("SubCategories Details Please wait...");
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        String url = AppUrl.getSubCategory + cateid;

        Log.d("url",url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String err = jsonObject.getString("err");
                    if(err.equals("false")){

                        String message = jsonObject.getString("msg");
                        String data = jsonObject.getString("data");

                        JSONArray jsonArray_data = new JSONArray(data);

                        for (int i=0;i<jsonArray_data.length();i++){

                            JSONObject jsonObject_data = jsonArray_data.getJSONObject(i);

                            String catid = jsonObject_data.getString("_id");
                            String CategoryId = jsonObject_data.getString("CategoryId");
                            String name = jsonObject_data.getString("name");


                            SubCategory_ModelClass subCategory_modelClass = new SubCategory_ModelClass(
                                    catid,name,CategoryId
                            );

                            subcategories.add(subCategory_modelClass);
                        }

                        SubCategoriesAdapter adapterSubCategories = new SubCategoriesAdapter(getActivity(),R.layout.spinnerdropdownitem,subcategories);
                        adapterSubCategories.setDropDownViewResource(R.layout.spinnerdropdownitem);
                        SubCategories.setAdapter(adapterSubCategories);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SubCategories.setSelection(-1, true);

               /* SubCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        try {

                            SubCategory_ModelClass mystate = (SubCategory_ModelClass) parent.getSelectedItem();

                            subcate_Name = mystate.getSubcatName();
                            subcate_Id = mystate.getSubcatId();
                            Log.d("R_Pincode", subcate_Id);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } // to close the onItemSelected

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });*/



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                error.printStackTrace();
                Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String,String> header = new HashMap<>();
                header.put("auth-token",token);
                return header;
            }};

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,3,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

}
