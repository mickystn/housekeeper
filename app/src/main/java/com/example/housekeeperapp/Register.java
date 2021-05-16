package com.example.housekeeperapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Register extends AppCompatActivity implements View.OnClickListener{

    private ImageView user,maid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        user =(ImageView) findViewById(R.id.userlogo);
        user.setOnClickListener(this);
        maid =(ImageView) findViewById(R.id.maidlogo);
        maid.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.userlogo:
                startActivity(new Intent(getApplicationContext(),RegisterUser.class));
                break;
            case R.id.maidlogo:
                startActivity(new Intent(getApplicationContext(),RegisterMaid.class));
                break;
        }
    }
}