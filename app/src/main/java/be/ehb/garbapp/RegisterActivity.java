package be.ehb.garbapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private TextView registerUser, loginUser;
    private EditText editText_firstName, editText_lastName, editText_Email, editText_Password;
    private Button addProfilePicButton;
    private ImageView imageView_profile_picture;
    private static final int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        registerUser = (Button) findViewById(R.id.btnRegister);
        loginUser = (TextView) findViewById(R.id.tv_Switch_To_Login);
        addProfilePicButton = (Button) findViewById(R.id.button_add_profile_picture);
        loginUser.setOnClickListener(this);
        registerUser.setOnClickListener(this);
        addProfilePicButton.setOnClickListener(this);

        editText_firstName = (EditText) findViewById(R.id.et_FirstName);
        editText_lastName = (EditText) findViewById(R.id.et_LastName);
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
                startActivityForResult(new Intent(this, UploadProfilePictureActivity.class), PICK_IMAGE_REQUEST);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Retrieve the image URL from the returned Intent and display the image
            String imageUrl = data.getStringExtra("image_url");
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Picasso.with(this).load(imageUrl).into(imageView_profile_picture);
            }
        }
    }

    private void registerUser() {
        String firstName = editText_firstName.getText().toString().trim();
        String lastName = editText_lastName.getText().toString().trim();
        String email = editText_Email.getText().toString().trim();
        String password = editText_Password.getText().toString().trim();

        if(firstName.isEmpty()){
            editText_firstName.setError("First name is required");
            editText_firstName.requestFocus();
            return;
        }

        if(lastName.isEmpty()){
            editText_lastName.setError("Last name is required");
            editText_lastName.requestFocus();
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
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "User has been registered succesfully", Toast.LENGTH_LONG).show();
                            showMainActivity();
                        }else{
                            Toast.makeText(RegisterActivity.this, "Failed to registered User try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void showMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}