package com.test.a7ara.product_list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.test.a7ara.DatabaseHelper;
import com.test.a7ara.R;
import com.test.a7ara.point_of_sale.PointOfSale;
import com.test.a7ara.sale_log.SaleLog;

import java.util.ArrayList;
import java.util.Locale;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ProductList extends AppCompatActivity implements ProductListItemClick {

    RecyclerView recyclerView;
    ArrayList<ProductListRow> Items;
    DatabaseHelper DB;
    ProductListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products);

        Locale.setDefault(new Locale("ar"));

        DB = new DatabaseHelper(this);
        Items = new ArrayList<>();
        recyclerView = findViewById(R.id.products_rv);
        adapter = new ProductListAdapter(this,Items , this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        new ItemTouchHelper(deleteCallback).attachToRecyclerView(recyclerView);
        new ItemTouchHelper(editCallback).attachToRecyclerView(recyclerView);

        displaydata();
    }

    private void displaydata() {
        Cursor cursor = DB.getAllProducts();
        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
        if(cursor.getCount()==0){
            Toast.makeText(ProductList.this, "nothing to show", Toast.LENGTH_LONG).show();
            return;
        }
        else{
            while (cursor.moveToNext()){
                Double inserts = cursor.getString(5)==null?0.0:Double.valueOf(cursor.getString(5));
                Double sales = cursor.getString(6)==null?0.0:Double.valueOf(cursor.getString(6));
                Double diffs = cursor.getString(7)==null?0.0:Double.valueOf(cursor.getString(7));
                Boolean byweighttemp = cursor.getString(8).equals("1")?true:false;
                Items.add(new ProductListRow(cursor.getString(0),cursor.getString(1),
                        cursor.getString(2),byweighttemp,cursor.getString(3),cursor.getString(4),sales,diffs,inserts));
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ProductList.this, ProductExpanded.class);
        intent.putExtra("ID",Items.get(position).getID());
        startActivity(intent);
    }

    ItemTouchHelper.SimpleCallback deleteCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getBindingAdapterPosition();


            new android.app.AlertDialog.Builder(ProductList.this)
                    .setTitle("هل أنت متأكد؟ سيتم حذف المنتج وجميع إدخالاته ومبيعاته, لا يمكن التراجع عن الحذف")
                    .setPositiveButton("حذف", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String deleted = DB.deleteProduct(Items.get(position).getID());
                            Toast.makeText(ProductList.this,deleted,Toast.LENGTH_LONG).show();
                            Items.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("عودة", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.notifyDataSetChanged();
                        }
                    })
                    .show();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(ProductList.this , R.color.Red))
                    .addSwipeLeftActionIcon(R.drawable.icon_delete_white)
                    .addSwipeLeftLabel("حذف")
                    .setSwipeLeftLabelTypeface(ResourcesCompat.getFont(ProductList.this, R.font.rubik_bold))
                    .setSwipeLeftLabelColor(ContextCompat.getColor(ProductList.this , R.color.white))
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    ItemTouchHelper.SimpleCallback editCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//            int position = viewHolder.getBindingAdapterPosition();
//            Intent intent = new Intent(ProductList.this, PointOfSale.class);
//            intent.putExtra("saleid", Items.get(position).getID());
//            startActivityForResult(intent,1);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(ProductList.this , R.color.BlueText))
                    .addSwipeRightActionIcon(R.drawable.icon_edit)
                    .addSwipeRightCornerRadius(3 ,5)
                    .addSwipeRightLabel("تعديل")
                    .setSwipeRightLabelTypeface(ResourcesCompat.getFont(ProductList.this, R.font.rubik_bold))
                    .setSwipeRightLabelColor(ContextCompat.getColor(ProductList.this , R.color.white))
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
}