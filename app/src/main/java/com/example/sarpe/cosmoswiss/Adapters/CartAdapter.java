package com.example.sarpe.cosmoswiss.Adapters;

//Class used to adapt listview in CartActivity

import android.app.Activity;
import android.graphics.Bitmap;
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

public class CartAdapter extends ArrayAdapter {
    //We need 4 different ArrayLists to show the items
    private ArrayList<String> produktName;
    private ArrayList<String> produktPriceWeb;
    private ArrayList<Bitmap> produktPicture;
    private ArrayList<String> produktQuantity;

    private Activity context;

    //Get pictures from barcode
    public CartAdapter(ArrayList<String> produktName, ArrayList<String> produktPriceWeb, ArrayList<String> produktQuantity, ArrayList<Bitmap> produktBarcode, Activity context) {
        super(context, R.layout.product_adapter, produktName);
        this.produktName = produktName;
        this.produktPriceWeb = produktPriceWeb;
        this.produktQuantity = produktQuantity;
        this.produktPicture = produktBarcode;

        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //Making layout here, writing everything on the Layout items that we have implemented
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.cart_adapter,null,true );

        //Declare here
        TextView produktNameText = customView.findViewById(R.id.itemNameCardAdapter);
        TextView produktPriceWebText = customView.findViewById(R.id.itemPriceCardAdapter);
        TextView produktQuantityText = customView.findViewById(R.id.itemQuantityCardAdapter);
        ImageView produktImage = customView.findViewById(R.id.itemCardAdapterImageView);

        //Set the items
        produktNameText.setText(produktName.get(position));
        produktPriceWebText.setText(produktPriceWeb.get(position));
        produktQuantityText.setText("x " + produktQuantity.get(position) ); //X How much we want ot buy
        produktImage.setImageBitmap(produktPicture.get(position));

        return customView;
    }

}
