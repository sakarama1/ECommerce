package com.example.sarpe.cosmoswiss;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeUserInfoActivity extends AppCompatActivity {

    EditText nameInput;
    EditText surnameInput;
    EditText emailInput;
    EditText passwordInput;
    EditText phoneNumberInput;
    EditText adressInput;

    Button submitButton;

    RadioButton herr;
    RadioButton frau;

    FirebaseDatabase database;
    DatabaseReference myRef;

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_info);

        nameInput = findViewById(R.id.vornameEditTextc);
        surnameInput = findViewById(R.id.nachnameEditTextc);
        emailInput = findViewById(R.id.benutzernameEditTextc);
        passwordInput = findViewById(R.id.kennwortEditTextc);
        phoneNumberInput = findViewById(R.id.handynummerEditTextc);
        adressInput = findViewById(R.id.adresseEditTextc);

        submitButton = findViewById(R.id.submitButtonc);

        herr = findViewById(R.id.herrRadioButtonc);
        frau = findViewById(R.id.frauRadioButtonc);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    public void submit(View view){

        if(!nameInput.getText().toString().equals(""))myRef.child("Users").child(currentUser.getEmail().replace(".","_")).child("name").setValue(nameInput.getText().toString());
        if(!surnameInput.getText().toString().equals(""))myRef.child("Users").child(currentUser.getEmail().replace(".","_")).child("surname").setValue(surnameInput.getText().toString());
        if(!emailInput.getText().toString().equals(""))myRef.child("Users").child(currentUser.getEmail().replace(".","_")).child("username").setValue(emailInput.getText().toString().replace(".", "_"));
        if(!passwordInput.getText().toString().equals(""))myRef.child("Users").child(currentUser.getEmail().replace(".","_")).child("password").setValue(passwordInput.getText().toString());
        if(!phoneNumberInput.getText().toString().equals(""))myRef.child("Users").child(currentUser.getEmail().replace(".","_")).child("phone").setValue(phoneNumberInput.getText().toString());
        if(!adressInput.getText().toString().equals(""))myRef.child("Users").child(currentUser.getEmail().replace(".","_")).child("adres").setValue(adressInput.getText().toString());

        Toast.makeText(this, "Anderungen wurden gespeichert", Toast.LENGTH_SHORT).show();
        Intent goToShow = new Intent(ChangeUserInfoActivity.this, ShowUserInformationActivity.class);
        startActivity(goToShow);


    }
}
