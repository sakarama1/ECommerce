package com.example.sarpe.cosmoswiss;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.sarpe.cosmoswiss.Adapters.CategoryAdapter;
import com.example.sarpe.cosmoswiss.Adapters.ProductAdapter;
import com.example.sarpe.cosmoswiss.Classes.Category;
import com.example.sarpe.cosmoswiss.Classes.Product;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ShowListsActivity extends AppCompatActivity {

    ListView listView;
    SearchView searchView;

    private ArrayList<Product> allProducts;
    private ArrayList<Product> chosenProducts;
    private ArrayList<String> allProductNames;
    private ArrayList<String> allProductPrices;
    private ArrayList<Bitmap> allProductPictures;


    private ArrayList<Category> mainCategories;
    private ArrayList<String> mainCategoriesString;
    private ArrayList<String> subCategories;
    private ArrayList<String> brands;

    private CategoryAdapter categoryAdapter;
    private ProductAdapter chosenProductsAdapter; //coming from categories
    private CategoryAdapter brandAdapter;


    private String infoForAdapters;

    //get info and distinguish categories later
    //for loop yap itemleri allProducts in icinden at ya da yeni bir listeye kaydet
    //category icin kod


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lists);

        final Intent intent = getIntent();
        infoForAdapters = intent.getStringExtra("category");

        allProducts = new ArrayList<Product>();
        chosenProducts = new ArrayList<Product>();
        allProductNames = new ArrayList<String>();
        allProductPrices = new ArrayList<String>();
        allProductPictures = new ArrayList<Bitmap>();

        mainCategories = new ArrayList<Category>();
        mainCategoriesString = new ArrayList<String>();
        subCategories = new ArrayList<String>();
        brands = new ArrayList<String>();

        try {
            readData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        listView = findViewById(R.id.listView);
        searchView = findViewById(R.id.searchView);

        //just for test
        Bitmap picture = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.cosmo);

        //dummy arrays
