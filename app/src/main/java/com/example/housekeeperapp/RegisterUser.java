package com.example.housekeeperapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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

public class RegisterUser extends AppCompatActivity {
    private String Email,Pwd,CFPwd,Number;
    private String Name;
    private EditText eEmail,ePwd,eCFPwd,eName,eNumber;
    private Button Submit,Back;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private String curr_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regiser_user);
        mAuth = FirebaseAuth.getInstance();
        eEmail = findViewById(R.id.Email_Info);
        ePwd = findViewById(R.id.Password_Info);
        eCFPwd = findViewById(R.id.ConfrimPass_info);
        eName = findViewById(R.id.Name_Info);
        eNumber = findViewById(R.id.Number_Info);
        Back = findViewById(R.id.Back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterUser.this,LoginActivity.class));
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
        Email = eEmail.getText().toString();
        Pwd = ePwd.getText().toString();
        CFPwd = eCFPwd.getText().toString();
        Number = eNumber.getText().toString();
        Name = eName.getText().toString();

        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            eEmail.setError("Invalid Email");
        }
        else if(TextUtils.isEmpty(Pwd)){
            ePwd.setError("Enter Password");
        }
        else if(!(CFPwd!=Pwd)){
            ePwd.setError("Password aren't the same");
        }
        else{
            firebaseSignUp();
        }
    }
    private void firebaseSignUp() {

        mAuth.createUserWithEmailAndPassword(Email,Pwd)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Intent intent = new Intent(RegisterUser.this,RegiserUserAddress.class);
                        intent.putExtra("keyname",Name);
                        intent.putExtra("keynumber",Number);
                        intent.putExtra("keyemail",Email);
                        startActivity(intent);
                    }
                });
        /*mAuth.createUserWithEmailAndPassword(Email,Pwd)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        curr_user = user.getUid();
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(Name);
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        String email = firebaseUser.getEmail();
                        Toast.makeText(RegisterUser.this, "Account created\n"+email, Toast.LENGTH_SHORT).show();
                        HashMap<String,String> userMap = new HashMap<>();
                        userMap.put("Email",eEmail.getText().toString());
                        userMap.put("Name",eName.getText().toString());
                        userMap.put("Phone",eNumber.getText().toString());


                        databaseReference
                                .setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(RegisterUser.this, "YEY", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterUser.this,RegiserUserAddress.class));
                                }
                                else{
                                    Toast.makeText(RegisterUser.this, "NOO", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterUser.this, "FAK", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterUser.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });*/
    }


}