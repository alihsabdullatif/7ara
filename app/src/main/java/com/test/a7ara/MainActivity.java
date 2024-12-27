package com.test.a7ara;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.test.a7ara.diffs.DiffsList;
import com.test.a7ara.inventory_insertion.InsertList;
import com.test.a7ara.point_of_sale.PointOfSale;
import com.test.a7ara.product_insertion.InsertProduct;
import com.test.a7ara.inventory_insertion.InsertInventory;
import com.test.a7ara.product_list.ProductList;
import com.test.a7ara.sale_log.SaleLog;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDB;

    Button btn_insert,btn_insert_p,btn_view_p,btn_pos,btn_pos_log,btn_diff_log,btn_insert_log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Locale.setDefault(new Locale("ar"));

        setContentView(R.layout.activity_main);

        myDB = new DatabaseHelper(this);

        btn_insert_p= findViewById(R.id.insertac);
        btn_insert_p.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, InsertProduct.class);
            startActivity(intent);
        });

        btn_insert= findViewById(R.id.insertbtn);
        btn_insert.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, InsertInventory.class);
            startActivity(intent);
        });

        btn_view_p= findViewById(R.id.viewb);
        btn_view_p.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, ProductList.class);
            startActivity(intent);
        });

        btn_pos= findViewById(R.id.pos);
        btn_pos.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, PointOfSale.class);
            startActivity(intent);
        });

        btn_pos_log= findViewById(R.id.pos_log);
        btn_pos_log.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, SaleLog.class);
            startActivity(intent);
        });

        btn_diff_log= findViewById(R.id.diff_log);
        btn_diff_log.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, DiffsList.class);
            startActivity(intent);
        });

        btn_insert_log= findViewById(R.id.insert_log);
        btn_insert_log.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, InsertList.class);
            startActivity(intent);
        });

    }
}