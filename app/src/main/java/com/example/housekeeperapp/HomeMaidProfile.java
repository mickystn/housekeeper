package com.example.housekeeperapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class HomeMaidProfile extends AppCompatActivity {
    private ImageView off,task;
    private TextView Name;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private ArrayList<User> list;
    private Button endtask;
    private FirebaseUser user;
    private String curr_user,UserID;
    private DatabaseReference databaseReference,ref1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_maid_profile);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("OnWork");
        ref1=FirebaseDatabase.getInstance().getReference().child("Pending");
        endtask= findViewById(R.id.endtask);
        endtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                curr_user = user.getUid();
                Task<Void> mTask = databaseReference.child(curr_user).removeValue();
                Task<Void> mTaskPending = ref1.child(UserID).removeValue();
                mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                        startActivity(getIntent());
                    }
                });
            }
        });
        off = findViewById(R.id.imageOff);
        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeMaidProfile.this,LoginActivity.class));
            }
        });
        task = findViewById(R.id.imageTask);
        task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeMaidProfile.this,HomeMaid.class));
            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String curr_user = user.getUid();

        Name = findViewById(R.id.Namemaid);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("AllUsers").child(curr_user);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Name.setText(snapshot.child("Name").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView = findViewById(R.id.list);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("OnWork");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new Adapter(this,list);
        recyclerView.setAdapter(adapter);
        databaseReference.child(curr_user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    list.clear();
                    User userA = snapshot.getValue(User.class);
                    list.add(userA);
                    UserID=snapshot.child("UIDUser").getValue().toString();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}