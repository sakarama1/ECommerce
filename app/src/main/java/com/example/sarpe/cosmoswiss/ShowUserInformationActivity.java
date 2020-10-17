package com.example.sarpe.cosmoswiss;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.sarpe.cosmoswiss.R;
import com.example.sarpe.cosmoswiss.UserProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ShowUserInformationActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    TextView name;
    TextView surname;
    TextView phonenumber;
    TextView adress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_information);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        name = findViewById(R.id.vornameTextView);
        surname = findViewById(R.id.nachnameTextview);
        phonenumber = findViewById(R.id.handynummerTextView);
        adress = findViewById(R.id.adresseTextView);

        getdataFromDB();

    }

    private void getdataFromDB() {

        final DatabaseReference newRef = myRef.child("Users").child(currentUser.getEmail().replace(".","_"));
        newRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HashMap<String, String> hashMap = new HashMap<String, String>();
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    if(ds.getKey() != "Favs" && ds.getKey() != "Cart" && ds.getKey() != "Orders"){
                        hashMap.put(ds.getKey(), ds.getValue().toString());
                    }

                }
                name.setText(hashMap.get("name"));
                surname.setText(hashMap.get("surname"));
                phonenumber.setText(hashMap.get("phone"));
                adress.setText(hashMap.get("adres"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void changeUserInfo(View view){
        Intent intent = new Intent(getApplicationContext(), ChangeUserInfoActivity.class);
        startActivity(intent);
    }
}
