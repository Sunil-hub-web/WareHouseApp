package com.example.warehouseapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.warehouseapp.R;
import com.example.warehouseapp.SharedPrefManager;
import com.example.warehouseapp.fragment.AllUsersDetails;
import com.example.warehouseapp.fragment.CreateProduct;
import com.example.warehouseapp.fragment.CreateProfile;
import com.example.warehouseapp.fragment.HomePage;
import com.example.warehouseapp.fragment.OrderRequest;
import com.example.warehouseapp.fragment.PaymentRequest;
import com.example.warehouseapp.fragment.Product;
import com.example.warehouseapp.fragment.Suppliers;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private DrawerLayout mydrawer;
    NavigationView navigationView;

    TextView nav_OrderRequest,nav_Logout,text_name,nav_Supplier,nav_PaymentRequest,nav_CreateProfile,nav_CreateProduct,
            nav_AllUsers,nav_MyProfile,nav_Product;
    ImageView image_Notification, image_Cart;

    private Boolean exit = false;

    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        mydrawer = (DrawerLayout) findViewById(R.id.mydrwaer);
        navigationView = findViewById(R.id.navigationview);

        text_name = findViewById(R.id.name);
        image_Notification = findViewById(R.id.imagenotification);
        image_Cart = findViewById(R.id.imagecart);

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

        text_name.setText("Home Page");
        mydrawer.closeDrawer(GravityCompat.START);
        image_Notification.setVisibility(View.VISIBLE);
        image_Cart.setVisibility(View.VISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        HomePage home = new HomePage();
        ft.replace(R.id.frame, home,"HomePage");
        ft.commit();


        nav_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPrefManager.getInstance(MainActivity.this).logout();
            }
        });

        nav_Supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text_name.setText("Suppliers");
                mydrawer.closeDrawer(GravityCompat.START);
                image_Notification.setVisibility(View.VISIBLE);
                image_Cart.setVisibility(View.VISIBLE);
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
                image_Cart.setVisibility(View.VISIBLE);
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
                image_Cart.setVisibility(View.VISIBLE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                CreateProduct createProduct = new CreateProduct();
                ft.replace(R.id.frame, createProduct);
                ft.commit();
            }
        });

        nav_AllUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //text_name.setText("Create Product");
                mydrawer.closeDrawer(GravityCompat.START);
                image_Notification.setVisibility(View.VISIBLE);
                image_Cart.setVisibility(View.VISIBLE);
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
                image_Cart.setVisibility(View.VISIBLE);
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
                image_Cart.setVisibility(View.VISIBLE);
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
                image_Cart.setVisibility(View.VISIBLE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                OrderRequest orderRequest = new OrderRequest();
                ft.replace(R.id.frame, orderRequest);
                ft.commit();

            }
        });

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
            image_Cart.setVisibility(View.VISIBLE);

        }
    }
}