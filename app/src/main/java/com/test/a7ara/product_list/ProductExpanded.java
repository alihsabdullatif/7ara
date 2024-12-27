package com.test.a7ara.product_list;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.test.a7ara.DatabaseHelper;
import com.test.a7ara.R;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductExpanded extends AppCompatActivity {
    String ID ;
    DatabaseHelper DB;
    TextView Procuct_name,Code,Category,Inserts,InsertsValue,Inventory,InventoryValue,Sales,SalesValue,Diffs,DiffsValue,LatestPriceWholesale,LatestPriceRetail,Profit;
    NumberFormat fr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fr = NumberFormat.getInstance(Locale.ENGLISH);
        fr.setMaximumFractionDigits(2);

        ID = getIntent().getStringExtra("ID");

        DB = new DatabaseHelper(this);
        Procuct_name = findViewById(R.id.name_value);
        Code = findViewById(R.id.code_value);
        Inserts = findViewById(R.id.inserts_amount);
        InsertsValue = findViewById(R.id.inserts_value_value);
        Inventory = findViewById(R.id.inventory_amount);
        InventoryValue = findViewById(R.id.inventory_value);
        Sales = findViewById(R.id.sale_amount);
        SalesValue = findViewById(R.id.sale_value);
        Diffs = findViewById(R.id.diffs_amount);
        DiffsValue = findViewById(R.id.diffs_value);
        LatestPriceWholesale = findViewById(R.id.latest_price_value);
        LatestPriceRetail = findViewById(R.id.latest_price_retail_value);
        Profit = findViewById(R.id.profit_value);

        displaydata(ID);
    }

    public void copycode(View view){
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Code",Code.getText().toString());
        clipboardManager.setPrimaryClip(clipData);
    }

    private void displaydata(String id) {
        Cursor cursor = DB.getProductfromID(id);
        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
        if(cursor.getCount()==0){
            Toast.makeText(ProductExpanded.this, "nothing to show", Toast.LENGTH_LONG).show();
            return;
        }
        else{
            Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
            if (cursor.moveToFirst()){
                do{
                    Procuct_name.setText(cursor.getString(0));
                    Code.setText(cursor.getString(1));
                    Double last_price_wholesale = cursor.getString(3)==null?0.0:Double.valueOf(cursor.getString(3));
                    Double last_price_retail = cursor.getString(4)==null?0.0:Double.valueOf(cursor.getString(4));
                    Double wholesale_sales_sum = cursor.getString(5)==null?0.0:Double.valueOf(cursor.getString(5));
                    Double retail_sales_sum = cursor.getString(6)==null?0.0:Double.valueOf(cursor.getString(6));
                    Double inserts_count = cursor.getString(7)==null?0.0:Double.valueOf(cursor.getString(7));
                    Double inserts_value_wholesale = cursor.getString(8)==null?0.0:Double.valueOf(cursor.getString(8));
                    Double sales_count = cursor.getString(9)==null?0.0:Double.valueOf(cursor.getString(9));
                    Double diffs_count = cursor.getString(10)==null?0.0:Double.valueOf(cursor.getString(10));
                    Double diffs_value_wholesale = cursor.getString(11)==null?0.0:Double.valueOf(cursor.getString(11));

                    LatestPriceWholesale.setText(fr.format(last_price_wholesale));
                    LatestPriceRetail.setText(fr.format(last_price_retail));
                    Profit.setText(fr.format(retail_sales_sum-wholesale_sales_sum));
                    Inserts.setText(fr.format(inserts_count));
                    InsertsValue.setText(fr.format(inserts_value_wholesale));
                    Sales.setText(fr.format(sales_count));
                    SalesValue.setText(fr.format(wholesale_sales_sum));
                    Diffs.setText(fr.format(diffs_count));
                    DiffsValue.setText(fr.format(diffs_value_wholesale));
                    Inventory.setText(fr.format(inserts_count-sales_count-diffs_count));
                    InventoryValue.setText(fr.format(inserts_value_wholesale-wholesale_sales_sum-diffs_value_wholesale));
                }while(cursor.moveToNext());
            }
            cursor.close();
            }
    }

}