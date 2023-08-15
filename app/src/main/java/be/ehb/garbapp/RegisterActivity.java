package be.ehb.garbapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private TextView registerUser, loginUser;
    private EditText editText_name, editText_Email, editText_Password;
    private Button addProfilePicButton;
    private ImageView imageView_profile_picture;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri = null;
    private final int role = 0;
    private double garbPoints = 0;
    private double totalPoints = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Picasso with a custom OkHttp3Downloader to handle HTTPS images
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true); // Optional: Show Picasso indicator for debugging
        built.setLoggingEnabled(true); // Optional: Enable logging for debugging
        Picasso.setSingletonInstance(built);

        mAuth = FirebaseAuth.getInstance();

        registerUser = (Button) findViewById(R.id.btnRegister);
        loginUser = (TextView) findViewById(R.id.tv_Switch_To_Login);
        addProfilePicButton = (Button) findViewById(R.id.button_add_profile_picture);
        loginUser.setOnClickListener(this);
        registerUser.setOnClickListener(this);
        addProfilePicButton.setOnClickListener(this);

        editText_name = (EditText) findViewById(R.id.et_name);
        editText_Email = (EditText) findViewById(R.id.et_Email);
        editText_Password = (EditText) findViewById(R.id.et_Password);
        imageView_profile_picture = (ImageView) findViewById(R.id.imageView_profile_picture);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister:
                registerUser();
                break;

            case R.id.tv_Switch_To_Login:
                startActivity(new Intent(this, LoginActivity.class));
                break;

            case R.id.button_add_profile_picture:
                openImagePicker();
                break;
        }
    }



    private void registerUser() {

        String name = editText_name.getText().toString().trim();
        String email = editText_Email.getText().toString().trim();
        String password = editText_Password.getText().toString().trim();

        if(name.isEmpty()){
            editText_name.setError("user name is required");
            editText_name.requestFocus();
            return;
        }

        if(email.isEmpty()){
            editText_Email.setError("Email is required");
            editText_Email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editText_Email.setError("Email must be valid");
            editText_Email.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editText_Password.setError("Password is required");
            editText_Password.requestFocus();
            return;
        }

        if(password.length() > 6){
            editText_Password.setError("Password must be 6 char long");
            editText_Password.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String profilePictureUrl = null; // Set it to null for now
                            GarbUser garbUser = new GarbUser(name, email, profilePictureUrl, role, totalPoints);
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                            firebaseUser.updateProfile(profileUpdates);
                            DatabaseReference referenceProfile = FirebaseDatabase.getInstance("https://garbapp-ab823-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Registered User");
                            if (selectedImageUri != null) {
                                // Upload the image and save the URL
                                uploadImageToFirebase(selectedImageUri);
                            }
                            referenceProfile.child(firebaseUser.getUid()).setValue(garbUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                        showMainActivity();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Failed to register User. Please try again", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(RegisterActivity.this, "Failed to register User. Please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData(); // Store the selected image URI
            imageView_profile_picture.setImageURI(selectedImageUri); // Display the selected image
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        // Generate a random filename for the image (you can use the user's UID as well)
        String filename = UUID.randomUUID().toString();

        // Reference to the Firebase Storage location where the image will be stored
        StorageReference storageRef = FirebaseStorage.getInstance("gs://garbapp-ab823.appspot.com").getReference("profile_pictures/" + filename);

        // Upload the image to Firebase Storage
        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL of the image after it's uploaded
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        // Save the image URL to the user's profile data in the Realtime Database
                        saveImageUrlToUserProfile(imageUrl);
                    }).addOnFailureListener(e -> {
                        // Handle any errors in getting the download URL
                        Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle any errors in uploading the image
                    Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveImageUrlToUserProfile(String imageUrl) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            DatabaseReference referenceProfile = FirebaseDatabase.getInstance("https://garbapp-ab823-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Registered User");
            referenceProfile.child(firebaseUser.getUid()).child("profilePictureUrl").setValue(imageUrl)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Image URL is saved successfully in the user's profile data
                            Toast.makeText(RegisterActivity.this, "Profile picture updated", Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle any errors in saving the image URL
                            Toast.makeText(RegisterActivity.this, "Failed to save profile picture", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void showMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}