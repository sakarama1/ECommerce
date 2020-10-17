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
import android.widget.TextView;
import android.widget.Toast;

import com.example.sarpe.cosmoswiss.Classes.User;
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

public class EinloggenActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;

    private Button loginButton;

    private TextView go;

    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_einloggen);

        emailInput = findViewById(R.id.loginEmailEditText);
        passwordInput = findViewById(R.id.loginPasswordEditText);

        loginButton = findViewById(R.id.loginLoginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        go = findViewById(R.id.loginGoToRegTextView);

        loadingBar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

    }

    public void goToReg(View view){
        Intent goToReg = new Intent(getApplicationContext(), UserProfileActivity.class);
        startActivity(goToReg);
    }

    private void loginUser() {

        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Bitte die E-mail Adresse Eingeben...", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Bitte das Kennwort Eingeben...", Toast.LENGTH_SHORT).show();
        }

        else{
            loadingBar.setTitle("Einloggen");
            loadingBar.setMessage("Bitte Warten...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            allowAccess(email, password);
        }
    }

    private void allowAccess(final String email, final String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Intent goToHome = new Intent(EinloggenActivity.this, MainActivity.class);
                            startActivity(goToHome);
                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(EinloggenActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
