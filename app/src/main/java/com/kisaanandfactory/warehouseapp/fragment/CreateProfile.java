package com.kisaanandfactory.warehouseapp.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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
import com.android.volley.toolbox.Volley;
import com.kisaanandfactory.warehouseapp.R;
import com.kisaanandfactory.warehouseapp.RealPathUtil;
import com.kisaanandfactory.warehouseapp.SharedPrefManager;
import com.kisaanandfactory.warehouseapp.activity.MainActivity;
import com.kisaanandfactory.warehouseapp.url.AppUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateProfile extends Fragment {

    Spinner profileFor;
    TextView button_back, text_Delivery, text_Vender;
    ImageView text_ProfileImage, text_DlPhoto, text_AddressProff;
    String[] profiletype = {"-Profile For-", "Vendor", "Delivery"};
    EditText edit_UserName, edit_MobileNo, edit_EmailId, edit_Password, edit_Location, edit_Locality, edit_State,
            edit_ZipCode, edit_PanNumber, edit_GstNumber, edit_AccNumber, edit_BankName, edit_IfcCode, edit_DrlinNumber,
            edit_Street, edit_City;
    String str_UserName, str_MobileNo, str_EmailId, str_Password, str_Location, str_Locality, str_State,
            str_ZipCode, str_PanNumber, str_GstNumber, str_AccNumber, str_BankName, str_IfcCode, str_DrlinNumber, token, str_Street, str_City;
    Button btn_AddVender;
    private static final int REQUEST_PERMISSIONS = 100;
    public static final int IMAGE_CODE = 1;
    Uri imageUri, selectedImageUri, selectedImageUri1,selectedImageUri2,selectedImageUri3;
    Bitmap bitmap, bitmap1,bitmap2;
    File f, f1;
    String user_password, profileImage = "", DlImage = "", Addressproff = "";

    ActivityResultLauncher<Intent> someActivityResultLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.createprofile_fragment, container, false);

        profileFor = view.findViewById(R.id.profileFor);
        text_ProfileImage = view.findViewById(R.id.text_ProfileImage);
        text_DlPhoto = view.findViewById(R.id.text_DlPhoto);
        text_AddressProff = view.findViewById(R.id.text_AddressProff);
        edit_UserName = view.findViewById(R.id.edit_UserName);
        edit_MobileNo = view.findViewById(R.id.edit_MobileNo);
        button_back = view.findViewById(R.id.button_back);
        // text_Delivery = view.findViewById(R.id.text_Delivery);
        edit_State = view.findViewById(R.id.edit_State);
        //text_Vender = view.findViewById(R.id.text_Vender);
        btn_AddVender = view.findViewById(R.id.btn_AddVenderDetails);
        edit_EmailId = view.findViewById(R.id.edit_EmailId);
        edit_Password = view.findViewById(R.id.edit_Password);
        edit_Location = view.findViewById(R.id.edit_Location);
        edit_Locality = view.findViewById(R.id.edit_Locality);
        edit_ZipCode = view.findViewById(R.id.edit_ZipCode);
        edit_PanNumber = view.findViewById(R.id.edit_PanNumber);
        edit_GstNumber = view.findViewById(R.id.edit_GstNumber);
        edit_AccNumber = view.findViewById(R.id.edit_AccNumber);
        edit_BankName = view.findViewById(R.id.edit_BankName);
        edit_IfcCode = view.findViewById(R.id.edit_IfcCode);
        edit_DrlinNumber = view.findViewById(R.id.edit_DrlinNumber);
        edit_Street = view.findViewById(R.id.edit_Street);
        edit_City = view.findViewById(R.id.edit_City);

        ArrayAdapter profiletypeadapter = new ArrayAdapter(getContext(), R.layout.spinneritem, profiletype);
        profiletypeadapter.setDropDownViewResource(R.layout.spinnerdropdownitem);
        profileFor.setAdapter(profiletypeadapter);
        profileFor.setSelection(-1, true);

        token = SharedPrefManager.getInstance(getActivity()).getUser().getToken();

        /*text_Vender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text_Vender.setBackgroundResource(R.drawable.button_back);
                text_Vender.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

                text_Delivery.setBackgroundResource(R.drawable.edittextback);
                text_Delivery.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));


            }
        });

        text_Delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text_Vender.setBackgroundResource(R.drawable.edittextback);
                text_Vender.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));

                text_Delivery.setBackgroundResource(R.drawable.button_back);
                text_Delivery.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            }
        });
*/
        text_ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user_password = "1";
                imageupload();
            }
        });

        text_DlPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user_password = "2";
                imageupload();
            }
        });

        text_AddressProff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user_password = "3";
                imageupload();
            }
        });

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        btn_AddVender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(edit_UserName.getText())) {

                    edit_UserName.setError("Fill The Field");
                    edit_UserName.setFocusable(true);
                    edit_UserName.requestFocus();

                } else if (!isValidUserName(edit_UserName.getText().toString().trim())) {

                    edit_UserName.setError("Fill The Field");
                    edit_UserName.setFocusable(true);
                    edit_UserName.requestFocus();

                } else if (TextUtils.isEmpty(edit_MobileNo.getText())) {

                    edit_MobileNo.setError("Fill The Field");
                    edit_MobileNo.setFocusable(true);
                    edit_MobileNo.requestFocus();

                } else if (!isValidMobile(edit_MobileNo.getText().toString().trim())) {

                    edit_MobileNo.setError("Fill The Field");
                    edit_MobileNo.setFocusable(true);
                    edit_MobileNo.requestFocus();

                } else if (TextUtils.isEmpty(edit_EmailId.getText())) {

                    edit_EmailId.setError("Fill The Field");
                    edit_EmailId.setFocusable(true);
                    edit_EmailId.requestFocus();

                } else if (!isValidEmail(edit_EmailId.getText().toString().trim())) {

                    edit_EmailId.setError("Fill The Field");
                    edit_EmailId.setFocusable(true);
                    edit_EmailId.requestFocus();

                } else if (TextUtils.isEmpty(edit_Location.getText())) {

                    edit_Location.setError("Fill The Field");
                    edit_Location.setFocusable(true);
                    edit_Location.requestFocus();

                } else if (!isValidUserName(edit_Location.getText().toString().trim())) {

                    edit_Location.setError("Fill The Field");
                    edit_Location.setFocusable(true);
                    edit_Location.requestFocus();

                } else if (TextUtils.isEmpty(edit_Locality.getText())) {

                    edit_Locality.setError("Fill The Field");
                    edit_Locality.setFocusable(true);
                    edit_Locality.requestFocus();

                } else if (!isValidUserName(edit_Locality.getText().toString().trim())) {

                    edit_Locality.setError("Fill The Field");
                    edit_Locality.setFocusable(true);
                    edit_Locality.requestFocus();

                } else if (TextUtils.isEmpty(edit_Street.getText())) {

                    edit_Street.setError("Fill The Field");
                    edit_Street.setFocusable(true);
                    edit_Street.requestFocus();

                } else if (!isValidUserName(edit_Street.getText().toString().trim())) {

                    edit_Street.setError("Fill The Field");
                    edit_Street.setFocusable(true);
                    edit_Street.requestFocus();

                } else if (TextUtils.isEmpty(edit_State.getText())) {

                    edit_State.setError("Fill The Field");
                    edit_State.setFocusable(true);
                    edit_State.requestFocus();

                } else if (!isValidUserName(edit_State.getText().toString().trim())) {

                    edit_State.setError("Fill The Field");
                    edit_State.setFocusable(true);
                    edit_State.requestFocus();

                } else if (TextUtils.isEmpty(edit_City.getText())) {

                    edit_City.setError("Fill The Field");
                    edit_City.setFocusable(true);
                    edit_City.requestFocus();

                } else if (!isValidUserName(edit_City.getText().toString().trim())) {

                    edit_City.setError("Fill The Field");
                    edit_City.setFocusable(true);
                    edit_City.requestFocus();

                } else if (edit_ZipCode.getText().toString().trim().length() == 0) {

                    edit_ZipCode.setError("enter zipcode");
                    edit_ZipCode.requestFocus();

                } else if (edit_ZipCode.getText().toString().trim().length() != 6) {

                    edit_ZipCode.setError("zipcode must be 6 digit");
                    edit_ZipCode.requestFocus();

                } else if (TextUtils.isEmpty(edit_BankName.getText())) {

                    edit_BankName.setError("enter BankName");
                    edit_BankName.requestFocus();

                } else if (!isValidUserName(edit_BankName.getText().toString().trim())) {

                    edit_BankName.setError("enter BankName");
                    edit_BankName.requestFocus();

                } else if (edit_AccNumber.getText().toString().trim().equals("")) {
                    edit_AccNumber.setError("enter account number");
                    edit_AccNumber.requestFocus();

                } else if (edit_AccNumber.getText().toString().trim().length() <= 10) {
                    edit_AccNumber.setError("enter account number");
                    edit_AccNumber.requestFocus();

                } else if (edit_IfcCode.getText().toString().trim().equals("")) {
                    edit_IfcCode.setError("enter IFSC code");
                    edit_IfcCode.requestFocus();

                } else if (edit_IfcCode.getText().toString().trim().length() <= 10) {
                    edit_IfcCode.setError("enter IFSC code");
                    edit_IfcCode.requestFocus();

                } else if (!isValidIfcCode(edit_IfcCode.getText().toString().trim())) {

                    edit_IfcCode.setError("enter IFSC code");
                    edit_IfcCode.requestFocus();

                } else if (TextUtils.isEmpty(edit_DrlinNumber.getText())) {

                    edit_IfcCode.setError("enter DL Number");
                    edit_IfcCode.requestFocus();

                } else if (profileImage.equals("") || DlImage.equals("") || Addressproff.equals("")) {

                    Toast.makeText(getActivity(), "Select Your Image", Toast.LENGTH_SHORT).show();

                } else {

                    str_UserName = edit_UserName.getText().toString().trim();
                    str_MobileNo = edit_MobileNo.getText().toString().trim();
                    long long_MobileNO = Long.valueOf(str_MobileNo);
                    str_EmailId = edit_EmailId.getText().toString().trim();
                    str_Password = edit_Password.getText().toString().trim();
                    str_Location = edit_Location.getText().toString().trim();
                    str_Locality = edit_Locality.getText().toString().trim();
                    str_State = edit_State.getText().toString().trim();
                    str_ZipCode = edit_ZipCode.getText().toString().trim();
                    long long_zip = Long.valueOf(str_ZipCode);
                    str_PanNumber = edit_PanNumber.getText().toString().trim();
                    str_GstNumber = edit_GstNumber.getText().toString().trim();
                    str_AccNumber = edit_AccNumber.getText().toString().trim();
                    long long_AccNumber = Long.valueOf(str_AccNumber);
                    str_BankName = edit_BankName.getText().toString().trim();
                    str_IfcCode = edit_IfcCode.getText().toString().trim();
                    str_DrlinNumber = edit_DrlinNumber.getText().toString().trim();
                    str_City = edit_City.getText().toString().trim();
                    str_Street = edit_Street.getText().toString().trim();

                    String profilefor1 = profileFor.getSelectedItem().toString();

                    if (profilefor1.equals("Vendor")) {

                        addVender(str_UserName, long_MobileNO, str_EmailId, str_Password, 91, str_Location, str_Street, long_AccNumber,
                                str_IfcCode, str_BankName, str_PanNumber, str_GstNumber, str_Locality, str_City, str_State, long_zip);

                    } else {

                        addDelivery(str_UserName, long_MobileNO, str_EmailId, str_Password, 91, "123456789", str_Street, str_AccNumber,
                                str_IfcCode, str_BankName, str_PanNumber, "driv1234", str_Locality, str_City, str_State, long_zip, str_DrlinNumber);

                    }

                }

            }
        });

        // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();

                            try {

                                if(data != null){

                                    imageUri = data.getData();

                                    if (user_password.equals("1")) {


                                        //profile_image.setImageURI(imageUri);
                                        String path = RealPathUtil.getRealPath(getContext(),imageUri);
                                        File file = new File(path);
                                        selectedImageUri1 = Uri.fromFile(file);
                                        profileImage = selectedImageUri1.toString();

                                        InputStream imageStream = getContext().getContentResolver().openInputStream(selectedImageUri1);
                                        bitmap = BitmapFactory.decodeStream(imageStream);
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                                        text_ProfileImage.setImageBitmap(bitmap);


                                    }
                                    else if (user_password.equals("2")) {

                                        String path = RealPathUtil.getRealPath(getContext(),imageUri);
                                        File file = new File(path);
                                        selectedImageUri2 = Uri.fromFile(file);
                                        DlImage = selectedImageUri2.toString();

                                        InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                                        bitmap1 = BitmapFactory.decodeStream(imageStream);
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        bitmap1.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                                        text_DlPhoto.setImageBitmap(bitmap1);


                                    }
                                    else if (user_password.equals("3")) {

                                        //imageUri = data.getData();
                                        //profile_image.setImageURI(imageUri);

                                        String path = RealPathUtil.getRealPath(getContext(),imageUri);
                                        File file = new File(path);
                                        selectedImageUri3 = Uri.fromFile(file);
                                        Addressproff = selectedImageUri3.toString();

                                        InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                                        bitmap2 = BitmapFactory.decodeStream(imageStream);
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        bitmap2.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                                        text_AddressProff.setImageBitmap(bitmap2);

                                    }

                                }else {

                                    Toast.makeText(getActivity(), "Image Not SeleCted", Toast.LENGTH_SHORT).show();
                                }


                                Log.d("ImageDecode1", selectedImageUri1.toString());
                                Log.d("ImageDecode2", selectedImageUri2.toString());
                                Log.d("ImageDecode3", selectedImageUri3.toString());


                            } catch (Exception e) {

                                //Toast.makeText(getActivity(), "Please try again" + e, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });


        return view;
    }

    public void addVender(String name, long mobile, String emailID, String password, int countryCode,
                          String address, String street, long accountNum, String ifsc, String bankName,
                          String panNumber, String gstNumber, String locality, String city, String state,
                          long zip) {


        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        TextView textView = progressDialog.findViewById(R.id.text);
        textView.setText("Add details Please wait...");
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("emailID", emailID);
            jsonObject.put("name", name);
            jsonObject.put("mobile", mobile);
            jsonObject.put("password", password);
            jsonObject.put("address", address);
            jsonObject.put("locality", locality);
            jsonObject.put("state", state);
            jsonObject.put("city", city);
            jsonObject.put("zip", zip);
            jsonObject.put("panNumber", panNumber);
            jsonObject.put("gstNumber", gstNumber);
            jsonObject.put("accountNum", accountNum);
            jsonObject.put("bankName", bankName);
            jsonObject.put("ifscCode", ifsc);

        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppUrl.addVender, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progressDialog.dismiss();

                try {
                    String err = response.getString("err");

                    if (err.equals("false")) {

                        String message = response.getString("msg");
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                /*error.printStackTrace();
                Log.d("Ranj_Login_error",error.toString());
                Toast.makeText (UserLoginPage.this, ""+error+"User Name password Not Match", Toast.LENGTH_LONG).show ( );*/

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(getActivity(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);
//                            if (error.networkResponse.statusCode == 400) {
                            String data = jsonError.getString("msg");
                            Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();

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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> header = new HashMap<>();
                header.put("auth-token", token);
                return header;
            }

        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);

    }

    public void addDelivery(String name, Long mobile, String emailID, String password, int countryCode,
                            String regNumber, String street, String accountNum, String ifsc, String bankName,
                            String panNumber, String driverid, String locality, String city, String state,
                            long zip, String drivingLisence) {


        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        TextView textView = progressDialog.findViewById(R.id.text);
        textView.setText("Add details Please wait...");
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("emailID", emailID);
            jsonObject.put("name", name);
            jsonObject.put("mobile", mobile);
            jsonObject.put("password", password);
            jsonObject.put("locality", locality);
            jsonObject.put("state", state);
            jsonObject.put("city", city);
            jsonObject.put("zip", zip);
            jsonObject.put("accountNum", accountNum);
            jsonObject.put("bankName", bankName);
            jsonObject.put("ifscCode", ifsc);
            jsonObject.put("driver_id", driverid);
            jsonObject.put("countryCode", countryCode);
            jsonObject.put("registrationNumber", regNumber);
            jsonObject.put("drivingLisence", drivingLisence);
            jsonObject.put("profilePhoto", profileImage);
            jsonObject.put("DlPhoto", DlImage);
            jsonObject.put("AddressProof", Addressproff);

        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppUrl.addDelivery, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("ghsvj",response.toString());

                progressDialog.dismiss();

                try {
                    String err = response.getString("err");

                    if (err.equals("false")) {

                        String message = response.getString("msg");
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }else{

                        String message = response.getString("msg");
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                /*error.printStackTrace();
                Log.d("Ranj_Login_error",error.toString());
                Toast.makeText (UserLoginPage.this, ""+error+"User Name password Not Match", Toast.LENGTH_LONG).show ( );*/

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(getActivity(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);
//                            if (error.networkResponse.statusCode == 400) {
                            String data = jsonError.getString("msg");
                            Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();

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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> header = new HashMap<>();
                header.put("auth-token", token);
                return header;
            }

        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);

    }

    public void imageupload() {

        if ((ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE))) {

            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        } else {
            Log.e("Else", "Else");

            showFileChooser();
        }
    }

    public void showFileChooser() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        someActivityResultLauncher.launch(Intent.createChooser(intent, "title"));
    }


    public boolean isValidUserName(final String userName) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^[A-Za-z\\s]{5,}[\\.]{0,1}[A-Za-z\\s]{0,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(userName);

        return matcher.matches();

    }

    public boolean isValidMobile(final String mobile) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^[0-9]{10}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(mobile);

        return matcher.matches();

    }

    public boolean isValidEmail(final String email) {

        Pattern pattern;
        Matcher matcher;

        pattern = Patterns.EMAIL_ADDRESS;
        matcher = pattern.matcher(email);

        return matcher.matches();

    }

    public boolean isValidIfcCode(final String ifsccode) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^[a-zA-Z]{4,}0[A-Z0-9]{6,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(ifsccode);

        return matcher.matches();

    }
}
