package com.gwu.studentservicesapp.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Item {
    private int product_id;
    private String product_name;
    private String product_description;
    private String product_price;
    private String product_location;
    private String product_category;
    private byte[] profilePicture;
    private Bitmap picture;

    public void setItemPicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }
    public byte[] getItemPicture() {
        return profilePicture;
    }

    public int getProductId() {
        return product_id;
    }

    public void setProductId(int id) {
        this.product_id = id;
    }

    public String getProductName() {
        return product_name;
    }

    public void setProductName(String product_name) {
        this.product_name = product_name;
    }

    public String getProductDescription() {
        return product_description;
    }

    public void setProductDescription(String product_description) {
        this.product_description = product_description;
    }
    public void setProductCategory(String product_category){
        this.product_category = product_category;
    }
    public String getProductCategory(){
        return  product_category;
    }
    public String getProductPrice() {
        return product_price;
    }

    public void setProductPrice(double product_price) {
        this.product_price = String.valueOf(product_price);
    }

    public void setProductLocation(String location){ this.product_location = location;}

    public String getProductLocation(){ return product_location;}

    public void setPicture(byte[] arrays){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        this.picture = getPicFromBytes(arrays,options);
    }

    public Bitmap getPicture(){
        return picture;
    }

    public Item(int product_id,String product_name,String product_description,double product_price,String product_location,String product_category,byte[] profilePicture){
        setProductId(product_id);
        setProductCategory(product_category);
        setProductDescription(product_description);
        setProductLocation(product_location);
        setProductName(product_name);
        setProductPrice(product_price);
        setPicture(profilePicture);
//        setItemPicture(profilePicture);
    }

    public Bitmap getPicFromBytes(byte[] bytes,
                                         BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                        opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }

    public Item(){}
}
