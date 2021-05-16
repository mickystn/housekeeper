package com.example.housekeeperapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookingPage extends AppCompatActivity {
    DatabaseReference databaseReference;
    private TextView MaidName,MaidContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_page);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String curr_user = user.getUid();

        MaidName = findViewById(R.id.maidname);
        MaidContact = findViewById(R.id.maidcontact);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Pending").child(curr_user);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    MaidName.setText(snapshot.child("Name").getValue().toString());
                    MaidContact.setText(snapshot.child("Phone").getValue().toString());
                }else{
                    startActivity(new Intent(BookingPage.this,HomeUserProfile.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
}