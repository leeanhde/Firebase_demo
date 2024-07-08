package com.example.demofirebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ManageActivity extends AppCompatActivity implements ContactAdapter.OnContactClickListener {

    private Group grBack;
    private ManageViewModel viewModel;
    private ContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(ManageViewModel.class);
        viewModel.getData();
        initUi();
        initListeners();
        initObservers();
    }

    private void initUi() {
        grBack = findViewById(R.id.grBack);
        RecyclerView rcvContent = findViewById(R.id.rcvContent);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvContent.setLayoutManager(layoutManager);
        contactAdapter = new ContactAdapter(new ArrayList<>(), this);
        rcvContent.setAdapter(contactAdapter);
    }

    private void initObservers() {
        viewModel.listData.observe(this, contacts -> {
            if (!contacts.isEmpty()) {
                contactAdapter.addContact(contacts);
            }
        });
    }

    private void initListeners() {
        grBack.setOnClickListener(v -> finish());
    }

    @Override
    public void onContactClick(Contact contact) {
        Intent intent = new Intent(this, UpdateActivity.class);
        intent.putExtra(Const.SELECTED_CONTACT, contact);
        startActivity(intent);
    }

}