//        ArrayList<Bitmap> dummypicturesMain = new ArrayList<Bitmap>();
//        for(int i = 0; i < mainCategories.size(); ++i){
//            dummypicturesMain.add(picture);
//        }

        ArrayList<Bitmap> dummypicturesSub = new ArrayList<Bitmap>();
        for(int i = 0; i < subCategories.size(); ++i){
            dummypicturesSub.add(picture);
        }
        //////////////

        ArrayList<Bitmap> dummypicturesBrands = new ArrayList<Bitmap>();
        for(int i = 0; i < brands.size(); ++i){
            dummypicturesBrands.add(picture);
        }
        //////////////

        categoryAdapter = new CategoryAdapter(subCategories, dummypicturesSub, this);
        brandAdapter = new CategoryAdapter(brands, dummypicturesBrands, this);
        chosenProductsAdapter = new ProductAdapter(allProductNames, allProductPrices, allProductPictures, this);

        //distinguish where are we coming here from
        //Search

        if(infoForAdapters.equals("all")){
            listView.setAdapter(chosenProductsAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent toShowItem = new Intent(getApplicationContext(), ShowItemActivity.class);
                    //send neccesary things
                    toShowItem.putExtra("object", allProducts.get(position));
                    startActivity(toShowItem);
                }
            });
        }
        else if (infoForAdapters.equals("marken")){
            listView.setAdapter(brandAdapter);
            listenerForSearchView(brandAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String brand = new String();
                    if(listView.getAdapter().equals(brandAdapter)){
                        brand = brands.get(position);
                        putItemsAccordingToBrands(brand, allProducts);
                        chosenProductsAdapter.notifyDataSetChanged();
                        listView.setAdapter(chosenProductsAdapter);
                        listenerForSearchView(chosenProductsAdapter);

                    }

                    else if(listView.getAdapter().equals(chosenProductsAdapter)){
                        Intent toShowItem = new Intent(getApplicationContext(), ShowItemActivity.class);
                        //send neccesary things
                        toShowItem.putExtra("object", allProducts.get(position));
                        startActivity(toShowItem);
                    }
                }
            });
        }

        else{
            listView.setAdapter(categoryAdapter);
            listenerForSearchView(categoryAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String sub = new String();
                    if(listView.getAdapter().equals(categoryAdapter)){
                        sub = subCategories.get(position);
                        putItemsInRightCategories(infoForAdapters, sub, allProducts);
                        chosenProductsAdapter.notifyDataSetChanged();
                        listView.setAdapter(chosenProductsAdapter);
                        listenerForSearchView(chosenProductsAdapter);
                    }

                    else if(listView.getAdapter().equals(chosenProductsAdapter)){
                        Intent toShowItem = new Intent(getApplicationContext(), ShowItemActivity.class);
                        //send neccesary things
                        toShowItem.putExtra("object", chosenProducts.get(position));
                        toShowItem.putExtra("quant", "");
                        startActivity(toShowItem);
                    }
                }
            });
        }

    }

    @Override
    //adapter a gore degistir
    public void onBackPressed() {
        if(infoForAdapters.equals("all")){
            super.onBackPressed();
        }

        else if(infoForAdapters.equals("marken")){
            if (listView.getAdapter().equals(chosenProductsAdapter)) {
                listView.setAdapter(brandAdapter);
                listenerForSearchView(brandAdapter);
            } else {
                super.onBackPressed();
            }
        }

        else{
            if (listView.getAdapter().equals(chosenProductsAdapter)) {
                listView.setAdapter(categoryAdapter);
                listenerForSearchView(categoryAdapter);
            } else {
                super.onBackPressed();
            }
        }

    }

    //read data from csv
    private void readData() throws IOException {

        //Read the CSV file into an InputStreamReader
        InputStreamReader isr = new InputStreamReader(getResources().openRawResource(R.raw.testexcel));

        //Read the InputStreamReader created by a CSV file
        CSVReader reader = new CSVReader(isr);

        //We will be reading that object
        String[] nextLine;

        //read the titles and get rid of them
        reader.readNext();

        //Put in a try catch block to avoid failure if something happens
        try{
            //read if there is something to read
            while ((nextLine = reader.readNext()) != null ){

                //read and create the product objects with constructor
                Product product = new Product(nextLine[0], nextLine[1], nextLine[2], nextLine[3], nextLine[4],
                        nextLine[5], nextLine[6], nextLine[7], nextLine[9], nextLine[10], "cosmo");

                //implementation 'com.opencsv:opencsv:3.9' add to build
            }
        }

        catch (IOException e){
            e.printStackTrace();
        }
    }

    //we want to create lists of categories
    //you can make it more generalized by making passing arrayList and making this arrayLists as dummy variable
    private void createCategories(Product product){
        if(!mainCategoriesString.contains(product.getMainCategory())) mainCategoriesString.add(product.getMainCategory());
        if(!subCategories.contains(product.getSubCategory())) subCategories.add(product.getSubCategory());
        if(!brands.contains(product.getBrand())) brands.add(product.getBrand());

        //implement, if you have main category than add sub category just another if but not now
    }

    //Checks if there is a picture for the item and converts the string in bitmap
    private Bitmap pictureControl(String produkt){
        Bitmap picture = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.cosmo);
        return picture;
    }

    //selects items after you choose categories
    private void putItemsInRightCategories(String main, String sub, ArrayList<Product> arrayList){
        for (int i= 0; i < allProducts.size(); ++i){
            if(arrayList.get(i).getMainCategory().equals(main) && arrayList.get(i).getSubCategory().equals(sub)){
                chosenProducts.add(arrayList.get(i));
                allProductNames.add(arrayList.get(i).getNameGerman());
                allProductPrices.add(arrayList.get(i).getCostWeb());
                allProductPictures.add(pictureControl(arrayList.get(i).getPicture()));
            }
        }
    }

    private void putItemsAccordingToBrands(String brand, ArrayList<Product> arrayList){
        for (int i= 0; i < allProducts.size(); ++i){
            if(arrayList.get(i).getBrand().equals(brand)){
                allProductNames.add(arrayList.get(i).getNameGerman());
                allProductPrices.add(arrayList.get(i).getCostWeb());
                allProductPictures.add(pictureControl(arrayList.get(i).getPicture()));
            }
        }
    }

    private void listenerForSearchView(final ArrayAdapter arrayAdapter){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}


