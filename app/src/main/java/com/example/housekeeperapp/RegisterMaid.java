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

public class RegisterMaid extends AppCompatActivity {

    private String Email,Pwd,CFPwd,Name,Number;
    private EditText eEmail,ePwd,eCFPwd,eName,eNumber;
    private Button Submit;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private String curr_user;
    private String usertype="1";


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
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        String email = firebaseUser.getEmail();
                        Toast.makeText(RegisterMaid.this, "Account created\n"+email, Toast.LENGTH_SHORT).show();
                        HashMap<String,String> userMap = new HashMap<>();
                        userMap.put("Email",eEmail.getText().toString());
                        userMap.put("Name",eName.getText().toString());
                        userMap.put("Phone",eNumber.getText().toString());
                        userMap.put("UserType",usertype);

                        user = FirebaseAuth.getInstance().getCurrentUser();
                        curr_user = user.getUid();
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("AllUsers").child(curr_user) ;
                        databaseReference.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(RegisterMaid.this, "YEY", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterMaid.this,LoginActivity.class));
                                }
                                else{
                                    Toast.makeText(RegisterMaid.this, "NOO", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterMaid.this, "FAK", Toast.LENGTH_SHORT).show();
                            }
                        });
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Maids").child(Name) ;
                        databaseReference.setValue(userMap);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterMaid.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}