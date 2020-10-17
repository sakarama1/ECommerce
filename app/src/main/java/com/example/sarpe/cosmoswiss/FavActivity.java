package com.example.sarpe.cosmoswiss;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sarpe.cosmoswiss.Adapters.CategoryAdapter;
import com.example.sarpe.cosmoswiss.Adapters.ProductAdapter;
import com.example.sarpe.cosmoswiss.Classes.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FavActivity extends AppCompatActivity {

    //similar to cardActivity without buying

    private ListView listView;
    TextView fav;

    private Product toAdd;

    private ArrayList<Product> allFavs;
    private ArrayList<String> allFavNames;
    private ArrayList<String> allFavPrices;
    private ArrayList<Bitmap> allFavPictures;

    FirebaseDatabase database;
    DatabaseReference myRef;

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    Bitmap picture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        allFavs = new ArrayList<Product>();
        allFavNames = new ArrayList<String>();
        allFavPrices = new ArrayList<String>();
        allFavPictures = new ArrayList<Bitmap>();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        fav = findViewById(R.id.favTextView);

        listView = findViewById(R.id.favListView);
        picture = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.cosmo);

        Intent intent = getIntent();
        toAdd = (Product) intent.getSerializableExtra("chosen");
        String decide = intent.getStringExtra("info");



        readFromDB();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setAdapters();
            }
        }, 2000);

    }

    private void readFromDB() {

        final DatabaseReference newRef = myRef.child("Users").child(currentUser.getEmail().replace(".","_")).child("Favs");
        newRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    final HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                    Product product;
                    Log.d("anan", hashMap.get("itemName") + hashMap.get("itemPrice"));
                    product = new Product(hashMap.get("code"), hashMap.get("nameEnglish"), hashMap.get("nameGerman"),
                            hashMap.get("brand"), hashMap.get("mainCategory"), hashMap.get("subCategory"), hashMap.get("barcode"),
                            hashMap.get("costSalon"), hashMap.get("costWeb"), hashMap.get("quantity"), hashMap.get("picture"));

                    allFavs.add(product);
                    allFavNames.add(hashMap.get("nameGerman"));
                    allFavPrices.add(hashMap.get("costWeb"));
                    allFavPictures.add(picture); //sey olunca duzelt


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setAdapters() {
        if (allFavs.size() > 0){
            final ProductAdapter adapter = new ProductAdapter(allFavNames, allFavPrices, allFavPictures, this);

            fav.setText("Meine Favoriten");
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent toShowItem = new Intent(getApplicationContext(), ShowItemActivity.class);
                    //send neccesary things
                    toShowItem.putExtra("object", allFavs.get(position));
                    startActivity(toShowItem);
                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                   String doDel = allFavNames.get(position);
                    //delete locally
                    allFavs.remove(position);
                    allFavNames.remove(position);
                    allFavPrices.remove(position);
                    allFavPictures.remove(position);

                    if(allFavNames.size() == 0){
                        Intent goToMain = new Intent(FavActivity.this, MainActivity.class);
                        startActivity(goToMain);
                    }

                    adapter.notifyDataSetChanged();
                    //delete from database

                    final DatabaseReference newRef = myRef.child("Users").child(currentUser.getEmail().replace(".","_")).child("Favs").child(doDel);
                    newRef.removeValue();

                    Toast.makeText(getApplicationContext(), "Item Deleted", Toast.LENGTH_LONG).show();

                    return true;
                }
            });
        }

        //https://stackoverflow.com/questions/47854504/how-to-add-and-retrieve-data-into-firebase-using-lists-arraylists
        else{
            listView.setVisibility(View.GONE);
            fav.setText("Keine Favoriten bisher...");
        }

    }
}
