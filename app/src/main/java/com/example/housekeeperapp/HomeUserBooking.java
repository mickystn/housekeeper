package com.example.housekeeperapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class HomeUserBooking extends AppCompatActivity {
    private Button Submits;
    private DatabaseReference databaseReference,ref;
    private FirebaseUser user;
    private String curr_user;
    private TextView AddressEd,ContactEd;
    private String Name,userAddress,Contact;
    private ImageView imgProfile,imgLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user_booking);

        AddressEd=findViewById(R.id.txtAddressCF);
        ContactEd=findViewById(R.id.txtPCCF2);

        imgProfile=findViewById(R.id.imageProfile);
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeUserBooking.this,HomeUserProfile.class));
            }
        });
        imgLogout=findViewById(R.id.imageOff);
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeUserBooking.this,LoginActivity.class));
            }
        });



        user = FirebaseAuth.getInstance().getCurrentUser();
        curr_user = user.getUid();
        ref= FirebaseDatabase.getInstance().getReference().child("AllUsers").child(curr_user);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userAddress = snapshot.child("Address").getValue().toString();
                Name =  snapshot.child("Name").getValue().toString();
                Contact = snapshot.child("Phone").getValue().toString();

                AddressEd.setText(userAddress);
                ContactEd.setText(Contact);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Submits = findViewById(R.id.Submits);
        Submits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitInfo();
            }
        });
    }

    private void SubmitInfo() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        curr_user = user.getUid();
        HashMap<String,String> userMap = new HashMap<>();
        userMap.put("Name",Name);
        userMap.put("Phone",Contact);
        userMap.put("Address",userAddress);
        userMap.put("UID",curr_user);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Request").child(curr_user);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    startActivity(new Intent(HomeUserBooking.this, BookingWaiting.class));
                    Toast.makeText(HomeUserBooking.this, "Already Booking!", Toast.LENGTH_SHORT).show();
                }
                else{
                    databaseReference.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                startActivity(new Intent(HomeUserBooking.this,BookingWaiting.class));
                                Toast.makeText(HomeUserBooking.this, "Booking Complete", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(HomeUserBooking.this, "Failed to Booking!", Toast.LENGTH_SHORT).show();
                            }
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