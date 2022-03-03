package com.kisaanandfactory.warehouseapp.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.kisaanandfactory.warehouseapp.R;
import com.kisaanandfactory.warehouseapp.SharedPrefManager;
import com.kisaanandfactory.warehouseapp.url.AppUrl;
import com.kisaanandfactory.warehouseapp.url.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewUserProfile extends AppCompatActivity {

    EditText edit_UserName,edit_MobileNo,edit_EmailId,edit_Password,edit_Location,edit_Locality,edit_State,
            edit_ZipCode,edit_GstNumber,edit_AccNumber,edit_BankName,edit_IfcCode,edit_City;

    String str_UserName,str_MobileNo,str_EmailId,str_Password,str_Location,str_Locality,str_State,
            str_ZipCode,str_GstNumber,str_AccNumber,str_BankName,str_IfcCode,str_City;

    TextView text_EditUserdata,edit_username,back_button;
    ImageView menuimage;
    Button btn_UpdateAddress;
    Long int_AccNumber;
    CircleImageView profile_image;
    private static final int REQUEST_PERMISSIONS = 100;
    public static final int IMAGE_CODE = 1;
    Uri imageUri,selectedImageUri;
    Bitmap bitmap;
    File f;
    String token,user_password,ImageDecode;
    AwesomeValidation awesomeValidation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_profile);


        edit_EmailId = findViewById(R.id.edit_EmailId);
        edit_UserName = findViewById(R.id.edit_UserName);
        edit_MobileNo = findViewById(R.id.edit_MobileNo);
        text_EditUserdata = findViewById(R.id.text_EditUserdata);
        menuimage = findViewById(R.id.menuimage);
        edit_username = findViewById(R.id.edit_username);
        edit_Password = findViewById(R.id.edit_Password);
        edit_Location = findViewById(R.id.edit_Location);
        edit_Locality = findViewById(R.id.edit_Locality);
        edit_State = findViewById(R.id.edit_State);
        edit_ZipCode = findViewById(R.id.edit_ZipCode);
        edit_GstNumber = findViewById(R.id.edit_GstNumber);
        edit_AccNumber = findViewById(R.id.edit_AccNumber);
        edit_BankName = findViewById(R.id.edit_BankName);
        edit_IfcCode = findViewById(R.id.edit_IfcCode);
        edit_City = findViewById(R.id.edit_City);
        btn_UpdateAddress = findViewById(R.id.btn_UpdateAddress);
        profile_image = findViewById(R.id.profile_image);
        back_button = findViewById(R.id.back_button);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(ViewUserProfile.this, R.id.edit_BankName, "^[A-Za-z\\s]{5,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.entername);
        awesomeValidation.addValidation(ViewUserProfile.this, R.id.edit_State, "^[A-Za-z\\s]{5,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.entername);
        awesomeValidation.addValidation(ViewUserProfile.this, R.id.edit_City, "^[A-Za-z\\s]{5,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.entername);
        awesomeValidation.addValidation(ViewUserProfile.this, R.id.edit_Location, "^[A-Za-z\\s]{5,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.entername);
        awesomeValidation.addValidation(ViewUserProfile.this, R.id.edit_Locality, "^[A-Za-z\\s]{5,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.entername);
        awesomeValidation.addValidation(ViewUserProfile.this, R.id.edit_AccNumber, "^[0-9]{9,18}$", R.string.enterbankaccno);
        awesomeValidation.addValidation(ViewUserProfile.this, R.id.edit_ZipCode, "^[0-9]{6}$", R.string.enterzipcode);
        awesomeValidation.addValidation(ViewUserProfile.this, R.id.edit_IfcCode, "^[A-Za-z]{4,}0[A-Za-z0-9]{6,}$", R.string.enterifccode);


       /* edit_EmailId.setFocusable(false);
        edit_EmailId.setFocusableInTouchMode(false);
        edit_UserName.setFocusable(true);
        edit_UserName.setFocusableInTouchMode(true);
        edit_MobileNo.setFocusable(false);
        edit_MobileNo.setFocusableInTouchMode(false);*/

        token = SharedPrefManager.getInstance(ViewUserProfile.this).getUser().getToken();
        user_password = SharedPrefManager.getInstance(ViewUserProfile.this).getUser().getPassword();

        viewUserDetails(token);

        menuimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ViewUserProfile.this,MainActivity.class);
                startActivity(intent);
            }
        });

        btn_UpdateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(awesomeValidation.validate()){

                    str_UserName = edit_UserName.getText().toString().trim();
                    str_MobileNo = edit_MobileNo.getText().toString().trim();
                    str_EmailId = edit_EmailId.getText().toString().trim();
                    str_Password = edit_Password.getText().toString().trim();
                    str_Location = edit_Location.getText().toString().trim();
                    str_Locality = edit_Locality.getText().toString().trim();
                    str_State = edit_State.getText().toString().trim();
                    str_ZipCode = edit_ZipCode.getText().toString().trim();
                    str_GstNumber = edit_GstNumber.getText().toString().trim();
                    str_AccNumber = edit_AccNumber.getText().toString().trim();
                    int_AccNumber = Long.valueOf(str_AccNumber);
                    str_BankName = edit_BankName.getText().toString().trim();
                    str_IfcCode = edit_IfcCode.getText().toString().trim();
                    str_City = edit_City.getText().toString().trim();

                    updateUserProfile(str_UserName,str_MobileNo,str_EmailId,91,int_AccNumber,str_IfcCode,str_BankName,"123",
                            str_GstNumber,"123",str_Location,str_Locality,str_City,str_State,str_ZipCode);

                }else{

                    Toast.makeText(ViewUserProfile.this, "Enter validate data", Toast.LENGTH_SHORT).show();

                }
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    if ((ActivityCompat.shouldShowRequestPermissionRationale(ViewUserProfile.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(ViewUserProfile.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE))) {

                    } else {
                        ActivityCompat.requestPermissions(ViewUserProfile.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS);
                    }
                } else {
                    Log.e("Else", "Else");
                    showFileChooser();
                }
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ViewUserProfile.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void viewUserDetails(String token){

        edit_UserName.setText("");
        edit_username.setText("");
        edit_MobileNo.setText("");
        edit_EmailId.setText("");
        edit_Password.setText("");
        edit_GstNumber.setText("");

        edit_Location.setText("");
        edit_Locality.setText("");
        edit_City.setText("");
        edit_State.setText("");
        edit_ZipCode.setText("");

        edit_AccNumber.setText("");
        edit_BankName.setText("");
        edit_IfcCode.setText("");

        ProgressDialog progressDialog = new ProgressDialog(ViewUserProfile.this);
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

                        String _id = jsonObject_Data.getString("_id");
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


                        edit_UserName.setText(name);
                        edit_username.setText(name);
                        edit_MobileNo.setText(mobile);
                        edit_EmailId.setText(email);
                        edit_Password.setText(password);
                        edit_GstNumber.setText(gstNumber);

                        edit_Location.setText(address);
                        edit_Locality.setText(locality);
                        edit_City.setText(city);
                        edit_State.setText(state);
                        edit_ZipCode.setText(zip);

                        edit_AccNumber.setText(accountNumber);
                        edit_BankName.setText(bankName);
                        edit_IfcCode.setText(ifscCode);
                    }else{

                        String _id = jsonObject_Data.getString("_id");
                        String name = jsonObject_Data.getString("name");
                        String email = jsonObject_Data.getString("emailID");
                        String mobile = jsonObject_Data.getString("mobile");
                        String countryCode = jsonObject_Data.getString("countryCode");
                        String password = jsonObject_Data.getString("password");

                        edit_UserName.setText(name);
                        edit_username.setText(name);
                        edit_MobileNo.setText(mobile);
                        edit_EmailId.setText(email);
                        edit_Password.setText(password);
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
                Toast.makeText(ViewUserProfile.this, error+"", Toast.LENGTH_SHORT).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(ViewUserProfile.this);
        requestQueue.add(stringRequest);


    }

    public void updateUserProfile(String name,String mobileNo,String emailId,int countryCode,Long accountNumber,
                                  String ifscCode,String bankName,String registrationNumber,String gstNumber,
                                  String gstImage,String address,String locality,String city,String state,
                                  String zip ){

        ProgressDialog progressDialog = new ProgressDialog(ViewUserProfile.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        TextView textView = progressDialog.findViewById(R.id.text);
        textView.setText("Update Address Please wait...");
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);


        Map<String,Object> params = new HashMap<>();

        params.put("name",name);
        params.put("mobile",mobileNo);
        params.put("emailID",emailId);
        params.put("countryCode",countryCode);
        params.put("accountNumber",accountNumber);
        params.put("ifscCode",ifscCode);
        params.put("bankName",bankName);
        params.put("registrationNumber",registrationNumber);
        params.put("gstNumber",gstNumber);
        params.put("gstImage",gstImage);
        params.put("address",address);
        params.put("locality",locality);
        params.put("city",city);
        params.put("state",state);
        params.put("zip",zip);

        JSONObject jsonObject1 = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppUrl.updateProfile, jsonObject1, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progressDialog.dismiss();

                try {
                    String code = response.getString("code");
                    if(code.equals("200")){
                        String message = response.getString("msg");
                        Toast.makeText(ViewUserProfile.this, message, Toast.LENGTH_SHORT).show();

                        viewUserDetails(token);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss ();

                /*error.printStackTrace();
                Log.d("Ranj_Login_error",error.toString());
                Toast.makeText (UserLoginPage.this, ""+error+"User Name password Not Match", Toast.LENGTH_LONG).show ( );*/

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(ViewUserProfile.this, "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                }else {

                    Log.d("successresponceVolley", "" + error.networkResponse);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);
//                            if (error.networkResponse.statusCode == 400) {
                            String data = jsonError.getString("msg");
                            Toast.makeText(ViewUserProfile.this, data, Toast.LENGTH_SHORT).show();

//                            } else if (error.networkResponse.statusCode == 404) {
//                                JSONArray data = jsonError.getJSONArray("msg");
//                                JSONObject jsonitemChild = data.getJSONObject(0);
//                                String ms = jsonitemChild.toString();
//                                Toast.makeText(RegisterActivity.this, ms, Toast.LENGTH_SHORT).show();
//
//                            }
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
            }};

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ViewUserProfile.this);
        requestQueue.add(jsonObjectRequest);



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ViewUserProfile.this,MainActivity.class);
        startActivity(intent);
    }

    public void showFileChooser() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "title"), IMAGE_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {

            if (requestCode == IMAGE_CODE && resultCode == RESULT_OK &&
                    data != null && data.getData() != null) {

                imageUri = data.getData();
                //profile_image.setImageURI(imageUri);

                String[] FILE = {MediaStore.Images.Media.DATA};


                Cursor cursor = getContentResolver().query(imageUri,
                        FILE, null, null, null);

                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(FILE[0]);
                ImageDecode = cursor.getString(columnIndex);
                f = new File(ImageDecode);
                selectedImageUri = Uri.fromFile(f);
                Log.d("selectedImageUri", selectedImageUri.toString());
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                profile_image.setImageBitmap(bitmap);
                //profile_image.setImageURI(selectedImageUri);

                Log.d("ImageDecode", ImageDecode);
                Log.d("ImageDecode1", f.toString());
                Log.d("ImageDecode2", selectedImageUri.toString());
                Log.d("bitmap", bitmap.toString());

                cursor.close();

                if (selectedImageUri.equals("")) {

                    Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show();

                } else {

                    profileImageUpload(bitmap);
                }


            }
        } catch (Exception e) {
            Toast.makeText(this, "Please try again", Toast.LENGTH_LONG)
                    .show();
        }


      /*  try {

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(UserDetails.this.getContentResolver(), imageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap = Bitmap.createScaledBitmap(bitmap, 500, 750, true);
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, baos); //bm is the bitmap object
            byte[] img = baos.toByteArray();
            profile_photo = Base64.encodeToString(img, Base64.DEFAULT);

        } catch (
                IOException e) {
            e.printStackTrace();
        }*/
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void profileImageUpload(final Bitmap bitmap) {

        ProgressDialog progressDialog = new ProgressDialog(ViewUserProfile.this);
        progressDialog.setMessage("Profile Pic Upload Please wait...");
        progressDialog.show();


        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, AppUrl.updateProfileImage, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {

                progressDialog.dismiss();

                try {

                    JSONObject jsonObject = new JSONObject(new String(response.data));

                    String err = jsonObject.getString("err");

                    String message = jsonObject.getString("message");

                    if (err.equals("false")) {

                        Toast.makeText(ViewUserProfile.this, "Image Upload Successfully", Toast.LENGTH_SHORT).show();

                        //viewProfileDetails();

                    } else {

                        Toast.makeText(ViewUserProfile.this, message, Toast.LENGTH_SHORT).show();

                        //viewProfileDetails();
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
                Toast.makeText(ViewUserProfile.this, "" + error, Toast.LENGTH_SHORT).show();


            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String,String> header = new HashMap<>();
                header.put("auth-token",token);
                return header;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {

                Map<String,DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("photo",new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ViewUserProfile.this);
        requestQueue.add(volleyMultipartRequest);

      /*  JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("user_id", userId);
            jsonObject.put("image", bitmap);

        } catch (Exception e) {

            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppUrl.updateProfileImage, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progressDialog.dismiss();

                try {
                    String status = response.getString("status");

                    String message = response.getString("message");

                    if (message.equals("Profile updated successfully")) {

                        Toast.makeText(UserDetails.this, message, Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(UserDetails.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(UserDetails.this, "" + error, Toast.LENGTH_SHORT).show();

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(UserDetails.this);
        requestQueue.add(jsonObjectRequest);*/

    }


}