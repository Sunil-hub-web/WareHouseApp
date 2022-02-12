package com.example.warehouseapp.modelclass;

import java.util.ArrayList;

public class AllProduct_ModelClass {

    String discount,id,title,price,type,description,totalRating,favid,inStock,experience,soldBy,createdAt;
    ArrayList<Image_ModelClass> image_modelClasses;
    ArrayList<Weight_ModelClass> weight_modelClasses;

    public AllProduct_ModelClass(String discount, String id, String title, String price, String type,
                                 String description, String totalRating, String favid, String inStock,
                                 String experience, String soldBy, ArrayList<Image_ModelClass> image_modelClasses,
                                 ArrayList<Weight_ModelClass> weight_modelClasses,String createdAt) {
        this.discount = discount;
        this.id = id;
        this.title = title;
        this.price = price;
        this.type = type;
        this.description = description;
        this.totalRating = totalRating;
        this.favid = favid;
        this.inStock = inStock;
        this.experience = experience;
        this.soldBy = soldBy;
        this.image_modelClasses = image_modelClasses;
        this.weight_modelClasses = weight_modelClasses;
        this.createdAt = createdAt;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(String totalRating) {
        this.totalRating = totalRating;
    }

    public String getFavid() {
        return favid;
    }

    public void setFavid(String favid) {
        this.favid = favid;
    }

    public String getInStock() {
        return inStock;
    }

    public void setInStock(String inStock) {
        this.inStock = inStock;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSoldBy() {
        return soldBy;
    }

    public void setSoldBy(String soldBy) {
        this.soldBy = soldBy;
    }

    public ArrayList<Image_ModelClass> getImage_modelClasses() {
        return image_modelClasses;
    }

    public void setImage_modelClasses(ArrayList<Image_ModelClass> image_modelClasses) {
        this.image_modelClasses = image_modelClasses;
    }

    public ArrayList<Weight_ModelClass> getWeight_modelClasses() {
        return weight_modelClasses;
    }

    public void setWeight_modelClasses(ArrayList<Weight_ModelClass> weight_modelClasses) {
        this.weight_modelClasses = weight_modelClasses;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
