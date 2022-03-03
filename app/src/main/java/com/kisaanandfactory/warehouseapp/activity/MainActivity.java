package com.kisaanandfactory.warehouseapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.kisaanandfactory.warehouseapp.fragment.AllUsersDetails;
import com.kisaanandfactory.warehouseapp.fragment.CreateProduct;
import com.kisaanandfactory.warehouseapp.fragment.CreateProfile;
import com.kisaanandfactory.warehouseapp.fragment.HomePage;
import com.kisaanandfactory.warehouseapp.fragment.OrderRequest;
import com.kisaanandfactory.warehouseapp.fragment.PaymentRequest;
import com.kisaanandfactory.warehouseapp.fragment.Product;
import com.kisaanandfactory.warehouseapp.fragment.Suppliers;
import com.kisaanandfactory.warehouseapp.url.AppUrl;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private DrawerLayout mydrawer;
    NavigationView navigationView;

    TextView nav_OrderRequest,nav_Logout,text_name,nav_Supplier,nav_PaymentRequest,nav_CreateProfile,nav_CreateProduct,
            nav_AllUsers,nav_MyProfile,nav_Product,nav_userAddress,nav_userName;
    ImageView image_Notification;

    private Boolean exit = false;

    public static FragmentManager fragmentManager;

    String str_Token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        mydrawer = (DrawerLayout) findViewById(R.id.mydrwaer);
        navigationView = findViewById(R.id.navigationview);

        text_name = findViewById(R.id.name);
        image_Notification = findViewById(R.id.imagenotification);
        //image_Cart = findViewById(R.id.imagecart);

        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        nav_OrderRequest = header.findViewById(R.id.nav_OrderRequest);
        nav_Logout = header.findViewById(R.id.nav_Logout);
        nav_Supplier = header.findViewById(R.id.nav_Supplier);
        nav_PaymentRequest = header.findViewById(R.id.nav_PaymentRequest);
        nav_CreateProfile = header.findViewById(R.id.nav_CreateProfile);
        nav_CreateProduct = header.findViewById(R.id.nav_CreateProduct);
        nav_AllUsers = header.findViewById(R.id.nav_AllUsers);
        nav_MyProfile = header.findViewById(R.id.nav_MyProfile);
        nav_Product = header.findViewById(R.id.nav_Product);
        nav_userAddress = header.findViewById(R.id.nav_userAddress);
        nav_userName = header.findViewById(R.id.nav_userName);

        str_Token = SharedPrefManager.getInstance(MainActivity.this).getUser().getToken();

        viewUserDetails(str_Token);

        text_name.setText("Home Page");
        mydrawer.closeDrawer(GravityCompat.START);
        image_Notification.setVisibility(View.VISIBLE);
        //image_Cart.setVisibility(View.VISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        HomePage home = new HomePage();
        ft.replace(R.id.frame, home,"HomePage");
        ft.commit();


        nav_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Show Your Another AlertDialog
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.condition_logout);
                dialog.setCancelable(false);
                Button btn_No = dialog.findViewById(R.id.no);
                TextView textView = dialog.findViewById(R.id.editText);
                Button btn_Yes = dialog.findViewById(R.id.yes);

                btn_Yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        SharedPrefManager.getInstance(MainActivity.this).logout();

                        dialog.dismiss();

                        finish();
                        //System.exit(1);

                    }
                });
                btn_No.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });

                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawableResource(R.drawable.homecard_back1);
            }
        });

        nav_Supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text_name.setText("Suppliers");
                mydrawer.closeDrawer(GravityCompat.START);
                image_Notification.setVisibility(View.VISIBLE);
                //image_Cart.setVisibility(View.VISIBLE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Suppliers suppliers = new Suppliers();
                ft.replace(R.id.frame, suppliers);
                ft.commit();
            }
        });

        nav_PaymentRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text_name.setText("Payment Request");
                mydrawer.closeDrawer(GravityCompat.START);
                image_Notification.setVisibility(View.VISIBLE);
                //image_Cart.setVisibility(View.VISIBLE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                PaymentRequest paymentRequest = new PaymentRequest();
                ft.replace(R.id.frame, paymentRequest);
                ft.commit();

            }
        });

        nav_CreateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text_name.setText("Create Product");
                mydrawer.closeDrawer(GravityCompat.START);
                image_Notification.setVisibility(View.VISIBLE);
                //image_Cart.setVisibility(View.VISIBLE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                CreateProduct createProduct = new CreateProduct();
                ft.replace(R.id.frame, createProduct);
                ft.commit();
            }
        });

        nav_AllUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text_name.setText("All Users");
                mydrawer.closeDrawer(GravityCompat.START);
                image_Notification.setVisibility(View.VISIBLE);
                //image_Cart.setVisibility(View.VISIBLE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                AllUsersDetails allUsersDetails = new AllUsersDetails();
                ft.replace(R.id.frame, allUsersDetails);
                ft.commit();
            }
        });

        nav_CreateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text_name.setText("Create Profile");
                mydrawer.closeDrawer(GravityCompat.START);
                image_Notification.setVisibility(View.VISIBLE);
                //image_Cart.setVisibility(View.VISIBLE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                CreateProfile createProfile = new CreateProfile();
                ft.replace(R.id.frame, createProfile);
                ft.commit();
            }
        });

        nav_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text_name.setText("Product Details");
                mydrawer.closeDrawer(GravityCompat.START);
                image_Notification.setVisibility(View.VISIBLE);
                //image_Cart.setVisibility(View.VISIBLE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Product product = new Product();
                ft.replace(R.id.frame, product);
                ft.commit();

            }
        });

        nav_MyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,ViewUserProfile.class);
                startActivity(intent);

            }
        });

        nav_OrderRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text_name.setText("Order Request");
                mydrawer.closeDrawer(GravityCompat.START);
                image_Notification.setVisibility(View.VISIBLE);
                //image_Cart.setVisibility(View.VISIBLE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                OrderRequest orderRequest = new OrderRequest();
                ft.replace(R.id.frame, orderRequest);
                ft.commit();

            }
        });

    }

    public void viewUserDetails(String token){

        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        TextView textView = progressDialog.findViewById(R.id.text);
        textView.setText("Retrive User Details Please wait...");
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrl.viewUserDetails, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String userData = jsonObject.getString("data");

                    JSONObject jsonObject_Data = new JSONObject(userData);

                    if(jsonObject_Data.has("location")) {

                        String name = jsonObject_Data.getString("name");
                        String email = jsonObject_Data.getString("emailID");
                        String mobile = jsonObject_Data.getString("mobile");
                        String countryCode = jsonObject_Data.getString("countryCode");
                        String password = jsonObject_Data.getString("password");
                        // String gstImage = jsonObject_Data.getString("gstImage");
                        String gstNumber = jsonObject_Data.getString("gstNumber");

                        String location = jsonObject_Data.getString("location");
                        String accountDetails = jsonObject_Data.getString("accountDetails");

                        JSONObject jsonObject_location = new JSONObject(location);

                        String address = jsonObject_location.getString("address");
                        String locality = jsonObject_location.getString("locality");
                        String city = jsonObject_location.getString("city");
                        String state = jsonObject_location.getString("state");
                        String zip = jsonObject_location.getString("zip");


                        JSONObject jsonObject_accountDetails = new JSONObject(accountDetails);

                        String accountNumber = jsonObject_accountDetails.getString("accountNumber");
                        String bankName = jsonObject_accountDetails.getString("bankName");
                        String ifscCode = jsonObject_accountDetails.getString("ifscCode");


                        nav_userName.setText(name);
                        nav_userAddress.setText(email);

                    }else{

                        String name = jsonObject_Data.getString("name");
                        String email = jsonObject_Data.getString("emailID");
                        String mobile = jsonObject_Data.getString("mobile");

                        nav_userName.setText(name);
                        nav_userAddress.setText(email);
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
                Toast.makeText(MainActivity.this, error+"", Toast.LENGTH_SHORT).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);


    }



    @Override
    public boolean onNavigationItemSelected(@NonNull  MenuItem item) {
        mydrawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void Clickmenu(View view){

        // open drawer
        openDrawer(mydrawer);
    }

    private static void openDrawer(DrawerLayout drawerLayout){

        // opendrawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        HomePage test = (HomePage) getSupportFragmentManager().findFragmentByTag("HomePage");

        if (test != null && test.isVisible()) {

            if (exit) {
                finish(); // finish activity
            } else {
                Toast.makeText(this, "Press Back again to Exit.",
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                    }
                }, 4 * 1000);
            }
        }
        else {

            text_name.setText("Home Page");
            MainActivity.fragmentManager.beginTransaction()
                    .replace(R.id.frame,new HomePage(),"HomePage").addToBackStack(null).commit();
            image_Notification.setVisibility(View.VISIBLE);
            //image_Cart.setVisibility(View.VISIBLE);

        }
    }
}