package com.example.sarpe.cosmoswiss.Adapters;

//The adapter which enables user to see all of his/her orders

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

public class HistoryAdapter extends ArrayAdapter {
    private ArrayList<String> orderNumber;
    private ArrayList<String> orderDate;
    private ArrayList<String> orderCurrentSituation;

    private Activity context;

    //Get pictures from barcode
    public HistoryAdapter(ArrayList<String> orderNumber, ArrayList<String> orderDate, ArrayList<String> orderCurrentSituation, Activity context) {
        super(context, R.layout.history_adapter, orderNumber);
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.orderCurrentSituation = orderCurrentSituation;

        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //Declare and set the items in Layout file
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.history_adapter,null,true );

        TextView orderNumberText = customView.findViewById(R.id.orderNumberHistoryAdapter);
        TextView orderDateText = customView.findViewById(R.id.orderDateHistoryAdapter);
        TextView orderCSText = customView.findViewById(R.id.orderCurrentSituationHistoryAdapter);

        orderNumberText.setText(orderNumber.get(position));
        orderDateText.setText(orderDate.get(position));
        orderCSText.setText(orderCurrentSituation.get(position) );

        return customView;
    }

}

