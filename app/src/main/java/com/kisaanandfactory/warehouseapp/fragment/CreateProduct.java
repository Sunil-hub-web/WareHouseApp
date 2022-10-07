package com.kisaanandfactory.warehouseapp.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import androidx.appcompat.app.AlertDialog;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.kisaanandfactory.warehouseapp.ApiResponse;
import com.kisaanandfactory.warehouseapp.ApiToJsonHandler;
import com.kisaanandfactory.warehouseapp.FileUtils;
import com.kisaanandfactory.warehouseapp.ImageResponse;
import com.kisaanandfactory.warehouseapp.R;
import com.kisaanandfactory.warehouseapp.RealPathUtil;
import com.kisaanandfactory.warehouseapp.SessionManager;
import com.kisaanandfactory.warehouseapp.SharedPrefManager;
import com.kisaanandfactory.warehouseapp.url.AppUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class CreateProduct extends Fragment {

    private final int PICK_IMAGE_CAMERA = 3, PICK_IMAGE_GALLERY = 4;
    private final File destination = null;
    private final String imgPath = null;
    TextView addnewproductbtn, priceClc;
    EditText productname, stock, weight, discount, retailprice, gst, description, servicecharges,
            commission, totalpayment, dimention, color, quentity;
    Spinner categories_spinner, producttype_spinner, prodty_spinner, prodty1_spinner, supercategory,
            subcategory_spinner;
    String catname = "", photostr1 = "", photostr2 = "", photostr3 = "", photoselection = "",
            catid = "", subcatcatname = "", subcatcatid = "", typename = "", typeid = "", ttl = "",
            des = "", sold = "", super_category = "", supercat = "", product_type = "", produ_Type = "",
            token = "", photoSelection = "",charge = "";
    HashMap<String, String> hashCategories = new HashMap<String, String>();
    ArrayList<String> CategoriesArray = new ArrayList<>();
    HashMap<String, String> hashProducttype = new HashMap<String, String>();
    ArrayList<String> ProducttypeArray = new ArrayList<>();
    ArrayList<String> typeArray = new ArrayList<>();
    ImageView productimage1, productimage2, productimage3;
    ArrayList<String> typeArray1;
    Map<String, String> type_Array;
    ArrayList<String> ImageArray = new ArrayList<>();
    Uri photouri;
    boolean photoselected = false;
    Uri selectedImage;
    int prc = 0, stok = 0, exp = 0, wt = 0, disc = 0;
    float pricetot;

    private static final int REQUEST_PERMISSIONS = 100;
    public static final int IMAGE_CODE = 4;
    Boolean bool_productType = false;
    ArrayList<String> superCategoryList;
    HashMap<String, String> super_CategoryList;
    ArrayList<String> categoryList;
    HashMap<String, String> category_List;
    ArrayList<String> subcCategoryList = new ArrayList<>();
    HashMap<String, String> subCategory_List = new HashMap<>();
    JSONObject jsonObject_metadata;
    SessionManager sessionManager;
    private String userChoosenTask;

    ActivityResultLauncher activityResultLauncher;
    private Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.addnewproduct, container, false);

        InIt(view);
        sessionManager = new SessionManager(getActivity());

        token = SharedPrefManager.getInstance(getActivity()).getUser().getToken();

        getsuperCatecory();

        typeArray = new ArrayList<>();
        typeArray.add("Select Type");
        typeArray.add("Goods");
        typeArray.add("Services");

        ArrayAdapter<String> typVehicle = new ArrayAdapter<String>(getActivity(),
                R.layout.spinneritem, typeArray);
        typVehicle.setDropDownViewResource(R.layout.spinnerdropdownitem);
        prodty_spinner.setAdapter(typVehicle);

        typeArray1 = new ArrayList<>();
        typeArray1.add("Select Product Type");
        typeArray1.add("Refundable");
        typeArray1.add("Non-Refundable");

        type_Array = new HashMap<>();
        type_Array.put("Refundable", "true");
        type_Array.put("Non-Refundable", "false");

        ArrayAdapter<String> typVehicle1 = new ArrayAdapter<String>(getActivity(),
                R.layout.spinneritem, typeArray1);
        typVehicle1.setDropDownViewResource(R.layout.spinnerdropdownitem);
        prodty1_spinner.setAdapter(typVehicle1);

        categories_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                catname = categories_spinner.getItemAtPosition(categories_spinner.getSelectedItemPosition()).toString();
                if (catname.equalsIgnoreCase("Select Category")) {

                } else {

                    //subcatcatname = "";
                    //subcatcatid = "";
                    catid = category_List.get(catname);

                    //GetProductType();

                    getSubCategory(catid);
                    extraCharge(catid);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
                Toast.makeText(getActivity().getApplicationContext(), "Select Category", Toast.LENGTH_SHORT).show();
            }
        });

        subcategory_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subcatcatname = subcategory_spinner.getItemAtPosition(subcategory_spinner.getSelectedItemPosition()).toString();
                if (subcatcatname.equalsIgnoreCase("select SubCategory")) {

                    subcatcatid = "";

                } else {

                    subcatcatid = subCategory_List.get(subcatcatname);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
                Toast.makeText(getActivity().getApplicationContext(), "Select Type", Toast.LENGTH_SHORT).show();
            }
        });

        prodty_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                typename = prodty_spinner.getItemAtPosition(prodty_spinner.getSelectedItemPosition()).toString();
                if (typename.equalsIgnoreCase("Select Type")) {

                    typeid = "";

                } else {

                    if (typename.equalsIgnoreCase("Goods")) {
                        typeid = "product";
                    } else {
                        typeid = "service";
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
                Toast.makeText(getActivity().getApplicationContext(), "Select Type", Toast.LENGTH_SHORT).show();
            }
        });

        prodty1_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                product_type = prodty1_spinner.getItemAtPosition(prodty1_spinner.getSelectedItemPosition()).toString();
                if (product_type.equalsIgnoreCase("Select Product Type")) {

                    //Toast.makeText(AddNewProduct.this, "Select Product Type", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("hshkjbsan", product_type);

                    bool_productType = product_type.equalsIgnoreCase("Refundable");

                    //bool_productType = Boolean.parseBoolean(produ_Type);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                Toast.makeText(getActivity().getApplicationContext(), "Select Type", Toast.LENGTH_SHORT).show();
            }
        });

        supercategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                super_category = supercategory.getItemAtPosition(supercategory.getSelectedItemPosition()).toString();

                if (super_category.equalsIgnoreCase("Select SuperCategory")) {

                    supercat = "";

                } else {

                    supercat = super_CategoryList.get(super_category);

                    getCategory(supercat);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

        priceClc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (retailprice.getText().toString().trim().length() == 0) {
                    retailprice.setError("enter product retail price");
                    retailprice.requestFocus();

                } else if (discount.getText().toString().trim().length() == 0) {
                    discount.setError("enter discount price");
                    discount.requestFocus();

                } else {

                    String str_gst = gst.getText().toString().trim();
                    String str_servicecharges = servicecharges.getText().toString().trim();
                    String str_commission = commission.getText().toString().trim();
                    String str_VenderPrice = retailprice.getText().toString().trim();
                    String str_Discount = discount.getText().toString().trim();

                    int int_gst = Integer.valueOf(str_gst);
                    int int_servicecharges = Integer.valueOf(str_servicecharges);
                    int int_commission = Integer.valueOf(str_commission);
                    int int_VenderPrice = Integer.valueOf(str_VenderPrice);
                    int int_Discount = Integer.valueOf(str_Discount);

                    float disc = int_VenderPrice * int_Discount / 100;
                    float tot_pric = int_VenderPrice - disc;
                    float gst = tot_pric * 18 / 100;
                    float commi = tot_pric * 5 / 100;
                    float coservic = tot_pric * 1 / 100;
                    pricetot = tot_pric + gst + commi + coservic;

                    DecimalFormat df = new DecimalFormat("#.##");
                    String pricetot1 = df.format(pricetot);

                    Log.d("ghghgh", pricetot1);

                    totalpayment.setText(pricetot1);

                    // priceClculator(int_VenderPrice,int_Discount,0,int_commission,int_servicecharges,int_gst);


                }

            }
        });

        addnewproductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageArray = new ArrayList<>();
                if (photostr1.length() != 0) {
                    ImageArray.add(photostr1);
                }
                if (photostr2.length() != 0) {
                    ImageArray.add(photostr2);
                }
                if (photostr3.length() != 0) {
                    ImageArray.add(photostr3);
                }

                if (productname.getText().toString().trim().length() == 0) {
                    productname.setError("enter product name");
                    productname.requestFocus();

                } else if (typeid.trim().length() == 0) {
                    Toast.makeText(getActivity().getApplicationContext(), "select product type", Toast.LENGTH_SHORT).show();

                } else if (catid.trim().length() == 0) {
                    Toast.makeText(getActivity().getApplicationContext(), "select category", Toast.LENGTH_SHORT).show();

                } else if (subcatcatid.trim().length() == 0) {
                    Toast.makeText(getActivity().getApplicationContext(), "select sub-category", Toast.LENGTH_SHORT).show();

                } else if (stock.getText().toString().trim().length() == 0) {
                    stock.setError("enter stock");
                    stock.requestFocus();

                } else if (weight.getText().toString().trim().length() == 0) {
                    weight.setError("enter product weight");
                    weight.requestFocus();

                } else if (discount.getText().toString().trim().length() == 0) {
                    discount.setError("enter product discount");
                    discount.requestFocus();

                } else if (retailprice.getText().toString().trim().length() == 0) {
                    retailprice.setError("enter product retail price");
                    retailprice.requestFocus();

                } else if (product_type.trim().length() == 0) {

                    Toast.makeText(getActivity(), "Select Product Type", Toast.LENGTH_SHORT).show();

                } else if (totalpayment.getText().toString().trim().length() == 0) {

                    Toast.makeText(getActivity(), "Please select total price", Toast.LENGTH_SHORT).show();

                } else {

                    ttl = productname.getText().toString();
                    prc = Integer.parseInt(retailprice.getText().toString());
                    disc = Integer.parseInt(discount.getText().toString());
                    des = description.getText().toString();
                    sold = sessionManager.getUserId();
                    stok = Integer.parseInt(stock.getText().toString());
                    exp = 0;
                    wt = Integer.parseInt(weight.getText().toString());

                    String str_quentity = quentity.getText().toString().trim();
                    int int_quentity = Integer.parseInt(str_quentity);
                    String str_dimention = dimention.getText().toString().trim();
                    String str_color = color.getText().toString().trim();

                    jsonObject_metadata = new JSONObject();
                    try {
                        jsonObject_metadata.put("color", str_color);
                        jsonObject_metadata.put("dimension", str_dimention);
                        jsonObject_metadata.put("quantity", int_quentity);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    addProduct();
                }
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                    Intent intent = result.getData();

                    if (intent != null) {

                        selectedImage = intent.getData();

                        try {

                            //bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), intent.getData());

                            InputStream imageStream = getContext().getContentResolver().openInputStream(selectedImage);
                            bitmap = BitmapFactory.decodeStream(imageStream);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            photoselected = true;

                            upload(bitmap);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
        });

        return view;
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
            activityResultLauncher.launch(Intent.createChooser(intent, "title"));
            //startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_CODE);
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

    private void addProduct() {

        JSONObject paramjson = new JSONObject();
        JSONArray imagejson = new JSONArray();
        for (int m = 0; m < ImageArray.size(); m++) {
            imagejson.put(ImageArray.get(m));
        }

        try {

            paramjson.put("title", ttl);
            paramjson.put("price", pricetot);
            paramjson.put("type", typeid);
            paramjson.put("discount", disc);
            paramjson.put("description", des);
            paramjson.put("soldBy", sold);
            paramjson.put("subcategoryId", subcatcatid);
            paramjson.put("inStock", stok);
            paramjson.put("experience", exp);
            paramjson.put("images", imagejson);
            paramjson.put("weight", wt);
            paramjson.put("isRefundable", bool_productType);
            paramjson.put("categoryId", catid);
            paramjson.put("metadata", jsonObject_metadata);

            Log.d("successresponceVolley", "" + paramjson);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, AppUrl.addProduct_url, paramjson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("onResponse", response.toString());
                try {
                    Log.d("successresponceVolley", "" + response);
                    String code = response.getString("code");

                    if (code.equalsIgnoreCase("200")) {

                        String message = response.getString("msg");

                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    } else {
                        String message = response.getString("msg");

                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("successresponceEr", "" + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onErrorResponse", error.toString());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(getActivity().getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                    Log.d("successresponceVolley", "" + error.networkResponse);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);

                            String data = jsonError.getString("msg");
                            Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();

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
                Map<String, String> headers = new HashMap<>();
                String auth = SharedPrefManager.getInstance(getActivity()).getUser().getToken();
                headers.put("auth-token", auth);
                Log.d("fvsDevbf", "" + auth);
                return headers;
            }
        };
        request.setRetryPolicy(new
                DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(request);
    }

    private void upload(final Bitmap bitmap) {

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        TextView textView = progressDialog.findViewById(R.id.text);
        textView.setText("Image Upload Please wait...");
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);

        String path = RealPathUtil.getRealPath(getContext(), selectedImage);
        File file = new File(path);

        RequestBody imageBode = RequestBody.create(MediaType.parse(getContext().getContentResolver().getType(selectedImage)), file);
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("photo", "productimage.png", imageBode);

        Log.d("fvsdz", "" + selectedImage);


        Call<ImageResponse> call = new ApiToJsonHandler().uploadImage(token, partImage);
        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, retrofit2.Response<ImageResponse> response) {

                progressDialog.dismiss();

                if (response.isSuccessful()) {


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

    public void InIt(View view) {

        categories_spinner = view.findViewById(R.id.categories_spinner);
        prodty_spinner = view.findViewById(R.id.prodty_spinner);
        prodty1_spinner = view.findViewById(R.id.prodty1_spinner);
        subcategory_spinner = view.findViewById(R.id.subcategory_spinner);
        productimage1 = view.findViewById(R.id.productimage1);
        productimage2 = view.findViewById(R.id.productimage2);
        productimage3 = view.findViewById(R.id.productimage3);
        addnewproductbtn = view.findViewById(R.id.addnewproductbtn);
        supercategory = view.findViewById(R.id.supercategory);
        productname = view.findViewById(R.id.productname);
        stock = view.findViewById(R.id.stock);
        weight = view.findViewById(R.id.weight);
        discount = view.findViewById(R.id.discount);
        retailprice = view.findViewById(R.id.retailprice);
        gst = view.findViewById(R.id.gst);
        description = view.findViewById(R.id.description);
        priceClc = view.findViewById(R.id.priceClc);
        servicecharges = view.findViewById(R.id.servicecharges);
        commission = view.findViewById(R.id.commission);
        totalpayment = view.findViewById(R.id.totalpayment);
        dimention = view.findViewById(R.id.dimention);
        color = view.findViewById(R.id.color);
        quentity = view.findViewById(R.id.quentity);

    }

    public void getsuperCatecory() {

        //progressbar.showDialog();

        superCategoryList = new ArrayList<>();
        super_CategoryList = new HashMap<>();

        String url = AppUrl.getSupercategory;

        Log.d("dssjhbjh", url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrl.getSupercategory, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String code = jsonObject.getString("code");
                    String err = jsonObject.getString("err");
                    String msg = jsonObject.getString("msg");
                    String data = jsonObject.getString("data");

                    if (code.equals("200")) {

                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                        JSONArray jsonArray_data = new JSONArray(data);

                        for (int i = 0; i < jsonArray_data.length(); i++) {

                            JSONObject jsonObject_data = jsonArray_data.getJSONObject(i);

                            String _id = jsonObject_data.getString("_id");
                            String name = jsonObject_data.getString("name");

                            superCategoryList.add(name);
                            super_CategoryList.put(name, _id);
                        }

                        superCategoryList.add(0, "Select SuperCategory");

                        ArrayAdapter<String> dataAdapterVehicle = new ArrayAdapter<String>(getActivity(),
                                R.layout.spinneritem, superCategoryList);
                        dataAdapterVehicle.setDropDownViewResource(R.layout.spinnerdropdownitem);
                        supercategory.setAdapter(dataAdapterVehicle);

                        serviceCharge();

                    } else {

                        String message = jsonObject.getString("msg");
                        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(getActivity().getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);

                            String data = jsonError.getString("msg");
                            Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();
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
                Map<String, String> headers = new HashMap<>();
                String auth = SharedPrefManager.getInstance(getActivity()).getUser().getToken();
                headers.put("auth-token", auth);
                Log.d("fvsDevbf", "" + auth);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("fvsDevbf", "" + params);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(50000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    public void getCategory(String supercategoryId) {


        categoryList = new ArrayList<>();
        category_List = new HashMap<>();

        categoryList.clear();
        category_List.clear();

        String category = AppUrl.getCategory + supercategoryId;

        Log.d("hsfjhva", category);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, category, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String code = jsonObject.getString("code");
                    String err = jsonObject.getString("err");
                    String msg = jsonObject.getString("msg");

                    if (code.equals("200")) {

                        if (jsonObject.has("data")) {

                            String data = jsonObject.getString("data");

                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                            JSONArray jsonArray_data = new JSONArray(data);

                            for (int i = 0; i < jsonArray_data.length(); i++) {

                                JSONObject jsonObject_data = jsonArray_data.getJSONObject(i);

                                String _id = jsonObject_data.getString("_id");
                                String name = jsonObject_data.getString("name");
                                String productType = jsonObject_data.getString("productType");
                                String superCategoryId = jsonObject_data.getString("superCategoryId");

                                categoryList.add(name);
                                category_List.put(name, _id);
                            }

                            categoryList.add(0, "Select Category");

                            ArrayAdapter<String> dataAdapterVehicle = new ArrayAdapter<String>(getActivity(),
                                    R.layout.spinneritem, categoryList);
                            dataAdapterVehicle.setDropDownViewResource(R.layout.spinnerdropdownitem);
                            categories_spinner.setAdapter(dataAdapterVehicle);

                        } else {

                            categoryList.add(0, "Select Category");

                            ArrayAdapter<String> dataAdapterVehicle = new ArrayAdapter<String>(getActivity(),
                                    R.layout.spinneritem, categoryList);
                            dataAdapterVehicle.setDropDownViewResource(R.layout.spinnerdropdownitem);
                            categories_spinner.setAdapter(dataAdapterVehicle);

                            String message = jsonObject.getString("msg");
                            Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        }


                    } else {

                        categoryList.add(0, "Select Category");

                        ArrayAdapter<String> dataAdapterVehicle = new ArrayAdapter<String>(getActivity(),
                                R.layout.spinneritem, categoryList);
                        dataAdapterVehicle.setDropDownViewResource(R.layout.spinnerdropdownitem);
                        categories_spinner.setAdapter(dataAdapterVehicle);

                        String message = jsonObject.getString("msg");
                        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(getActivity().getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);

                            String data = jsonError.getString("msg");
                            Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();
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
                Map<String, String> headers = new HashMap<>();
                String auth = SharedPrefManager.getInstance(getActivity()).getUser().getToken();
                headers.put("auth-token", auth);
                Log.d("fvsDevbf", "" + auth);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("fvsDevbf", "" + params);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    public void getSubCategory(String subCategoryId) {


        subcCategoryList = new ArrayList<>();
        subCategory_List = new HashMap<>();

        subCategory_List.clear();
        subcCategoryList.clear();


        String category = AppUrl.getSubCategory + subCategoryId;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, category, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String code = jsonObject.getString("code");
                    String err = jsonObject.getString("err");
                    String msg = jsonObject.getString("msg");

                    if (code.equals("200")) {

                        if (jsonObject.has("data")) {

                            String data = jsonObject.getString("data");

                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                            JSONArray jsonArray_data = new JSONArray(data);

                            for (int i = 0; i < jsonArray_data.length(); i++) {

                                JSONObject jsonObject_data = jsonArray_data.getJSONObject(i);

                                String _id = jsonObject_data.getString("_id");
                                String name = jsonObject_data.getString("name");
                           /* String productType = jsonObject_data.getString("productType");
                            String superCategoryId = jsonObject_data.getString("superCategoryId");*/

                                subcCategoryList.add(name);
                                subCategory_List.put(name, _id);

                            }

                            subcCategoryList.add(0, "select SubCategory");

                            ArrayAdapter<String> dataAdapterVehicle = new ArrayAdapter<String>(getActivity(),
                                    R.layout.spinneritem, subcCategoryList);
                            dataAdapterVehicle.setDropDownViewResource(R.layout.spinnerdropdownitem);
                            subcategory_spinner.setAdapter(dataAdapterVehicle);

                        } else {

                            subCategory_List.clear();
                            subcCategoryList.clear();

                            String message = jsonObject.getString("msg");
                            Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                            subcCategoryList.add(0, "select SubCategory");

                            ArrayAdapter<String> dataAdapterVehicle = new ArrayAdapter<String>(getActivity(),
                                    R.layout.spinneritem, subcCategoryList);
                            dataAdapterVehicle.setDropDownViewResource(R.layout.spinnerdropdownitem);
                            subcategory_spinner.setAdapter(dataAdapterVehicle);
                        }

                    } else {

                        String message = jsonObject.getString("msg");
                        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        subcCategoryList.add(0, "select SubCategory");

                        ArrayAdapter<String> dataAdapterVehicle = new ArrayAdapter<String>(getActivity(),
                                R.layout.spinneritem, subcCategoryList);
                        dataAdapterVehicle.setDropDownViewResource(R.layout.spinnerdropdownitem);
                        subcategory_spinner.setAdapter(dataAdapterVehicle);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(getActivity().getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);

                            String data = jsonError.getString("msg");
                            Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();
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
                Map<String, String> headers = new HashMap<>();
                String auth = SharedPrefManager.getInstance(getActivity()).getUser().getToken();
                headers.put("auth-token", auth);
                Log.d("fvsDevbf", "" + auth);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("fvsDevbf", "" + params);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    public void serviceCharge() {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrl.serviceCharge, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String code = jsonObject.getString("code");
                    String err = jsonObject.getString("err");
                    String msg = jsonObject.getString("msg");
                    String data = jsonObject.getString("data");

                    JSONArray jsonArray_data = new JSONArray(data);

                    for (int i = 0; i < jsonArray_data.length(); i++) {

                        JSONObject jsonObjec_data = jsonArray_data.getJSONObject(i);

                        String charge = jsonObjec_data.getString("charge");
                        String _id = jsonObjec_data.getString("_id");

                    }

                    servicecharges.setText(charge);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(getActivity().getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);

                            String data = jsonError.getString("msg");
                            Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();
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
                Map<String, String> headers = new HashMap<>();
                String auth = SharedPrefManager.getInstance(getActivity()).getUser().getToken();
                headers.put("auth-token", auth);
                Log.d("fvsDevbf", "" + auth);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("fvsDevbf", "" + params);
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    public void extraCharge(String categoryID) {

        // progressbar.showDialog();

        String urlextraCharge = AppUrl.extraCharge + categoryID;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlextraCharge, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("hjdbiugs", response);

                try {

                    String crappyPrefix = "null";

                    if (response.startsWith(crappyPrefix)) {
                        response = response.substring(crappyPrefix.length());
                    }

                    JSONObject jsonObject = new JSONObject(response);

                    String code = jsonObject.getString("code");
                    String err = jsonObject.getString("err");
                    String msg = jsonObject.getString("msg");
                    String data = jsonObject.getString("data");

                    if (code.equals("200")) {

                        if (data.equals("null")) {

                            commission.setText("0");
                            gst.setText("0");

                        } else {

                            JSONObject jsonObject_data = new JSONObject(data);

                            String commission1 = jsonObject_data.getString("commission");
                            String refundCharge1 = jsonObject_data.getString("refundCharge");
                            String gst1 = jsonObject_data.getString("gst");
                            String categoryId1 = jsonObject_data.getString("categoryId");
                            String _id1 = jsonObject_data.getString("_id");

                            commission.setText(commission1);
                            gst.setText(gst1);

                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {

                    e.printStackTrace();

                    Log.d("hsgzxuygjh", e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(getActivity().getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);

                            String data = jsonError.getString("msg");
                            Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();
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
                Map<String, String> headers = new HashMap<>();
                String auth = SharedPrefManager.getInstance(getActivity()).getUser().getToken();
                headers.put("auth-token", auth);
                Log.d("fvsDevbf", "" + auth);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("fvsDevbf", "" + params);
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

}
