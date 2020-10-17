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

import com.example.sarpe.cosmoswiss.Adapters.CartAdapter;
import com.example.sarpe.cosmoswiss.Adapters.HistoryAdapter;
import com.example.sarpe.cosmoswiss.Classes.Order;
import com.example.sarpe.cosmoswiss.Classes.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class HistoryActivity extends AppCompatActivity {

    ListView listView;
    TextView header;

    //for first adapter
    ArrayList<Order> allOrders;
    ArrayList<String> allOrderDates;
    ArrayList<String> allOrderNumbers;
    ArrayList<String> allOrderCurrentSituations;

    //for second  adapter
    ArrayList<Product> allProductsInOrder;
    private ArrayList<Product> allCarts;
    private ArrayList<String> allCartNames;
    private ArrayList<String> allCartPrices;
    private ArrayList<String> allProductQuantities; //save all
    private ArrayList<String> allCartQuantities; //save all
    private ArrayList<Bitmap> allCartPictures;

    HistoryAdapter historyAdapter;
    CartAdapter cartAdapter;

    String decide;
    String info;

    FirebaseDatabase database;
    DatabaseReference myRef;

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    Bitmap picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //get it to make a new order
        Intent intent = getIntent();
        allProductsInOrder = (ArrayList<Product>) intent.getSerializableExtra("allitemsinorder");
        allProductQuantities = intent.getStringArrayListExtra("objectQuantity");
        decide = intent.getStringExtra("info");

        listView = findViewById(R.id.orderListView);
        header = findViewById(R.id.orderTextView);

        //first Adapter
        allOrders = new ArrayList<Order>();
        allOrderDates = new ArrayList<String>();
        allOrderNumbers = new ArrayList<String>();
        allOrderCurrentSituations = new ArrayList<String>();

        //second Adapter
        allCarts = new ArrayList<Product>();
        allCartNames = new ArrayList<String>();
        allCartPrices = new ArrayList<String>();
        allCartQuantities = new ArrayList<String>();
        allCartPictures = new ArrayList<Bitmap>();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        picture = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.cosmo);

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
        final DatabaseReference newRef = myRef.child("Users").child(currentUser.getEmail().replace(".","_")).child("Orders");
        newRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    HashMap<String, String> hashMap = new HashMap<>();

                    if(ds.getKey() != "Products"){
                        hashMap = (HashMap<String, String>) ds.getValue();
                        ArrayList<Product> arrayListP = new ArrayList<>();
                        ArrayList<String> arrayListQ = new ArrayList<>();
                        Log.d("anan", hashMap.get("orderNumber"));
                        for(DataSnapshot ds1 : dataSnapshot.child(hashMap.get("orderNumber")).child("Products").getChildren()){
                            final HashMap<String, String> hashMap1 = (HashMap<String, String>) ds1.getValue();
                            Product product = new Product(hashMap1.get("code"), hashMap1.get("nameEnglish"), hashMap1.get("nameGerman"),
                                    hashMap1.get("brand"), hashMap1.get("mainCategory"), hashMap1.get("subCategory"), hashMap1.get("barcode"),
                                    hashMap1.get("costSalon"), hashMap1.get("costWeb"), hashMap1.get("quantity"), hashMap1.get("picture"));

                            String bought = hashMap1.get("boughtQuantity");
                            arrayListP.add(product);
                            arrayListQ.add(bought);
                        }

                        Order order = new Order(arrayListP, arrayListQ, hashMap.get("orderNumber"), hashMap.get("date"), hashMap.get("info"));

                        allOrders.add(order);
                        allOrderNumbers.add(order.getOrderNumber());
                        allOrderCurrentSituations.add(order.getCurrentSituation());
                        allOrderDates.add(order.getDate());

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setAdapters() {

        if(allOrders.size() != 0){ //if we have orders do that
            historyAdapter = new HistoryAdapter(allOrderNumbers, allOrderDates, allOrderCurrentSituations, this);
            cartAdapter = new CartAdapter(allCartNames, allCartPrices, allCartQuantities, allCartPictures, this);

            listView.setAdapter(historyAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    createArrayListsForSecondAdapter(allOrders.get(position).getProducts(), allOrders.get(position));
                    if(listView.getAdapter().equals(historyAdapter)){
                        listView.setAdapter(cartAdapter);
                    }
                    else if(listView.getAdapter().equals(cartAdapter)){
                        Intent toShow = new Intent(HistoryActivity.this, ShowItemActivity.class);
                        toShow.putExtra("object", allCarts.get(position));
                        toShow.putExtra("quant", "");
                        startActivity(toShow);
                    }
                }
            });

            //clickListener yap ve bu tum siparislerden secili olana gecsin
            //onbackclicked yap tam tersi olsun
            //https://www.codeproject.com/Questions/491823/Read-2fWriteplusCSVplusinplusplusAndroid csv updat in de kullanilabilir
        }

        else {
            listView.setVisibility(View.GONE);
            header.setText("Keine Bestellungen bisher...");
        }
    }
    @Override
    public void onBackPressed() {
        if (cartAdapter != null && listView.getAdapter().equals(cartAdapter)) {
            listView.setAdapter(historyAdapter);
            allCarts.clear();
            allCartNames.clear();
            allCartPrices.clear();
            allCartQuantities.clear();
            allCartPictures.clear();
            cartAdapter.notifyDataSetChanged();
        } else {
            super.onBackPressed();
        }
    }

    //choose one order and get its infos about products
    public void createArrayListsForSecondAdapter(ArrayList<Product> products , Order order){
        Bitmap picture = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.cosmo);
        for(int i = 0; i < products.size(); i++){
            Log.d("anan", products.get(i).toString());
            allCarts.add(products.get(i));
            allCartNames.add(products.get(i).getNameGerman());
            allCartPrices.add(products.get(i).getCostWeb());
            allCartQuantities.add(order.getProductQuantities().get(i));
            allCartPictures.add(picture);
        }
    }
}
