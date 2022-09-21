package com.kisaanandfactory.warehouseapp.url;

public class AppUrl {

    public static final String MAIN_URL = "https://kisaanandfactory.com/api/v1/";

    public static final String registerUser = "https://kisaanandfactory.com/api/v1/warehouseapp/auth/register";

    public static final String loginUser = "https://kisaanandfactory.com/api/v1/warehouseapp/auth/login";

    public static final String verifyotp = "https://kisaanandfactory.com/api/v1/warehouseapp/auth/register/verify-otp";

    public static final String resendotp = "https://kisaanandfactory.com/api/v1/warehouseapp/auth/register/resend-otp";

    public static final String viewUserDetails = "https://kisaanandfactory.com/api/v1/warehouseapp/main/profile";

    public static final String deliveryrange = "https://kisaanandfactory.com/api/v1/warehouseapp/main/set-delivery-range";

    public static final String updateProfile = "https://kisaanandfactory.com/api/v1/warehouseapp/main/update-profile";

    public static final String updateProfileImage = "https://kisaanandfactory.com/api/v1/vendorapp/vendor/product/image/upload";

    //public static final String getCategory = "https://kisaanandfactory.com/api/v1/adminapp/product/category/all";

    //public static final String getSubCategory = "https://kisaanandfactory.com/api/v1/adminapp/product/sub-category/all?CategoryId=";

    public static final String paymenthistorys = "https://kisaanandfactory.com/api/v1/adminapp/payment/historys?";

    public static final String getAllProduct = "https://kisaanandfactory.com/api/v1/userapp/product/all";

    public static final String getAllOrder = "https://kisaanandfactory.com/api/v1/adminapp/order/all";

    public static final String getAllpacked = "https://kisaanandfactory.com/api/v1/adminapp/order/packed";

    public static final String getAllshipped = "https://kisaanandfactory.com/api/v1/adminapp/order/shipped";

    public static final String getAllcomplaints = "https://kisaanandfactory.com/api/v1/adminapp/complaint/all-complaints";

    public static final String getHomeDetailsZip = "https://kisaanandfactory.com/api/v1/warehouseapp/main/get-home-data?zipcode=";

    public static final String getHomeDetails = "https://kisaanandfactory.com/api/v1/warehouseapp/main/get-home-data";

    //public static final String addProduct = "https://kisaanandfactory.com/api/v1/adminapp/product/add";

    public static final String addVender = "https://kisaanandfactory.com/api/v1/adminapp/user/vendor/add";

    public static final String update_status = "https://kisaanandfactory.com/api/v1/vendorapp/view/myOrders/status/update/";

    public static final String get_driveragent = "https://kisaanandfactory.com/api/v1/warehouseapp/main/get-driver-agent?zipCode=";

    public static final String acceptOrder = "https://kisaanandfactory.com/api/v1/adminapp/order/acceptOrder";

    public static final String addDelivery = "https://kisaanandfactory.com/api/v1/adminapp/user/delivery/add";

    public static final String out_for_delivery = "https://kisaanandfactory.com/api/v1/adminapp/order/out-for-delivery/";

    public static final String getSupercategory = MAIN_URL+"adminapp/product/productSuperCategories";
    public static final String getCategory = MAIN_URL+"vendorapp/vendor/product/category/all?superCategoryId=";
    public static final String getSubCategory = MAIN_URL+"adminapp/product/sub-category/all?CategoryId=";

    public static final String priceCalculator = MAIN_URL+"vendorapp/vendor/product/priceCalculator";

    public static final String addProduct_url = MAIN_URL+"vendorapp/vendor/product/add";



}
