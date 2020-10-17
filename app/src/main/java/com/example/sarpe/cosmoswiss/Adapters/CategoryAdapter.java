package com.example.sarpe.cosmoswiss.Adapters;

//Class to show the categories in ShowItemsActivity

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

import java.util.ArrayList;

public class CategoryAdapter extends ArrayAdapter {
    private final ArrayList<String> categoryName;
    private final ArrayList<Bitmap> categoryPicture;
    private final Activity context;

    //Get pictures from barcode
    public CategoryAdapter(ArrayList<String> categoryName, ArrayList<Bitmap> categoryPicture, Activity context) {
        super(context, R.layout.category_adapter, categoryName);
        this.categoryName = categoryName;
        this.categoryPicture = categoryPicture;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //Declare and set the layout items
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View categoryAdapter = layoutInflater.inflate(R.layout.category_adapter,null,true );

        TextView categoryNameText = categoryAdapter.findViewById(R.id.categoryNameAdapter);
        ImageView categoryImage = categoryAdapter.findViewById(R.id.categoryPictureAdapter);


        categoryNameText.setText(categoryName.get(position));
        categoryImage.setImageBitmap(categoryPicture.get(position));

        return categoryAdapter;
    }
}
