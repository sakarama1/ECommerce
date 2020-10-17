package com.example.sarpe.cosmoswiss;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sarpe.cosmoswiss.Classes.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ShowItemActivity extends AppCompatActivity {

    Button toCartButton;
    Button toFavButton;
    Button plusButton;
    Button minusButton;

    ImageView showImage;

    TextView showItemName;
    TextView showPrice;
    TextView showQuantity;

    EditText enterQuantity;
    Product toShow;

    FirebaseDatabase database;
    DatabaseReference myRef;

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item);

        Intent intent = getIntent();
        toShow = (Product) intent.getSerializableExtra("object");
        String quant = intent.getStringExtra("quant");

        toCartButton = findViewById(R.id.toCartButton);
        toFavButton = findViewById(R.id.toFavoriteButton);
        plusButton = findViewById(R.id.plusButton);
        minusButton = findViewById(R.id.minusButton);

        showImage = findViewById(R.id.showItemImageView);

        showItemName = findViewById(R.id.showItemNameTextView);
        showPrice = findViewById(R.id.showItemPriceTextView);
        showQuantity = findViewById(R.id.showQuantityTextView);

        enterQuantity = findViewById(R.id.enterQuantityEditText);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        showItemName.setText(toShow.getNameGerman());
        showPrice.setText(toShow.getCostWeb());
        enterQuantity.setText(quant);
        if(Integer.parseInt(toShow.getQuantity()) > 0){
            String dummy = toShow.getQuantity() + " verfugbar";
            showQuantity.setText(dummy);
        }
        else{
            showQuantity.setText("Ausverkauft");
        }


    }

    public void plus(View view){

        if(isInteger(enterQuantity.getText().toString()) && Integer.parseInt(enterQuantity.getText().toString()) < Integer.parseInt(toShow.getQuantity())){
            int quantityNumber = Integer.parseInt(enterQuantity.getText().toString());
            String newQuantityNumber = Integer.toString(++quantityNumber);
            enterQuantity.setText(newQuantityNumber);
        }
        else {
            enterQuantity.setText("1");
        }
    }

    public void minus(View view){
        if(isInteger(enterQuantity.getText().toString()) && Integer.parseInt(enterQuantity.getText().toString()) > 1){
            int quantityNumber = Integer.parseInt(enterQuantity.getText().toString());
            String newQuantityNumber = Integer.toString(--quantityNumber);
            enterQuantity.setText(newQuantityNumber);
        }
        else if (enterQuantity.getText().toString().equals("")){
            enterQuantity.setText("1");
        }
        else {
            enterQuantity.setText("1");
        }
    }

    public void buy(View view){
        if(isInteger(enterQuantity.getText().toString())){
            final Intent goToCart = new Intent(getApplicationContext(), CartActivity.class);
            //firebase le yap
            goToCart.putExtra("info", "add");
            goToCart.putExtra("chosen", toShow);
            goToCart.putExtra("chosenQ", enterQuantity.getText().toString());

            addToDBCart();

            startActivity(goToCart);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(), "Wahlen sie bitte, wie viel sie kaufen wollen", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void addToDBCart() {
        final DatabaseReference newRef = myRef.child("Users").child(currentUser.getEmail().replace(".","_"));
                newRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(!(dataSnapshot.child("Cart").child(toShow.getNameGerman()).exists())){

                HashMap<String, Object> userDataMap = new HashMap<>();
                userDataMap.put("code", toShow.getCode());
                userDataMap.put("nameEnglish", toShow.getNameEnglish());
                userDataMap.put("nameGerman", toShow.getNameGerman());
                userDataMap.put("brand", toShow.getBrand());
                userDataMap.put("mainCategory", toShow.getMainCategory());
                userDataMap.put("subCategory", toShow.getSubCategory());
                userDataMap.put("barcode", toShow.getBarcode());
                userDataMap.put("costSalon", toShow.getCostSalon());
                userDataMap.put("costWeb", toShow.getCostWeb());
                userDataMap.put("quantity", toShow.getQuantity());
                userDataMap.put("picture", toShow.getPicture());
                userDataMap.put("boughtQuantity", enterQuantity.getText().toString());

                //make user object

                newRef.child("Cart").child(toShow.getNameGerman()).updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ShowItemActivity.this, "Die Ware wurde in den Warenkorb hinzugefugt", Toast.LENGTH_SHORT).show();
                    }

                    else{
                    Toast.makeText(ShowItemActivity.this, "Bitte noch mals versuchen!!", Toast.LENGTH_SHORT).show();
                    }
                }});

            }
            else{
                Toast.makeText(ShowItemActivity.this, "Quantitat wurde verandert", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

            }});
    }

    public void addToFav(View view){
        Intent goToFav = new Intent(getApplicationContext(), FavActivity.class);
        //firebase le yap

        addToDBFav();

        //can be removed
        goToFav.putExtra("chosen", toShow);
        goToFav.putExtra("info", "add");
        startActivity(goToFav);
    }

    private void addToDBFav() {

        final DatabaseReference newRef = myRef.child("Users").child(currentUser.getEmail().replace(".","_"));
                newRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Favs").child(toShow.getNameGerman()).exists())){

                HashMap<String, Object> userDataMap = new HashMap<>();
                userDataMap.put("code", toShow.getCode());
                userDataMap.put("nameEnglish", toShow.getNameEnglish());
                userDataMap.put("nameGerman", toShow.getNameGerman());
                userDataMap.put("brand", toShow.getBrand());
                userDataMap.put("mainCategory", toShow.getMainCategory());
                userDataMap.put("subCategory", toShow.getSubCategory());
                userDataMap.put("barcode", toShow.getBarcode());
                userDataMap.put("costSalon", toShow.getCostSalon());
                userDataMap.put("costWeb", toShow.getCostWeb());
                userDataMap.put("quantity", toShow.getQuantity());
                userDataMap.put("picture", toShow.getPicture());

                //make user object

                newRef.child("Favs").child(toShow.getNameGerman()).updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful()){
                Toast.makeText(ShowItemActivity.this, "Die Ware wurde in den Favoriten hinzugefugt", Toast.LENGTH_SHORT).show();
            }

            else{
                Toast.makeText(ShowItemActivity.this, "Bitte noch mals versuchen!!", Toast.LENGTH_SHORT).show();
            }
        }});

            }
            else{
                Toast.makeText(ShowItemActivity.this, "Die Ware ist schon in den Favoriten",Toast.LENGTH_SHORT ).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

            }
            });
    }

    //https://stackoverflow.com/questions/237159/whats-the-best-way-to-check-if-a-string-represents-an-integer-in-java
    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
        if (length == 1) {
            return false;
        }
        i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
        }
}
