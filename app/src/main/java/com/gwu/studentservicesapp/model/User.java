package com.gwu.studentservicesapp.model;

public class User {

    private int id;
    private String username;
    private String pName;
    private String email;
    private String password;
    private String location;
    private byte[] profilePicture;
    private String phoneNo;

    public void setPname(String pName){ this.pName = pName;}
    public String getPname(){ return pName;}
    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }
    public byte[] getProfilePicture() {
        return profilePicture;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLocation(String location){ this.location = location;}

    public String getLocation(){ return location;}

    public String getPhoneNumber(){ return phoneNo; }
    public void setPhoneNo(String phoneNo){
        this.phoneNo = phoneNo;
    }
}
