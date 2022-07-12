package com.kisaanandfactory.warehouseapp.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kisaanandfactory.warehouseapp.R;
import com.kisaanandfactory.warehouseapp.SessionManager;
import com.kisaanandfactory.warehouseapp.SharedPrefManager;
import com.kisaanandfactory.warehouseapp.modelclass.DeliveryBoy_ModelClass;
import com.kisaanandfactory.warehouseapp.modelclass.Image_ModelClass;
import com.kisaanandfactory.warehouseapp.modelclass.OrderRequest_ModelClass;
import com.kisaanandfactory.warehouseapp.url.AppUrl;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderRequestAdapter extends RecyclerView.Adapter<OrderRequestAdapter.ViewHolder> {

    Context context;
    ArrayList<OrderRequest_ModelClass> orderRequest;
    int index;
    String token;
    SessionManager sessionManager;
    ArrayList<DeliveryBoy_ModelClass> delivery_agent = new ArrayList<>();
    ArrayAdapter deliveryAgentAdapter;

    public OrderRequestAdapter(FragmentActivity activity, ArrayList<OrderRequest_ModelClass> orderrequest) {

        this.context = activity;
        this.orderRequest = orderrequest;
    }

    @NonNull
    @Override
    public OrderRequestAdapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderrequest,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  OrderRequestAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        sessionManager = new SessionManager(context);

        token = SharedPrefManager.getInstance(context).getUser().getToken();

        OrderRequest_ModelClass order_request = orderRequest.get(position);

        holder.productName.setText(order_request.getTitle());
        holder.producr_qty.setText(order_request.getProductQuantity());
        holder.total_price.setText(order_request.getTotalAmount());
        holder.stauesName.setText(order_request.getStatus());
        holder.text_weight.setText(order_request.getStr_weight());

        ArrayList<Image_ModelClass> image_modelClass = order_request.getImage_modelClasses();
        String image = "https://kisaanandfactory.com/static_file/"+image_modelClass.get(0);
        Log.d("ranj_adapter_image",image);
        Log.d("ranj_adapter_image",image_modelClass.get(0)+"");
        Picasso.with(context).load(image).into(holder.productImage);

        holder.customerName.setText(order_request.getCoustomerName());
        holder.phoneNo.setText(order_request.getContacts());
        holder.paymentStatues.setText(order_request.getPaymentMethod());
        holder.totalAmount.setText(order_request.getTotalAmount());
        holder.deliveryAddress.setText(order_request.getHouse()+","+order_request.getStreet()+","+order_request.getLocality()
                +","+order_request.getCity()+","+order_request.getState()+","+order_request.getCountry()+","+order_request.getZip());

        if(order_request.getStatus().equals("delivered")){

            holder.btn_Accept.setVisibility(View.GONE);
            holder.btn_Reject.setText("Delete");

        }else  if(order_request.getStatus().equals("shipped")){

            holder.btn_Reject.setVisibility(View.GONE);
            holder.btn_Accept.setText("Out For Delivery");

        }else  if(order_request.getStatus().equals("confirmed")){

            holder.btn_Accept.setVisibility(View.GONE);
            holder.btn_Reject.setText("Delete");

        }else  if(order_request.getStatus().equals("ordered")){

            holder.btn_Accept.setVisibility(View.VISIBLE);
            holder.btn_Reject.setVisibility(View.VISIBLE);

            holder.btn_Accept.setText("Accept");
            holder.btn_Reject.setText("Reject");

        }else  if(order_request.getStatus().equals("cancel")){

            holder.btn_Accept.setVisibility(View.VISIBLE);
            holder.btn_Reject.setVisibility(View.VISIBLE);

            holder.btn_Accept.setText("Accept");
            holder.btn_Reject.setText("Reject");

        }else  if(order_request.getStatus().equals("return")){

            holder.btn_Accept.setVisibility(View.GONE);
            holder.btn_Reject.setText("Delete");

        }else  if(order_request.getStatus().equals("out for delivery")){

            holder.btn_Accept.setVisibility(View.GONE);
            holder.btn_Reject.setText("Delete");

        }


        boolean isExpand = order_request.isExpanded();
        holder.expandableLayout.setVisibility(isExpand ? View.VISIBLE : View.GONE);

       /* holder.rel_orderdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                index = position;
                notifyDataSetChanged();
            }
        });*/

       /* index = position;

        if(index == position){

            holder.expandableLayout.setVisibility(View.VISIBLE);

        }else{

            holder.expandableLayout.setVisibility(View.GONE);
        }*/

        String utcDateString = order_request.getOrderDate();

       /* try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                Date date = dateFormat.parse(utcDateString);//You will get date object relative to server/client timezone wherever it is parsed
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); //If you need time just put specific format for time like 'HH:mm:ss'
                DateFormat formatter1 = new SimpleDateFormat("HH:mm"); //If you need time just put specific format for time like 'HH:mm:ss'
                String dateStr = formatter.format(date);
                String dateStr1 = formatter1.format(date);

                Log.d("dateStr", dateStr + "  "+dateStr1);

               // holder.datetime.setText(dateStr+"("+dateStr1+")");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }*/

        holder.btn_Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String orderId = order_request.getOrderId();

                if(holder.btn_Accept.getText().toString().trim().equals("Accept")){

                    orderStatuesChange(orderId);

                    holder.btn_Accept.setVisibility(View.GONE);
                    holder.btn_Reject.setText("Delete");

                }else if(holder.btn_Accept.getText().toString().trim().equals("Out For Delivery")){

                    holder.delivery_Agent.setVisibility(View.VISIBLE);

                    String zipcode = sessionManager.getZipCode();

                    getvenderdetails(zipcode);

                    holder.delivery_Agent.setAdapter(deliveryAgentAdapter);
                    holder.delivery_Agent.setSelection(-1, true);

                }
            }
        });

        holder.btn_Reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String orderId = order_request.getOrderId();

            }
        });

       holder.delivery_Agent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               DeliveryBoy_ModelClass cate_data = (DeliveryBoy_ModelClass) parent.getSelectedItem();

               String delivery_id = cate_data.getId();
               String delivery_name = cate_data.getName();
               Log.d("city_Id", delivery_id);

               String orderId = order_request.getOrderId();
               String wareHouseID = sessionManager.getUserId();

               assignDelivery(orderId,wareHouseID,delivery_id);

           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

    }

    @Override
    public int getItemCount() {

        return orderRequest.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productName,producr_qty,total_price,stauesName,customerName,phoneNo,deliveryAddress,
                paymentStatues,totalAmount,text_weight;
        ImageView productImage;
        RelativeLayout rel_orderdetails;
        LinearLayout expandableLayout;
        Button btn_Accept,btn_Reject;
        Spinner delivery_Agent;

        public ViewHolder(@NonNull  View itemView) {
            super(itemView);

            totalAmount = itemView.findViewById(R.id.totalAmount);
            productName = itemView.findViewById(R.id.productName);
            producr_qty = itemView.findViewById(R.id.producr_qty);
            total_price = itemView.findViewById(R.id.total_price);
            stauesName = itemView.findViewById(R.id.stauesName);
            customerName = itemView.findViewById(R.id.customerName);
            phoneNo = itemView.findViewById(R.id.phoneNo);
            deliveryAddress = itemView.findViewById(R.id.deliveryAddress);
            paymentStatues = itemView.findViewById(R.id.paymentStatues);
            productImage = itemView.findViewById(R.id.productImage);
            rel_orderdetails = itemView.findViewById(R.id.rel_orderdetails);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            text_weight = itemView.findViewById(R.id.text_weight);
            btn_Reject = itemView.findViewById(R.id.btn_Reject);
            btn_Accept = itemView.findViewById(R.id.btn_Accept);
            delivery_Agent = itemView.findViewById(R.id.delivery_Agent);


            rel_orderdetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    OrderRequest_ModelClass order_request = orderRequest.get(getAdapterPosition());
                    order_request.setExpanded(!order_request.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }

    }

    public void orderStatuesChange(String orderId){

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        TextView textView = progressDialog.findViewById(R.id.text);
        textView.setText("Accept Order Please wait...");
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        //String url_satus = AppUrl.acceptOrder+orderId;

        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("orderId",orderId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, AppUrl.acceptOrder, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progressDialog.dismiss();

                try {
                    String code = response.getString("code");
                    String err = response.getString("err");
                    String OrderStatus = response.getString("OrderStatus");

                    if(code.equals("200")){

                        Toast.makeText(context, OrderStatus, Toast.LENGTH_SHORT).show();

                    }else{

                        Toast.makeText(context, OrderStatus, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(context, "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);

                            String data = jsonError.getString("msg");
                            Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("successresponceVolley", "" + e);
                        }


                    }

                }

            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String,String> header = new HashMap<>();
                header.put("auth-token",token);
                return header;
            }

        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,3,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);

    }

    public void getvenderdetails(String zipcode){

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        TextView textView = progressDialog.findViewById(R.id.text);
        textView.setText("Retrive Delivery Name Please wait...");
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        String delivery = AppUrl.get_driveragent+zipcode;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, delivery, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String err = jsonObject.getString("err");
                    String msg = jsonObject.getString("msg");
                    String data = jsonObject.getString("data");

                    if(code.equalsIgnoreCase("200")){

                        delivery_agent = new ArrayList<>();
                        JSONArray jsonArray_data = new JSONArray(data);

                        for (int i=0;i<jsonArray_data.length();i++){

                            JSONObject jsonObject_data = jsonArray_data.getJSONObject(i);

                            String location = jsonObject_data.getString("location");
                            String accountDetails = jsonObject_data.getString("accountDetails");
                            String fcm_id = jsonObject_data.getString("fcm_id");
                            String availabilityStatus = jsonObject_data.getString("availabilityStatus");
                            String _id = jsonObject_data.getString("_id");
                            String name = jsonObject_data.getString("name");

                            sessionManager.setUserId_Delivery(_id);

                            DeliveryBoy_ModelClass deliveryBoy_modelClass = new DeliveryBoy_ModelClass(
                                    name,_id
                            );

                            delivery_agent.add(deliveryBoy_modelClass);
                        }

                        deliveryAgentAdapter = new ArrayAdapter(context, R.layout.spinneritem, delivery_agent);
                        deliveryAgentAdapter.setDropDownViewResource(R.layout.spinnerdropdownitem);

                        Log.d("hdudgib",delivery_agent.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(context, "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);

                            String data = jsonError.getString("msg");
                            Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("successresponceVolley", "" + e);
                        }


                    }

                }

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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    public void assignDelivery(String orderid,String wareHouse_Id,String Delivery_Id){

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        TextView textView = progressDialog.findViewById(R.id.text);
        textView.setText("Assign Delivery Please wait...");
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        String url = AppUrl.out_for_delivery+orderid;

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("warehouse_id",wareHouse_Id);
            jsonObject.put("driver_id",Delivery_Id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progressDialog.dismiss();

                Toast.makeText(context, "Delivery Boy Assign SuccessFully", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(context, "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);

                            String data = jsonError.getString("msg");
                            Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("successresponceVolley", "" + e);
                        }


                    }

                }

            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String,String> header = new HashMap<>();
                header.put("auth-token",token);
                return header;
            }

        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,3,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }
}
