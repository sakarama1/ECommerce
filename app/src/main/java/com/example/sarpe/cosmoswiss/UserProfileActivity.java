package com.example.sarpe.cosmoswiss;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UserProfileActivity extends AppCompatActivity {

    EditText nameInput;
    EditText surnameInput;
    EditText emailInput;
    EditText passwordInput;
    EditText phoneNumberInput;
    EditText adressInput;

    Button submitButton;

    RadioButton herr;
    RadioButton frau;

    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        nameInput = findViewById(R.id.vornameEditText);
        surnameInput = findViewById(R.id.nachnameEditText);
        emailInput = findViewById(R.id.benutzernameEditText);
        passwordInput = findViewById(R.id.kennwortEditText);
        phoneNumberInput = findViewById(R.id.handynummerEditText);
        adressInput = findViewById(R.id.adresseEditText);

        submitButton = findViewById(R.id.submitButton);

        herr = findViewById(R.id.herrRadioButton);
        frau = findViewById(R.id.frauRadioButton);

        loadingBar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
    }

    public void submit(View view){

        //define for logged in or out later or coming from control panel

        createAccount();

    }

    private void createAccount() {
        String name = nameInput.getText().toString();
        String surname = surnameInput.getText().toString();
        String email = emailInput.getText().toString().replace(".","_");//to display setback
        String password = passwordInput.getText().toString();
        String phonenumber = phoneNumberInput.getText().toString();
        String adress = adressInput.getText().toString();

        //regulateRadioButton(herr, frau);

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Bitte den Namen Eingeben...", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(surname)){
            Toast.makeText(this, "Bitte den Nachnmen Eingeben...", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Bitte die E-mail Adresse Eingeben...", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Bitte das Kennwort Eingeben...", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(phonenumber)){
            Toast.makeText(this, "Bitte die Handynummer Eingeben...", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(adress)){
            Toast.makeText(this, "Bitte die Adresse Eingeben...", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Konto erstellen");
            loadingBar.setMessage("Bitte Warten...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            validate(name, surname, email, password, phonenumber, adress);
            signUp(email.replace("_", "."), password);

        }


    }



    private  void validate(final String nam, final String nachnam, final String ema, final String pass, final String pheon, final String adr) {

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!(dataSnapshot.child("Users").child(ema).exists())){

                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("name", nam);
                    userDataMap.put("surname", nachnam);
                    userDataMap.put("username", ema);
                    userDataMap.put("password", pass);
                    userDataMap.put("phone", pheon);
                    userDataMap.put("adres", adr);

                    //make user object

                    rootRef.child("Users").child(ema).updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(UserProfileActivity.this, "Konto wurde erstellt", Toast.LENGTH_SHORT);
                                loadingBar.dismiss();

                            }

                            else{
                                Toast.makeText(UserProfileActivity.this, "Bitte noch mals versuchen!!", Toast.LENGTH_SHORT);
                                loadingBar.dismiss();
                            }
                        }
                    });

                }
                else{
                    Toast.makeText(UserProfileActivity.this, "Benutzername ist schon vorhanden",Toast.LENGTH_SHORT );
                    loadingBar.dismiss();
                    Toast.makeText(UserProfileActivity.this, "Bitte einen anderen Benutzername wahlen",Toast.LENGTH_SHORT );
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Intent tologin = new Intent(UserProfileActivity.this, EinloggenActivity.class);
                            startActivity(tologin);
                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(UserProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void regulateRadioButton(final RadioButton but1, final RadioButton but2){
        if(but1.isChecked()){
            but2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    but1.isChecked();
                }
            });
        }
        else{
            but1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    but2.toggle();
                }
            });
        }
    }

}
