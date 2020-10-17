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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sarpe.cosmoswiss.Adapters.CartAdapter;
import com.example.sarpe.cosmoswiss.Adapters.ProductAdapter;
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

public class CartActivity extends AppCompatActivity {

    Button buyButton;

    ListView listView;

    TextView totalPrice;
    TextView gesamt;
    TextView header;

    //Arraylists containing all datas for products whwich should be shown in listview
    ArrayList<Product> allCart;
    private ArrayList<String> allCartNames;
    private ArrayList<String> allCartPrices;
    private ArrayList<String> allCartQuantities;
    private ArrayList<Bitmap> allCartPictures;

    Product toAdd;

    String quantity;

    FirebaseDatabase database;
    DatabaseReference myRef;

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    Bitmap picture;

    String decide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Intent intent = getIntent();
        decide = intent.getStringExtra("info"); //used to determine from which activity we are coming from
        toAdd = (Product) intent.getSerializableExtra("chosen"); //Product, which should be added to the cart
        quantity = intent.getStringExtra("chosenQ"); //How much has been bought

        buyButton = findViewById(R.id.buyButton);

        totalPrice = findViewById(R.id.totalCostTextView);
        gesamt = findViewById(R.id.gesamtTextView2);
        header = findViewById(R.id.warenKorbTextView);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        picture = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.cosmo); //default picture for all
        listView = findViewById(R.id.cartListView);
        allCart = new ArrayList<Product>();
        allCartNames = new ArrayList<String>();
        allCartPrices = new ArrayList<String>();
        allCartQuantities = new ArrayList<String>();
        allCartPictures = new ArrayList<Bitmap>();

        if(decide.equals("change")){
            //If we want to change the quantitz of the product we come from change activity and we override the quantity in dsatabase
            myRef.child("Users").child(currentUser.getEmail().replace(".","_")).child("Cart").child(toAdd.getNameGerman()).child("boughtQuantity").setValue(quantity);
        }

        //Read the data from database
        readFromDB();

        //Wait a little bit until we are done with reading from DB to see if the cart is empty or full and act accordingly
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setAdapters();
                //after setting adapters calculate the total amount without any changes
                totalPrice.setText("CHF " + calculateTotal());

            }
        }, 2000);

        //https://www.android-examples.com/remove-selected-listview-item-in-android-on-long-click-listener/
        //https://stackoverflow.com/questions/8369640/listview-setitemchecked-only-works-with-standard-arrayadapter-does-not-work-w
        //to checkable items

    }

    private void readFromDB() {
        //Create the actual DB reference and than try to read the acual reference
        final DatabaseReference newRef = myRef.child("Users").child(currentUser.getEmail().replace(".","_")).child("Cart");
        newRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Iterate over all the children of an objects, its attributes, and save them in a java object
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    //Use hashmap to map the info from DB to Java object
                    final HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                    //Write the neccesary info from DB to java object
                    Product product = new Product(hashMap.get("code"), hashMap.get("nameEnglish"), hashMap.get("nameGerman"),
                            hashMap.get("brand"), hashMap.get("mainCategory"), hashMap.get("subCategory"), hashMap.get("barcode"),
                            hashMap.get("costSalon"), hashMap.get("costWeb"), hashMap.get("quantity"), hashMap.get("picture"));
                    String bought = hashMap.get("boughtQuantity");

                    //Add neccesary items to the ArrayLists
                    allCart.add(product);
                    allCartNames.add(hashMap.get("nameGerman"));
                    allCartPrices.add(hashMap.get("costWeb"));
                    allCartQuantities.add(bought);
                    allCartPictures.add(picture); //sey olunca duzelt

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setAdapters(){
        //If we have some item in cart than set the listviews as normal
        if (allCart.size() != 0){
            final CartAdapter adapter = new CartAdapter(allCartNames, allCartPrices, allCartQuantities, allCartPictures, this);

            buyButton.setVisibility(View.VISIBLE);
            totalPrice.setVisibility(View.VISIBLE);
            gesamt.setVisibility(View.VISIBLE);
            header.setText("Mein Warenkorb");

            listView.setAdapter(adapter);

            //All deleting and selecting can be done with alert bar

            //If we click on item we want to change the quantity so go to that activity
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent toChangeItem = new Intent(getApplicationContext(), ChangeItemActivity.class);
                    //send neccesary things
                    toChangeItem.putExtra("object", allCart.get(position));
                    toChangeItem.putExtra("quant", allCartQuantities.get(position));
                    startActivity(toChangeItem);
                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    //if we click long remove
                    //delete from database
                    final DatabaseReference newRef = myRef.child("Users").child(currentUser.getEmail().replace(".","_")).child("Cart").child(allCartNames.get(position));
                    newRef.child("barcode").removeValue();
                    newRef.child("boughtQuantity").removeValue();
                    newRef.child("brand").removeValue();
                    newRef.child("code").removeValue();
                    newRef.child("costSalon").removeValue();
                    newRef.child("costWeb").removeValue();
                    newRef.child("mainCategory").removeValue();
                    newRef.child("subCategory").removeValue();
                    newRef.child("nameEnglish").removeValue();
                    newRef.child("nameGerman").removeValue();
                    newRef.child("picture").removeValue();
                    newRef.child("quantity").removeValue();


                    allCart.remove(position);
                    allCartNames.remove(position);
                    allCartPrices.remove(position);
                    allCartQuantities.remove(position);
                    allCartPictures.remove(position);

                    //If we delete everything we go back to main menu bur can also be no items etc...
                    if(allCart.size() == 0){
                        Intent goToMain = new Intent(CartActivity.this, MainActivity.class);
                        startActivity(goToMain);
                    }
                    //If we stil have something in cart after we delete it adapters should see it
                    else adapter.notifyDataSetChanged();

                    //We always calculate how much would a customer pay after the deletion
                    totalPrice.setText("CHF " + calculateTotal());

                    //Notify user that item has been deleted
                    Toast.makeText(getApplicationContext(), "Item Deleted", Toast.LENGTH_LONG).show();

                    return true;
                }
            });

        }

        else{
            listView.setVisibility(View.GONE);
            header.setText("Ihr Warenkorb ist leer");
        }
    }



    //ListView icin Adapter ozel class i vs
    //degistirme algoritmasi

    //implement buttons

    public  void buy(View view){
        //You can also add like an alert bar to confirm the order
        Intent toHistory = new Intent(getApplicationContext(), HistoryActivity.class);

        //For orders we need some informaton and they are provided here
        String info = "Bitte bezahlen sie";
        addOrderToDb(info, createDate(), createOrderNumberString());

        //Send all products in DB and additional information
        toHistory.putExtra("allitemsinorder", allCart);
        toHistory.putExtra("objectQuantity", allCartQuantities);
        toHistory.putExtra("info", "add");
        startActivity(toHistory);
    }

    private String createOrderNumberString() {
        //Create some random number can be done better somehow with dates etc
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder("CS");
        for(int i = 0; i < 10; ++i) {
            int n = random.nextInt(10);
            stringBuilder.append(n);
        }
        return  stringBuilder.toString();
    }

    private String createDate() {
        //Create the date on which the items where bought
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        return  formattedDate;
    }

    private void addOrderToDb(final String info, final String date, final String orderNumber) {
        //Again get the reference and add value listener
        final DatabaseReference newRef = myRef.child("Users").child(currentUser.getEmail().replace(".", "_"));
        newRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //If we dont have it on DB of course we never have the same order number
                if (!(dataSnapshot.child("Orders").child(orderNumber).exists())) {

                    //again create hashmap to map the DB with java
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("orderNumber", orderNumber);
                    userDataMap.put("date", date);
                    userDataMap.put("info", info);
                    newRef.child("Orders").child(orderNumber).updateChildren(userDataMap); //mapping here
                    for (int i = 0; i < allCart.size(); i++){
                        //Add all the items in order to the DB
                        if (!(dataSnapshot.child("Orders").child(orderNumber).child("Products").child(allCartNames.get(i)).exists())){
                            HashMap<String, Object> userDataMap1 = new HashMap<>();
                            userDataMap1.put("code", allCart.get(i).getCode());
                            userDataMap1.put("nameEnglish", allCart.get(i).getNameEnglish());
                            userDataMap1.put("nameGerman", allCart.get(i).getNameGerman());
                            userDataMap1.put("brand", allCart.get(i).getBrand());
                            userDataMap1.put("mainCategory", allCart.get(i).getMainCategory());
                            userDataMap1.put("subCategory", allCart.get(i).getSubCategory());
                            userDataMap1.put("barcode", allCart.get(i).getBarcode());
                            userDataMap1.put("costSalon", allCart.get(i).getCostSalon());
                            userDataMap1.put("costWeb", allCart.get(i).getCostWeb());
                            userDataMap1.put("quantity", allCart.get(i).getQuantity());
                            userDataMap1.put("picture", allCart.get(i).getPicture());
                            userDataMap1.put("boughtQuantity", allCartQuantities.get(i));
                            newRef.child("Orders").child(orderNumber).child("Products").child(allCartNames.get(i)).updateChildren(userDataMap1);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Method to calculate total amount to pay
    public double calculateTotal(){
        double total = 0;
        for(int i = 0; i < allCart.size(); i++){
            //get double value without chf
            String[] price = allCart.get(i).getCostWeb().split(" ");
            double priceDouble = Double.parseDouble(price[1]);
            double quantityDouble = Double.parseDouble(allCartQuantities.get(i)) ;

            total += (quantityDouble * priceDouble);
        }
        return  total;
    }


}
