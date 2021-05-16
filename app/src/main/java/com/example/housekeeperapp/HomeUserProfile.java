package com.example.housekeeperapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeUserProfile extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference ref;

    private String userID;
    private ImageView imgBooking,imgOff,imgHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("AllUsers");
        userID = user.getUid();

        final TextView txtName =findViewById(R.id.textName);
        final TextView txtEmail = findViewById(R.id.textEmailEdit);
        final TextView txtNumber = findViewById(R.id.textNumberEdit);
        final TextView txtAddress = findViewById(R.id.textAddressEdit);
        final TextView txtCity = findViewById(R.id.textCityEdit);
        final TextView txtState = findViewById(R.id.textStateEdit);
        final TextView txtPostal = findViewById(R.id.textPostalCEdit);



        ref.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userName = snapshot.child("Name").getValue().toString();
                String userEmail = snapshot.child("Email").getValue().toString();
                String userNumber = snapshot.child("Phone").getValue().toString();
                String userAddress = snapshot.child("Address").getValue().toString();
                String[] AllAddress = userAddress.split(" ");

                String Address=AllAddress[0]
                        ,Address2=AllAddress[1]
                        ,City=AllAddress[2]
                        ,State=AllAddress[3]
                        ,Postal=AllAddress[4];



                txtName.setText(userName);
                txtEmail.setText(userEmail);
                txtNumber.setText(userNumber);
                txtAddress.setText(Address+" "+Address2);
                txtCity.setText(City);
                txtState.setText(State);
                txtPostal.setText(Postal);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeUserProfile.this, "Something wrong!", Toast.LENGTH_SHORT).show();
            }
        });
        imgOff = findViewById(R.id.imageOff);
        imgOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeUserProfile.this,LoginActivity.class));
            }
        });
        imgBooking = findViewById(R.id.imageBooking);
        imgBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomeUserBooking.class));
            }
        });
    }
}