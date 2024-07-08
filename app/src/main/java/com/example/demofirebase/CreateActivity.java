package com.example.demofirebase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateActivity extends AppCompatActivity {

    private EditText editTextId, editTextName, editTextEmail, editTextCompany, editTextAddress;
    private Button saveButton, loadImageButton;
    private ImageView imageView2;
    private DatabaseReference databaseReference;
    private static final int PICK_IMAGE_REQUEST = 1;
    private String photoUri; // Variable to store selected image URI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        editTextId = findViewById(R.id.editTextId);
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextCompany = findViewById(R.id.editTextCompany);
        editTextAddress = findViewById(R.id.editTextAddress);
        imageView2 = findViewById(R.id.imageView2);
        loadImageButton = findViewById(R.id.loadImageButton);
        saveButton = findViewById(R.id.saveButton);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        loadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open gallery or file picker to select an image
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });
    }

    private void saveUserData() {
        String id = editTextId.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String company = editTextCompany.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();

        if (id.isEmpty() || name.isEmpty() || email.isEmpty() || company.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a user object
        Contact user = new Contact(id, name, email, company, address, photoUri);

        // Push user object to Firebase
        databaseReference.child(id).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(CreateActivity.this, "User data saved successfully", Toast.LENGTH_SHORT).show();
                        // Clear EditText fields after successful save
                        editTextId.setText("");
                        editTextName.setText("");
                        editTextEmail.setText("");
                        editTextCompany.setText("");
                        editTextAddress.setText("");
                        imageView2.setImageResource(0); // Clear image view
                        photoUri = null; // Clear photo URI
                    } else {
                        Toast.makeText(CreateActivity.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            Glide.with(this)
                    .load(imageUri)
                    .into(imageView2);

            // Save the image URI to your photoUri variable or wherever you need it
            photoUri = imageUri.toString();
        }
    }
}
