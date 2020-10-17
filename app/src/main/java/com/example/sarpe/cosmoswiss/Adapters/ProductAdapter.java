package com.example.sarpe.cosmoswiss.Adapters;

//Class to show the products in ShowItemsActivity

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sarpe.cosmoswiss.R;

import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter {

    private ArrayList<String> produktName;
    private ArrayList<String> produktPriceWeb;
    private ArrayList<Bitmap> produktPicture;
    private Activity context;

    //Get pictures from barcode
    public ProductAdapter(ArrayList<String> produktName, ArrayList<String> produktPriceWeb, ArrayList<Bitmap> produktBarcode, Activity context) {
        super(context, R.layout.product_adapter, produktName);
        this.produktName = produktName;
        this.produktPriceWeb = produktPriceWeb;
        this.produktPicture = produktBarcode;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.product_adapter,null,true );

        TextView produktNameText = customView.findViewById(R.id.itemNameCustomView);
        TextView produktPriceWebText = customView.findViewById(R.id.itemPriceCustomView);
        ImageView produktImage = customView.findViewById(R.id.itemImageView);


        produktNameText.setText(produktName.get(position));
        produktPriceWebText.setText(produktPriceWeb.get(position));
        produktImage.setImageBitmap(produktPicture.get(position));

        return customView;
    }

    public void setArrayLists(ArrayList<String> produktName, ArrayList<String> produktPriceWeb, ArrayList<Bitmap> produktBarcode){
        this.produktName = produktName;
        this.produktPriceWeb = produktPriceWeb;
        this.produktPicture = produktBarcode;
    }
}
