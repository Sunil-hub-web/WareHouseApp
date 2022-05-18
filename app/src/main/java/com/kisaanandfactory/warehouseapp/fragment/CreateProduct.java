package com.kisaanandfactory.warehouseapp.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

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
import com.google.android.material.snackbar.Snackbar;
import com.kisaanandfactory.warehouseapp.ApiResponse;
import com.kisaanandfactory.warehouseapp.ApiToJsonHandler;
import com.kisaanandfactory.warehouseapp.FileUtils;
import com.kisaanandfactory.warehouseapp.ImageResponse;
import com.kisaanandfactory.warehouseapp.R;
import com.kisaanandfactory.warehouseapp.RealPathUtil;
import com.kisaanandfactory.warehouseapp.SharedPrefManager;
import com.kisaanandfactory.warehouseapp.activity.MainActivity;
import com.kisaanandfactory.warehouseapp.activity.UserLogin;
import com.kisaanandfactory.warehouseapp.adapter.CategoriesAdapter;
import com.kisaanandfactory.warehouseapp.adapter.SubCategoriesAdapter;
import com.kisaanandfactory.warehouseapp.modelclass.Category_ModelClass;
import com.kisaanandfactory.warehouseapp.modelclass.SubCategory_ModelClass;
import com.kisaanandfactory.warehouseapp.url.AppUrl;
import com.google.gson.Gson;
import com.kisaanandfactory.warehouseapp.url.VolleyMultipartRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static android.app.Activity.RESULT_OK;

public class CreateProduct extends Fragment {

    Spinner Categories, SubCategories, ProductType, edit_VenderName;
    ArrayList<Category_ModelClass> categories = new ArrayList<>();
    ArrayList<SubCategory_ModelClass> subcategories = new ArrayList<>();
    String cate_Name, cate_Id, subcate_Name, subcate_Id;
    ImageView productimage1, productimage2, productimage3;
    String[] Product_Type = {"--Select ProductType--", "Product", "Service",};
    private static final int REQUEST_PERMISSIONS = 100;
    public static final int IMAGE_CODE = 4;
    Uri selectedImage, selectedImageUri;
    Bitmap bitmap;
    File f;
    String imagePath, token;
    TextView back_button;
    String photoSelection, photostr1 = "", photostr2 = "", photostr3 = "";
    private String userChoosenTask;
    ArrayList<String> imageArray = new ArrayList<>();

    EditText edit_ProductName, edit_ProductDescription, edit_Price, edit_Discount, edit_inStock, edit_Weight, edit_GST;
    String str_ProductName, str_ProductDescription, str_Price, str_Discount, str_inStock, str_Weight, str_type;
    Button btn_AddProduct;
    boolean photoselected = false;

