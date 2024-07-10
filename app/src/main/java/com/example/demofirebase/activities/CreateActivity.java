package com.example.demofirebase.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.demofirebase.R;
import com.example.demofirebase.modals.ContactModal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

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

        // Tìm ImageView và TextView
        ImageView ivBack = findViewById(R.id.ivBack);
        TextView tvBack = findViewById(R.id.tvBack);

        // Thiết lập sự kiện nhấn cho ImageView
        View.OnClickListener backClickListener = v -> {
            Intent intent = new Intent(CreateActivity.this, DashboardActivity.class);
            startActivity(intent);
        };

        ivBack.setOnClickListener(backClickListener);
        tvBack.setOnClickListener(backClickListener);

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

        // Validate name (no special characters)
        if (!Pattern.matches("^[a-zA-Z0-9 ]+$", name)) {
            Toast.makeText(this, "Name should not contain special characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate email format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if ID already exists
        databaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(CreateActivity.this, "ID already exists. Please use a different ID.", Toast.LENGTH_SHORT).show();
                } else {
                    // Create a user object
                    ContactModal user = new ContactModal(id, name, email, company, address, photoUri);

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
                                    startActivity(new Intent(CreateActivity.this, ManageActivity.class));
                                } else {
                                    Toast.makeText(CreateActivity.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(CreateActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
