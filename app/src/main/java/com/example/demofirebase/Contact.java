package com.example.demofirebase;

public class Contact {
    public String id;
    public String name;
    public String email;
    public String company;
    public String address;
    public String photoUri;

    public Contact() {
    }

    public Contact(String id, String name, String email, String company, String address, String photoUri) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.company = company;
        this.address = address;
        this.photoUri = photoUri;
    }

    public Contact(String id, String name, String email, String company, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.company = company;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }
}
