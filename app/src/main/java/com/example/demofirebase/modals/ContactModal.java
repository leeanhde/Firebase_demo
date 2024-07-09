package com.example.demofirebase.modals;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ContactModal implements Parcelable {
    public String id;
    public String name;
    public String email;
    public String company;
    public String address;
    public String photoUri;

    public ContactModal() {
    }

    public ContactModal(String id, String name, String email, String company, String address, String photoUri) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.company = company;
        this.address = address;
        this.photoUri = photoUri;
    }

    public ContactModal(String id, String name, String email, String company, String address) {
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

    protected ContactModal(Parcel in) {
        id = in.readString();
        name = in.readString();
        email = in.readString();
        company = in.readString();
        address = in.readString();
        photoUri = in.readString();
    }

    public static final Creator<ContactModal> CREATOR = new Creator<ContactModal>() {
        @Override
        public ContactModal createFromParcel(Parcel in) {
            return new ContactModal(in);
        }

        @Override
        public ContactModal[] newArray(int size) {
            return new ContactModal[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(company);
        parcel.writeString(address);
        parcel.writeString(photoUri);
    }
}
