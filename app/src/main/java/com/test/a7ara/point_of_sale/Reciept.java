package com.test.a7ara.point_of_sale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.test.a7ara.R;

import java.util.ArrayList;

public class Reciept extends AppCompatActivity  {

    ArrayList<PointOfSaleRow> Items;
    ListView listView;
    Reciept_adapter rec_adapter;
    TextView Total , TotalBeforeDiscount , Discount;
    Double DiscountAmount , TotalAfterDiscount;
    LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reciept);

        Bundle extras = getIntent().getExtras();
        Items = getIntent().getParcelableArrayListExtra("Items");
        DiscountAmount = extras.getDouble("DiscountAmount");


        rec_adapter = new Reciept_adapter(this,R.layout.row_reciept,Items);
        listView = findViewById(R.id.recieptlist);
        listView.setAdapter(rec_adapter);

        layoutInflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) layoutInflater.inflate(R.layout.header_reciept, listView,false);
        ViewGroup footer = (ViewGroup) layoutInflater.inflate(R.layout.footer_reciept, listView,false);
        TotalBeforeDiscount = footer.findViewById(R.id.reciept_total_before_discount);
        Total = footer.findViewById(R.id.reciept_total);
        Discount = footer.findViewById(R.id.reciept_discount);

        Discount.setText(String.format("%.0f",DiscountAmount));
        listView.addHeaderView(header);
        listView.addFooterView(footer);
        listView.setAdapter(rec_adapter);

        Double total = 0.0;
        for(int i = 0 ; i < Items.size() ; i++){
            total = total + Items.get(i).getSum();
        }

        TotalBeforeDiscount.setText(String.format("%.0f",total));
        TotalAfterDiscount = total - DiscountAmount;
        Total.setText(String.format("%.0f",TotalAfterDiscount));
    }
}
