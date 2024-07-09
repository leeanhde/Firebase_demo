package com.example.demofirebase;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.demofirebase.modals.ContactModal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ManageViewModel extends ViewModel {

    private final MutableLiveData<List<ContactModal>> _listData = new MutableLiveData<>();
    public LiveData<List<ContactModal>> listData = _listData;

    private List<ContactModal> currentContacts = new ArrayList<>();

    public void getData() {
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child("users");
        dbReference.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            } else {
                List<ContactModal> contacts = new ArrayList<>();
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    ContactModal contact = snapshot.getValue(ContactModal.class);
                    if (contact != null) {
                        contacts.add(contact);
                    }
                }
                Log.d("firebase", contacts.toString());
                this.currentContacts = contacts;
                _listData.setValue(contacts);
            }
        });
    }

    public void setListData(boolean isSort) {
        if (isSort) {
            List<ContactModal> contacts = new ArrayList<>(currentContacts);
            contacts.sort(Comparator.comparing(ContactModal::getName, String.CASE_INSENSITIVE_ORDER));
            _listData.setValue(contacts);
        } else {
            _listData.setValue(currentContacts);
        }
    }

}
