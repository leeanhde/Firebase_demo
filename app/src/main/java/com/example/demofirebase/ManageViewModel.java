package com.example.demofirebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManageViewModel extends ViewModel {
    private DatabaseReference dbReference;

    private final MutableLiveData<List<Contact>> _listData = new MutableLiveData<>();
    public LiveData<List<Contact>> listData = _listData;

    public void getData() {
        dbReference = FirebaseDatabase.getInstance().getReference().child("users");
        dbReference.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                List<Contact> contacts = new ArrayList<>();
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    Contact contact = snapshot.getValue(Contact.class);
                    if (contact != null) {
                        contacts.add(contact);
                    }
                }
                Log.d("firebase", contacts.toString());
                _listData.setValue(contacts);
                // Do something with the contacts list
            }
        });
    }

    public void setListData(List<String> data) {

    }
}
