package com.test.a7ara.point_of_sale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.test.a7ara.R;

import java.util.ArrayList;

public class Reciept_adapter extends ArrayAdapter<PointOfSaleRow> {

    private LayoutInflater inflater;
    private ArrayList<PointOfSaleRow> Items;
    private int viewResourceID;

    public Reciept_adapter(Context context , int textViewResourceId , ArrayList<PointOfSaleRow> items){
        super(context,textViewResourceId,items);
        this.Items = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewResourceID = textViewResourceId;
    }

    public View getView (int pos , View convertView , ViewGroup parents){
        convertView = inflater.inflate(viewResourceID,null);
        PointOfSaleRow row = Items.get(pos);
        if(row!=null){
            TextView name = convertView.findViewById(R.id.reciept_product);
            TextView count = convertView.findViewById(R.id.reciept_count);
            TextView total = convertView.findViewById(R.id.reciept_total);

            name.setText(row.getName());
            count.setText(row.getCountWithPrefix());
            total.setText(String.format("%.0f",row.getSum()));
        }
        return convertView;
    }
}
