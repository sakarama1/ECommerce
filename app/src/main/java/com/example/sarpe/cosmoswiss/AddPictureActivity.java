package com.example.sarpe.cosmoswiss;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public class AddPictureActivity extends AppCompatActivity {

    //Do it

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_picture);
    }

    //select picture method

    public void addNow(View view){
        Intent intent = new Intent(getApplicationContext(), ControlPanelActivity.class);
        startActivity(intent);
    }
}
