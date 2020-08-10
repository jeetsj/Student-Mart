package com.gwu.studentservicesapp.model;

public class Apartment {
    private int apt_id;
    private String apt_description;
    private int apt_price;
    private String apt_location;
    private byte[] profilePicture;


    public int getAptId() {
        return apt_id;
    }

    public void setAptId(int id) {
        this.apt_id = id;
    }

    public String getAptDescription() {
        return apt_description;
    }

    public void setAptDescription(String apt_description) {
        this.apt_description = apt_description;
    }

    public int getAptPrice() {
        return apt_price;
    }

    public void setAptPrice(int apt_price) {
        this.apt_price = apt_price;
    }

    public void setAptLocation(String apt_location){ this.apt_location = apt_location;}

    public String getAptLocation(){ return apt_location;}

    public void setItemPicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }
    public byte[] getItemPicture() {
        return profilePicture;
    }
}
