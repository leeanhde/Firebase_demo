package com.example.demofirebase.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demofirebase.R;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Button btnView = findViewById(R.id.viewButton);

        btnView.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ManageActivity.class);
            startActivity(intent);
        });

        Button createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, CreateActivity.class);
            startActivity(intent);
        });


    }
}