package com.example.sarpe.cosmoswiss;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ControlPanelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);
    }

    public void addPicture(View view){
        Intent intent = new Intent(getApplicationContext(), AddPictureActivity.class);
        startActivity(intent);
    }
    public void addUser(View view){
        Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
        startActivity(intent);
    }
    public void addNewItem(View view){
        Intent intent = new Intent(getApplicationContext(), AddItemActivity.class);
        startActivity(intent);
    }
    public void aktion(View view){

    }
}
