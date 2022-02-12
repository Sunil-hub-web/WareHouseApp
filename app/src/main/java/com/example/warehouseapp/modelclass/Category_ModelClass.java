package com.example.warehouseapp.modelclass;

public class Category_ModelClass {

    String catId,CategoryUID,catname,productType;

    public Category_ModelClass(String catId, String categoryUID, String catname, String productType) {
        this.catId = catId;
        CategoryUID = categoryUID;
        this.catname = catname;
        this.productType = productType;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCategoryUID() {
        return CategoryUID;
    }

    public void setCategoryUID(String categoryUID) {
        CategoryUID = categoryUID;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    @Override
    public String toString() {
        return "Category_ModelClass{" +
                "catId='" + catId + '\'' +
                ", CategoryUID='" + CategoryUID + '\'' +
                ", catname='" + catname + '\'' +
                ", productType='" + productType + '\'' +
                '}';
    }
}
