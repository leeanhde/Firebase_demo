package com.example.demofirebase;

public class User {
    public String id;
    public String name;
    public String email;
    public String company;
    public String address;
    public String photoUri;

    public User() {
    }

    public User(String id, String name, String email, String company, String address, String photoUri) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.company = company;
        this.address = address;
        this.photoUri = photoUri;
    }

    public User(String id, String name, String email, String company, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.company = company;
        this.address = address;
    }
}
