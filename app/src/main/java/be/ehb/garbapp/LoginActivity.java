package be.ehb.garbapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView register;
    private EditText editText_email, editText_password;
    private Button signIn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        // To go to register page
        register = (TextView) findViewById(R.id.tv_SwitchToRegister);
        register.setOnClickListener(this);

        signIn = (Button) findViewById(R.id.signIn);
        signIn.setOnClickListener(this);

        editText_email = (EditText) findViewById(R.id.et_LoginEmail);
        editText_password = (EditText) findViewById(R.id.et_LoginPassword);




    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tv_SwitchToRegister:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.signIn:
                userLogin();
                break;
        }
    }

    private void userLogin() {

        String email = editText_email.getText().toString().trim();
        String password = editText_password.getText().toString().trim();

        if(email.isEmpty()){
            editText_email.setError("Email is required");
            editText_email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editText_email.setError("Email must be valid");
            editText_email.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editText_password.setError("Password is required");
            editText_password.requestFocus();
            return;
        }

        if(password.length() < 6){
            editText_password.setError("Password must be 6 char long");
            editText_password.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                    showMainActivity();
                }else{
                    Toast.makeText(LoginActivity.this, "Failed to login! please try again", Toast.LENGTH_LONG).show();
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
