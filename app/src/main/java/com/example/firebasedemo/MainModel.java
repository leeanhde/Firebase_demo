package com.example.firebasedemo;

public class MainModel {

    String name, address, email, photoUri;

    MainModel() {

    }

    public MainModel(String name, String address, String email, String photoUri) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.photoUri = photoUri;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getphotoUri() {
        return photoUri;
    }

    public void setphotoUri(String photoUri) {
        this.photoUri = photoUri;
    }
}
