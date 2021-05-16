package com.example.housekeeperapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeMaid extends AppCompatActivity {
    private FirebaseUser user;
    private String uidUser;
    private DatabaseReference dBaseRequest;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private ArrayList<User> list;
    private String curr_user;
    private String MaidName,MaidContact,NameUser,NameContact,NameAddress;
    private ImageView logout,profile;
    HashMap<String,String> userMaid = new HashMap<>();
    HashMap<String,String> userA = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_maid);

        recyclerView = findViewById(R.id.list);
        dBaseRequest= FirebaseDatabase.getInstance().getReference("Request");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new Adapter(this,list);
        recyclerView.setAdapter(adapter);
        AlertDialog dialog= new AlertDialog.Builder(HomeMaid.this)
                .setMessage("Accept this job?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        curr_user = user.getUid();



                        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("AllUsers").child(uidUser);
                        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                NameUser = snapshot.child("Name").getValue().toString();
                                NameAddress = snapshot.child("Address").getValue().toString();
                                NameContact = snapshot.child("Phone").getValue().toString();
                                userA.put("Name",NameUser);
                                userA.put("Address",NameAddress);
                                userA.put("Phone",NameContact);
                                userA.put("UIDUser",uidUser);
                                DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference().child("OnWork").child(curr_user);
                                databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(!snapshot.exists()){
                                            databaseReference3.setValue(userA);
                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("AllUsers").child(curr_user);
                                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    MaidName = snapshot.child("Name").getValue().toString();
                                                    MaidContact = snapshot.child("Phone").getValue().toString();
                                                    userMaid.put("Name",MaidName);
                                                    userMaid.put("Phone",MaidContact);
                                                    userMaid.put("UIDuser",uidUser);
                                                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Pending").child(uidUser);
                                                    databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if(snapshot.exists()){
                                                            }else{
                                                                Toast.makeText(HomeMaid.this, "Confirmed Job", Toast.LENGTH_SHORT).show();
                                                                dBaseRequest.child(uidUser).removeValue();
                                                                databaseReference1.setValue(userMaid);
                                                            }
                                                        }
                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                            startActivity(new Intent(HomeMaid.this,HomeMaidProfile.class));
                                        }
                                        else{
                                            Toast.makeText(HomeMaid.this, "You must finish work before!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        adapter.OnRecyclerViewListener(new Adapter.OnRecyclerViewListener() {
            @Override
            public void OnItemClick(int position) {

                uidUser = list.get(position).getUID();
                NameUser=list.get(position).getName();

                dialog.show();
            }
        });
        dBaseRequest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    list.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        logout = findViewById(R.id.imageOff);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeMaid.this,LoginActivity.class));
            }
        });
        profile = findViewById(R.id.imageProfile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeMaid.this,HomeMaidProfile.class));
            }
        });


    }

}