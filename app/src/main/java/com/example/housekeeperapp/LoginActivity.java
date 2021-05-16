package com.example.housekeeperapp;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    private TextView register,reset;
    private EditText eEmail,ePassword;
    private Button SignIn;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        eEmail=findViewById(R.id.Email);
        ePassword=findViewById(R.id.Password);

        SignIn = findViewById(R.id.SignIn);
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        register = (TextView)findViewById(R.id.Register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,Register.class));
                finish();

            }
        });

        reset = (TextView)findViewById(R.id.forgotPassword);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,FogetPassword.class));
            }
        });

    }
    private void userLogin(){
        String Email = eEmail.getText().toString();
        String Pwd = ePassword.getText().toString();

        if(Email.isEmpty()){
            eEmail.setError("Email is require!!");
            eEmail.requestFocus();
            return;
        }
        if(Pwd.isEmpty()){
            ePassword.setError("Password is require!!");
            ePassword.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            eEmail.setError("Please Enter valid Email");
            eEmail.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(Email,Pwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String uid = task.getResult().getUser().getUid();
                            DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("AllUsers").child(uid);

                            firebaseDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String usertype = snapshot.child("UserType").getValue().toString();
                                    int us=Integer.parseInt(usertype);
                                    if(us==0){
                                        UserNextPage(uid);
                                    }
                                    if(us==1){
                                        MaidNextPage(uid);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Failed to login!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void MaidNextPage(String uid){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("OnWork").child(uid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    startActivity(new Intent(LoginActivity.this,HomeMaidProfile.class));
                }
                else{
                    startActivity(new Intent(LoginActivity.this,HomeMaid.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void UserNextPage(String uid) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Request").child(uid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    startActivity(new Intent(LoginActivity.this, BookingWaiting.class));
                }else{
                    DatabaseReference refPending = FirebaseDatabase.getInstance().getReference().child("Pending").child(uid);
                    refPending.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Intent intent = new Intent(LoginActivity.this, BookingPage.class);
                                intent.putExtra("keyuid",uid);
                                startActivity(intent);
                            }else{
                                startActivity(new Intent(LoginActivity.this, HomeUserProfile.class));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }

}