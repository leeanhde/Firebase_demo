package com.example.demofirebase.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.demofirebase.R;
import com.example.demofirebase.modals.ContactModal;
import com.example.demofirebase.utils.Const;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateActivity extends AppCompatActivity {

    static final String SELECTED_CONTACT = "SELECTED_CONTACT";

    private EditText editTextId, editTextName, editTextEmail, editTextCompany, editTextAddress;
    private Button updateButton, deleteButton, loadImageButton;
    private ImageView imageView;
    private DatabaseReference databaseReference;
    private static final int PICK_IMAGE_REQUEST = 2;
    private String photoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        editTextId = findViewById(R.id.editTextId);
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextCompany = findViewById(R.id.editTextCompany);
        editTextAddress = findViewById(R.id.editTextAddress);
        imageView = findViewById(R.id.imageView2);

        loadImageButton = findViewById(R.id.loadImageButton);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");


        //todo => update data from bundle selected contact
        ContactModal selectedContact = getIntent().getParcelableExtra(Const.SELECTED_CONTACT);
        if (selectedContact != null) {
            fillContact(selectedContact);
        } else {
            fetchUserData("test");
        }

        loadImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        });

        updateButton.setOnClickListener(v -> showUpdateConfirmationDialog());

        //onCLick delete button
        deleteButton.setOnClickListener(v -> deleteUserData());

        ImageView ivBackUpdate = findViewById(R.id.ivBack);
        ivBackUpdate.setOnClickListener(v -> finish());
    }

    private void fillContact(ContactModal contact) {
        editTextId.setText(contact.getId());
        editTextName.setText(contact.getName());
        editTextEmail.setText(contact.getEmail());
        editTextCompany.setText(contact.getCompany());
        editTextAddress.setText(contact.getAddress());
        if (contact.getPhotoUri() != null && !contact.getPhotoUri().isEmpty()) {
            Glide.with(UpdateActivity.this).load(Uri.parse(contact.getPhotoUri())).into(imageView);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            Glide.with(this).load(imageUri).into(imageView);
            photoUri = imageUri.toString();
        }
    }

    private void fetchUserData(String userId) {
        databaseReference.child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {
                    ContactModal user = dataSnapshot.getValue(ContactModal.class);
                    if (user != null) {
                        editTextId.setText(user.getId());
                        editTextName.setText(user.getName());
                        editTextEmail.setText(user.getEmail());
                        editTextCompany.setText(user.getCompany());
                        editTextAddress.setText(user.getAddress());
                        if (user.getPhotoUri() != null && !user.getPhotoUri().isEmpty()) {
                            Glide.with(UpdateActivity.this).load(Uri.parse(user.getPhotoUri())).into(imageView);
                        }
                    }
                } else {
                    Toast.makeText(UpdateActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(UpdateActivity.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserData() {
        String id = editTextId.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String company = editTextCompany.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();

        if (id.isEmpty() || name.isEmpty() || email.isEmpty() || company.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (photoUri == null || photoUri.isEmpty()) {
            databaseReference.child(id).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot.exists()) {
                        ContactModal existingUser = dataSnapshot.getValue(ContactModal.class);
                        if (existingUser != null && existingUser.getPhotoUri() != null && !existingUser.getPhotoUri().isEmpty()) {
                            photoUri = existingUser.getPhotoUri();
                            updateContact(id, name, email, company, address, photoUri);
                        }
                    }
                } else {
                    Toast.makeText(UpdateActivity.this, "Failed to fetch existing user data", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            updateContact(id, name, email, company, address, photoUri);
        }
    }

    private void updateContact(String id, String name, String email, String company, String address, String photoUri) {
        ContactModal user = new ContactModal(id, name, email, company, address, photoUri);
        databaseReference.child(id).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(UpdateActivity.this, "User data updated successfully", Toast.LENGTH_SHORT).show();
                        // Redirect to ManageActivity
                        Intent intent = new Intent(UpdateActivity.this, ManageActivity.class);
                        startActivity(intent);
                        finish(); // Close the current activity
                    } else {
                        Toast.makeText(UpdateActivity.this, "Failed to update user data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteUserData() {
        String id = editTextId.getText().toString().trim();

        if (!id.isEmpty()) {
            databaseReference.child(id).removeValue()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(UpdateActivity.this, "User data deleted successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UpdateActivity.this, ManageActivity.class));
                        } else {
                            Toast.makeText(UpdateActivity.this, "Failed to delete user data", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "User ID is empty", Toast.LENGTH_SHORT).show();
        }
    }



    private void showUpdateConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Update")
                .setMessage("Are you sure you want to update this contact?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> updateUserData())
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.drawable.ic_launcher_foreground)
                .show();
    }


}