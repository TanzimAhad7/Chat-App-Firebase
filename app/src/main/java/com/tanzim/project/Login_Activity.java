package com.tanzim.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Login_Activity extends AppCompatActivity {

    EditText userETLogin,passETLogin;
    Button LoginBtn,RegisterBtn;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // checking for users existance

       /* if(firebaseUser == null){
            Intent i = new Intent(Login_Activity.this,MainActivity.class);
            startActivity(i);
            finish();
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");

        userETLogin =  findViewById(R.id.editTextLogin);
        passETLogin = findViewById(R.id.editTextPassword);
        LoginBtn = findViewById(R.id.buttonLogin);
        RegisterBtn = findViewById(R.id.button2);

        auth = FirebaseAuth.getInstance();


        //register button

        RegisterBtn.setOnClickListener(view -> {
            Intent i = new Intent(Login_Activity.this,RegisterActivity.class);
            startActivity(i);
        });

        // login button

        LoginBtn.setOnClickListener(view -> {
            progressDialog.show();
            String email_text = userETLogin.getText().toString();
            String pass_text = passETLogin.getText().toString();

            // empty check
            if (TextUtils.isEmpty(email_text) || TextUtils.isEmpty(pass_text)){
                Toast.makeText(Login_Activity.this, "Please Fill out all the fields", Toast.LENGTH_SHORT).show();
            }else{
                auth.signInWithEmailAndPassword(email_text,pass_text)
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                if(Objects.requireNonNull(auth.getCurrentUser()).isEmailVerified()){
                                    Toast.makeText(Login_Activity.this, "Welcome to RUET Connect", Toast.LENGTH_SHORT).show();

                                    Intent i = new Intent(Login_Activity.this, MainActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    finish();
                                }else{
                                    Toast.makeText(Login_Activity.this, "Please Verify Email", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(Login_Activity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.hide();
                        });
            }

        });
    }
}