    AwesomeValidation awesomeValidation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.addnewproduct, container, false);

        Categories = view.findViewById(R.id.Categories);
        SubCategories = view.findViewById(R.id.SubCategories);
        productimage1 = view.findViewById(R.id.productimage1);
        productimage2 = view.findViewById(R.id.productimage2);
        productimage3 = view.findViewById(R.id.productimage3);
        ProductType = view.findViewById(R.id.ProductType);
        back_button = view.findViewById(R.id.back_button);


        btn_AddProduct = view.findViewById(R.id.btn_AddProduct);
        edit_ProductName = view.findViewById(R.id.edit_ProductName);
        edit_ProductDescription = view.findViewById(R.id.edit_ProductDescription);
        edit_Price = view.findViewById(R.id.edit_Price);
        edit_Discount = view.findViewById(R.id.edit_Discount);
        edit_inStock = view.findViewById(R.id.edit_inStock);
        edit_Weight = view.findViewById(R.id.edit_Weight);
        edit_GST = view.findViewById(R.id.edit_GST);
        //edit_VenderName = view.findViewById(R.id.edit_VenderName);

        token = SharedPrefManager.getInstance(getActivity()).getUser().getToken();

        ArrayAdapter WorkingCityadapter = new ArrayAdapter(getContext(), R.layout.spinneritem, Product_Type);
        WorkingCityadapter.setDropDownViewResource(R.layout.spinnerdropdownitem);
        ProductType.setAdapter(WorkingCityadapter);
        ProductType.setSelection(-1, true);

        getCategoryDetails(token);

        productimage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                photoSelection = "1";
                imageupload();

            }
        });

        productimage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                photoSelection = "2";
                imageupload();

            }
        });

        productimage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                photoSelection = "3";
                imageupload();

            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        btn_AddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(edit_ProductName.getText())) {

                    edit_ProductName.setError("Fill The Details");
                    edit_ProductName.setFocusable(true);
                    edit_ProductName.requestFocus();

                } else if (!isValidUserName(edit_ProductName.getText().toString().trim())) {

                    edit_ProductName.setError("Fill The Details");
                    edit_ProductName.setFocusable(true);
                    edit_ProductName.requestFocus();

                } else if (TextUtils.isEmpty(edit_ProductDescription.getText())) {

                    edit_ProductDescription.setError("Fill The Details");
                    edit_ProductDescription.setFocusable(true);
                    edit_ProductDescription.requestFocus();

                }/*else if(!isValidUserName(edit_ProductDescription.getText().toString().trim())){

                    edit_ProductDescription.setError("Fill The Details");
                    edit_ProductDescription.setFocusable(true);
                    edit_ProductDescription.requestFocus();

                }*/ else if (TextUtils.isEmpty(edit_Price.getText())) {

                    edit_Price.setError("Fill The Details");
                    edit_Price.setFocusable(true);
                    edit_Price.requestFocus();

                } else if (!isValidMobile(edit_Price.getText().toString().trim())) {

                    edit_Price.setError("Fill The Details");
                    edit_Price.setFocusable(true);
                    edit_Price.requestFocus();

                } else if (TextUtils.isEmpty(edit_Discount.getText())) {

                    edit_Discount.setError("Fill The Details");
                    edit_Discount.setFocusable(true);
                    edit_Discount.requestFocus();

                } else if (!isValidMobile(edit_Discount.getText().toString().trim())) {

                    edit_Discount.setError("Fill The Details");
                    edit_Discount.setFocusable(true);
                    edit_Discount.requestFocus();

                } else if (TextUtils.isEmpty(edit_inStock.getText())) {

                    edit_inStock.setError("Fill The Details");
                    edit_inStock.setFocusable(true);
                    edit_inStock.requestFocus();

                } else if (!isValidMobile(edit_inStock.getText().toString().trim())) {

                    edit_inStock.setError("Fill The Details");
                    edit_inStock.setFocusable(true);
                    edit_inStock.requestFocus();

                } else if (TextUtils.isEmpty(edit_Weight.getText())) {

                    edit_Weight.setError("Fill The Details");
                    edit_Weight.setFocusable(true);
                    edit_Weight.requestFocus();

                } else if (!isValidWeightProduct(edit_Weight.getText().toString().trim())) {

                    edit_Weight.setError("Fill The Details");
                    edit_Weight.setFocusable(true);
                    edit_Weight.requestFocus();

                } else if (photostr1.equals("") || photostr2.equals("") || photostr3.equals("")) {

                    Toast.makeText(getActivity(), "Select The image", Toast.LENGTH_SHORT).show();

                } else {

                    //Toast.makeText(getActivity(), "Success fully", Toast.LENGTH_SHORT).show();

                    imageArray = new ArrayList<>();
                    if (photostr1.length() != 0) {
                        imageArray.add(photostr1);
                    }
                    if (photostr2.length() != 0) {
                        imageArray.add(photostr2);
                    }
                    if (photostr3.length() != 0) {
                        imageArray.add(photostr3);
                    }

                    str_ProductName = edit_ProductName.getText().toString().trim();
                    str_ProductDescription = edit_ProductDescription.getText().toString().trim();
                    str_Price = edit_Price.getText().toString().trim();
                    int int_Price = Integer.parseInt(str_Price);
                    str_Discount = edit_Discount.getText().toString().trim();
                    str_inStock = edit_inStock.getText().toString().trim();
                    int int_inStock = Integer.parseInt(str_inStock);
                    str_Weight = edit_Weight.getText().toString().trim();
                    str_type = ProductType.getSelectedItem().toString();

                    addProduct(str_ProductName, int_Price, str_type, str_Discount, str_ProductDescription,
                            "61704b03d2cc8624d02ebf62", int_inStock, "10", str_Weight, cate_Id, subcate_Id);
                }

            }
        });


        return view;
    }

    public void getCategoryDetails(String token) {

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

                    if (err.equals("false")) {

                        String message = jsonObject.getString("msg");

                        JSONArray jsonArray_data = new JSONArray(data);

                        for (int i = 0; i < jsonArray_data.length(); i++) {

                            JSONObject jsonObject_data = jsonArray_data.getJSONObject(i);

                            String catid = jsonObject_data.getString("_id");
                            //String CategoryUID = jsonObject_data.getString("CategoryUID");
                            String name = jsonObject_data.getString("name");
                            String productType = jsonObject_data.getString("productType");

                            Category_ModelClass category_modelClass = new Category_ModelClass(
                                    catid, "", name, productType
                            );

                            categories.add(category_modelClass);
                        }

                        CategoriesAdapter adapterCategories = new CategoriesAdapter(getActivity(), R.layout.spinneritem, categories);
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

                            Log.d("cate_Name", cate_Id + "  " + cate_Name);

                            GetSubCategories(cate_Id, token);


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
                Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> header = new HashMap<>();
                header.put("auth-token", token);
                return header;
            }
        };


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


    public void GetSubCategories(String cateid, String token) {

        subcategories.clear();

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        TextView textView = progressDialog.findViewById(R.id.text);
        textView.setText("SubCategories Details Please wait...");
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        String url = AppUrl.getSubCategory + cateid;

        Log.d("url", url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String err = jsonObject.getString("err");
                    if (err.equals("false")) {

                        String message = jsonObject.getString("msg");
                        String data = jsonObject.getString("data");

                        JSONArray jsonArray_data = new JSONArray(data);

                        for (int i = 0; i < jsonArray_data.length(); i++) {

                            JSONObject jsonObject_data = jsonArray_data.getJSONObject(i);

                            String catid = jsonObject_data.getString("_id");
                            String CategoryId = jsonObject_data.getString("CategoryId");
                            String name = jsonObject_data.getString("name");


                            SubCategory_ModelClass subCategory_modelClass = new SubCategory_ModelClass(
                                    catid, name, CategoryId
                            );

                            subcategories.add(subCategory_modelClass);
                        }

                        SubCategoriesAdapter adapterSubCategories = new SubCategoriesAdapter(getActivity(), R.layout.spinnerdropdownitem, subcategories);
                        adapterSubCategories.setDropDownViewResource(R.layout.spinnerdropdownitem);
                        SubCategories.setAdapter(adapterSubCategories);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SubCategories.setSelection(-1, true);

                SubCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        try {

                            SubCategory_ModelClass mystate = (SubCategory_ModelClass) parent.getSelectedItem();

                            subcate_Name = mystate.getSubcatName();
                            subcate_Id = mystate.getSubcatId();
                            Log.d("R_Pincode", subcate_Id + "  " + subcate_Name);

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
                Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> header = new HashMap<>();
                header.put("auth-token", token);
                return header;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

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

            selectImg();
        }
    }

    public void selectImg() {
        final CharSequence[] items = {"Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                /*if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";

                    cameraIntent();

                } else*/

                if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";

                    galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        try {
            final String[] ACCEPT_MIME_TYPES = {
                    "image/*"
            };
            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_MIME_TYPES, ACCEPT_MIME_TYPES);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_CODE);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Camera Permission error", Toast.LENGTH_SHORT).show();
            Log.d("rfgrvdcs", String.valueOf(e));
            e.printStackTrace();
        }

    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {

            if (requestCode == IMAGE_CODE && resultCode == RESULT_OK &&
                    data != null && data.getData() != null) {

                selectedImage = data.getData();

                Toast.makeText(getActivity(), ""+selectedImage.toString(), Toast.LENGTH_SHORT).show();
                //profile_image.setImageURI(imageUri);

                try {

                    InputStream imageStream = getContext().getContentResolver().openInputStream(selectedImage);

                    bitmap = BitmapFactory.decodeStream(imageStream);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    photoselected = true;

                    upload(bitmap);

                } catch (FileNotFoundException e) {

                    e.printStackTrace();
                    Log.d("plih", String.valueOf(e));

                }

               /* String path = RealPathUtil.getRealPath(getActivity(),selectedImage);
                bitmap = BitmapFactory.decodeFile(path);
                productimage1.setImageBitmap(bitmap);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                upload(path);
*/
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Please try again", Toast.LENGTH_LONG)
                    .show();
        }

    }

    private File createFile(Uri uri) {

        String path = "";
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);
        }
        if (Build.VERSION.SDK_INT < 11) {
            path = FileUtils.getRealPathFromURI_BelowAPI11(getActivity(), uri);
        } else if (Build.VERSION.SDK_INT < 19) {
            path = FileUtils.getRealPathFromURI_API11to18(getActivity(), uri);
        } else {
            path = FileUtils.getRealPathFromURI_API19(getActivity(), uri);
            Log.d("path", path);
        }

        File image = new File(path);
        return image;
    }

    public void addProduct(String title, int price, String type, String discount, String description, String soldBy,
                           int inStock, String experience, String weight, String categoryId, String subcategoryId) {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        TextView textView = progressDialog.findViewById(R.id.text);
        textView.setText("Shipped Details Please wait...");
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        JSONObject jsonObject = new JSONObject();
        JSONArray image_Array = new JSONArray();

        for (int i = 0; i < imageArray.size(); i++) {

            image_Array.put(imageArray.get(i));
        }

        Log.d("imageArray", image_Array.toString());

        try {

            jsonObject.put("title", title);
            jsonObject.put("price", price);
            jsonObject.put("type", type);
            jsonObject.put("discount", discount);
            jsonObject.put("description", description);
            jsonObject.put("soldBy", soldBy);
            jsonObject.put("inStock", inStock);
            jsonObject.put("experience", experience);
            jsonObject.put("images", image_Array);
            jsonObject.put("weight", weight);
            jsonObject.put("categoryId", categoryId);
            jsonObject.put("subcategoryId", subcategoryId);


        } catch (Exception e) {

            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppUrl.addProduct, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progressDialog.dismiss();

                try {
                    String err = response.getString("err");
                    if (err.equals("false")) {

                        String message = response.getString("msg");
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
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

    private void upload(final Bitmap bitmap) {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Upload Image Please Wait..");
        progressDialog.show();

        String path = RealPathUtil.getRealPath(getContext(),selectedImage);
        File file = new File(path);

        RequestBody imageBode = RequestBody.create(MediaType.parse(getContext().getContentResolver().getType(selectedImage)), file);
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("photo", "productimage.png", imageBode);

        Log.d("fvsdz", "" + imageBode);

        Call<ImageResponse> call = new ApiToJsonHandler().uploadImage(token, partImage);
        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, retrofit2.Response<ImageResponse> response) {

                if (response.isSuccessful()) {

                    progressDialog.dismiss();

                    Toast.makeText(getActivity(), "Upload success", Toast.LENGTH_SHORT).show();

                    // get the path and save it to images array
                    Log.d("fvsdzfvfc", "" + response.body().getMsg().getFilename());
                    Log.d("fvsdzfvfc", "" + response.body().getMsg().getPath());

                    if (photoSelection.equalsIgnoreCase("1")) {
                        productimage1.setImageBitmap(bitmap);
                        photostr1 = response.body().getMsg().getFilename();

                    } else if (photoSelection.equalsIgnoreCase("2")) {
                        productimage2.setImageBitmap(bitmap);
                        photostr2 = response.body().getMsg().getFilename();

                    } else if (photoSelection.equalsIgnoreCase("3")) {
                        productimage3.setImageBitmap(bitmap);
                        photostr3 = response.body().getMsg().getFilename();

                    }

                } else {

                    progressDialog.dismiss();
                    Gson gson = new Gson();
                    ApiResponse message = gson.fromJson(response.errorBody().charStream(), ApiResponse.class);
                    Toast.makeText(getContext(), message.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("getMessage", t.getMessage());

                if (t instanceof SocketTimeoutException) {
                    // "Connection Timeout";

                    Toast.makeText(getActivity(), "Connection Timeout", Toast.LENGTH_SHORT).show();
                } else if (t instanceof IOException) {
                    // "Timeout";

                    Toast.makeText(getActivity(), "Timeout", Toast.LENGTH_SHORT).show();
                } else {
                    //Call was cancelled by user
                    if (call.isCanceled()) {
                        //System.out.println("Call was cancelled forcefully");

                        Toast.makeText(getActivity(), "Call was cancelled forcefully", Toast.LENGTH_SHORT).show();
                    } else {
                        //Generic error handling
                        //System.out.println("Network Error :: " + error.getLocalizedMessage());

                        Toast.makeText(getActivity(), "Network Error :: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

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

        final String PASSWORD_PATTERN = "^[0-9]{1,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(mobile);

        return matcher.matches();

    }

    public boolean isValidWeightProduct(final String mobile) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^[0-9a-zA-Z]{1,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(mobile);

        return matcher.matches();

    }

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 512;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }
}
