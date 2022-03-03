package com.kisaanandfactory.warehouseapp.modelclass;

public class SubCategory_ModelClass {

    String subcatId,subcatName,CategoryId;

    public SubCategory_ModelClass(String subcatId, String subcatName, String categoryId) {
        this.subcatId = subcatId;
        this.subcatName = subcatName;
        CategoryId = categoryId;
    }

    public String getSubcatId() {
        return subcatId;
    }

    public void setSubcatId(String subcatId) {
        this.subcatId = subcatId;
    }

    public String getSubcatName() {
        return subcatName;
    }

    public void setSubcatName(String subcatName) {
        this.subcatName = subcatName;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }
}
