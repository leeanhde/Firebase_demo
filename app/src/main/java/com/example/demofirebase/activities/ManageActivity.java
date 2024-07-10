package com.example.demofirebase.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demofirebase.adapters.ContactAdapter;
import com.example.demofirebase.ManageViewModel;
import com.example.demofirebase.R;
import com.example.demofirebase.modals.ContactModal;
import com.example.demofirebase.utils.Const;

import java.util.ArrayList;

public class ManageActivity extends AppCompatActivity implements ContactAdapter.OnContactClickListener {

    private Group grBack;
    private ManageViewModel viewModel;
    private ContactAdapter contactAdapter;
    private RecyclerView rcvContent;

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
        rcvContent = findViewById(R.id.rcvContent);

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
        grBack.setOnClickListener(v -> {
            Intent intent = new Intent(ManageActivity.this, DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        RadioGroup rgFilter = findViewById(R.id.rgFilter);
        rgFilter.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                radioGroup.getChildAt(i).setClickable(true);
            }
            AppCompatRadioButton selected = radioGroup.findViewById(checkedId);
            if (selected != null) {
                selected.setClickable(false);
            }
            assert selected != null;
            RecyclerView.LayoutManager layoutManager;
            if (selected.getId() == R.id.rbVertical) {
                layoutManager = new LinearLayoutManager(ManageActivity.this, LinearLayoutManager.VERTICAL, false);
            } else {
                layoutManager = new GridLayoutManager(ManageActivity.this, 2);
            }
            rcvContent.setLayoutManager(layoutManager);
        });

        AppCompatCheckBox cbSort = findViewById(R.id.cbSortAlpha);

        cbSort.setOnCheckedChangeListener((view, isChecked) -> {
            viewModel.setListData(isChecked);
        });

    }

    @Override
    public void onContactClick(ContactModal contact) {
        Intent intent = new Intent(this, UpdateActivity.class);
        intent.putExtra(Const.SELECTED_CONTACT, contact);
        startActivity(intent);
    }

}
