package com.example.housekeeperapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FogetPassword extends AppCompatActivity {

    private EditText email;
    private Button sumbit;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foget_password);

        email = (EditText)findViewById(R.id.emailreset);
        sumbit = (Button)findViewById(R.id.submit);

        auth=FirebaseAuth.getInstance();

        sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });


    }

    private void resetPassword() {
        String Email = email.getText().toString();

        if(Email.isEmpty()){
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            email.setError("Invalid Email!");
            email.requestFocus();
            return;
        }
        auth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(FogetPassword.this, "Request Send", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(FogetPassword.this,LoginActivity.class));
                }
                else{
                    Toast.makeText(FogetPassword.this, "Try Again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}