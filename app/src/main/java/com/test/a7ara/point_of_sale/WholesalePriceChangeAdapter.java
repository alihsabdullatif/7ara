package com.test.a7ara.point_of_sale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.test.a7ara.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class WholesalePriceChangeAdapter extends ArrayAdapter<InventoryWholesalePrices> {

    private LayoutInflater inflater;
    private ArrayList<InventoryWholesalePrices> Items;
    private int viewResourceID;

    public WholesalePriceChangeAdapter(Context context , int textViewResourceId , ArrayList<InventoryWholesalePrices> items){
        super(context,textViewResourceId,items);
        this.Items = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewResourceID = textViewResourceId;
    }

    public View getView (int pos , View convertView , ViewGroup parents){
        convertView = inflater.inflate(viewResourceID,null);
        InventoryWholesalePrices row = Items.get(pos);
        if(row!=null){
            TextView name = convertView.findViewById(R.id.info);
            NumberFormat fr = NumberFormat.getInstance(Locale.ENGLISH);
            fr.setMaximumFractionDigits(0);
            name.setText((row.getRemainingCount()==null?"":fr.format(row.getRemainingCount())) + " بسعر جملة " + (row.getPrice()==null?"":fr.format(row.getPrice()))  + " إدخال " + (row.getDateTime().equals(null)?"":row.getDateTime()));
        }
        return convertView;
    }

}
