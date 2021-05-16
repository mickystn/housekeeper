package com.example.housekeeperapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegiserUserAddress extends AppCompatActivity{
    private EditText eAdd1,eAdd2,eCity,eState,ePostalC;
    private Button Submit,Back;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser user;

    private String Add1,Add2,City,State,Postal;
    private String Name,Email,Phone,curr_user;
    private String usertype="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regiser_user_address);
        mAuth = FirebaseAuth.getInstance();

        eAdd1 = findViewById(R.id.editTextAddress1);
        eAdd2 = findViewById(R.id.editTextArdress2);
        eCity = findViewById(R.id.editTextCity);
        eState = findViewById(R.id.editTextState);
        ePostalC = findViewById(R.id.editTextPostal);
        Name = getIntent().getStringExtra("keyname");
        Email = getIntent().getStringExtra("keyemail");
        Phone = getIntent().getStringExtra("keynumber");

        Back = findViewById(R.id.Back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegiserUserAddress.this,RegisterUser.class));
            }
        });
        Submit = findViewById(R.id.Submit);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitInfo();
            }
        });
    }

    private void SubmitInfo() {
        Add1 = eAdd1.getText().toString();
        Add2 = eAdd2.getText().toString();
        City = eCity.getText().toString();
        State = eState.getText().toString();
        Postal = ePostalC.getText().toString();

        if(Add1.isEmpty()){
            eAdd1.setError("Address1 is required!");
            eAdd1.requestFocus();
            return;
        }
        if(City.isEmpty()){
            eCity.setError("City is required!");
            eCity.requestFocus();
            return;
        }
        if(State.isEmpty()){
            eState.setError("State is required!");
            eState.requestFocus();
            return;
        }
        if(Postal.isEmpty()){
            ePostalC.setError("Postal Code is required");
            ePostalC.requestFocus();
            return;
        }
        firebaseSignUp();
    }

    private void firebaseSignUp() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String email = firebaseUser.getEmail();
        Toast.makeText(RegiserUserAddress.this, "Account created\n"+email, Toast.LENGTH_SHORT).show();
        HashMap<String,String> userMap = new HashMap<>();
        userMap.put("Email",Email);
        userMap.put("Name",Name);
        userMap.put("Phone",Phone);
        userMap.put("Address",eAdd1.getText().toString()+" "+eAdd2.getText().toString()
                +" "+eCity.getText().toString()+" "+eState.getText().toString()+" "+ePostalC.getText().toString());
        userMap.put("UserType",usertype);

        user = FirebaseAuth.getInstance().getCurrentUser();
        curr_user = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("AllUsers").child(curr_user) ;
        databaseReference.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegiserUserAddress.this, "Created Account!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegiserUserAddress.this,LoginActivity.class));
                }
                else{
                    Toast.makeText(RegiserUserAddress.this, "Failed to Register!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegiserUserAddress.this, "Try Again!", Toast.LENGTH_SHORT).show();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(Name) ;
        databaseReference.setValue(userMap);
    }

}