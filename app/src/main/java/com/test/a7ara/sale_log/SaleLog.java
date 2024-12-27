package com.test.a7ara.sale_log;

import static android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE;
import static com.google.android.material.internal.ContextUtils.getActivity;
import static java.lang.Double.valueOf;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.test.a7ara.DatabaseHelper;
import com.test.a7ara.R;
import com.test.a7ara.databinding.ActivityPointOfSaleLogBinding;
import com.test.a7ara.point_of_sale.PointOfSale;
import com.test.a7ara.point_of_sale.PointOfSaleDiscountDialog;
import com.test.a7ara.point_of_sale.Reciept;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class SaleLog extends AppCompatActivity implements SaleLogItemClick, SaleLogFilterDialog.SaleLogFilterDialogInterface{

//    private ActivityPointOfSaleLogBinding binding;
    DatabaseHelper DB;
    ArrayList<SaleLogRow> Items;
    RecyclerView recyclerView;
    SaleLogAdapter adapter;
    Boolean Grouped;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_point_of_sale_log);

        Locale.setDefault(new Locale("ar"));

        DB = new DatabaseHelper(this);
        Items = new ArrayList<>();
        recyclerView = findViewById(R.id.sales_rv);
        adapter = new SaleLogAdapter(this,Items , this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));


        Grouped = true;
        displaydata(Grouped);

        new ItemTouchHelper(deleteCallback).attachToRecyclerView(recyclerView);
        new ItemTouchHelper(editCallback).attachToRecyclerView(recyclerView);

        swipeRefreshLayout = findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            finish();
            Intent intent = new Intent(this, SaleLog.class);
            startActivity(intent);
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void displaydata(boolean gr) {
        Cursor cursor = DB.getAllSales(gr);
        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
        if(cursor.getCount()==0){
            Toast.makeText(SaleLog.this, "nothing to show", Toast.LENGTH_LONG).show();
            return;
        }
        else{
            while (cursor.moveToNext()){
                ArrayList<SaleLogRowExpandable> nestedList = new ArrayList<SaleLogRowExpandable>();
                if(gr){
                    Cursor nestedCursor = DB.getSaleItemsFromSaleID(cursor.getString(0));
//                    ArrayList<SaleLogRowExpandable> nestedList = new ArrayList<SaleLogRowExpandable>();
                    if(nestedCursor.getCount()==0){
                        Toast.makeText(SaleLog.this, "no items", Toast.LENGTH_LONG).show();

                    }
                    else{
                        while (nestedCursor.moveToNext()) {
                            nestedList.add(new SaleLogRowExpandable(nestedCursor.getString(0),
                                    Double.valueOf(nestedCursor.getString(1)),
                                    Double.valueOf(nestedCursor.getString(2)),
                                    Double.valueOf(nestedCursor.getString(3))));
                        }
                    }
                }

                String title = cursor.getString(1);
                if(gr){title = cursor.getString(1) + " منتج";}
                Double retail_amount = valueOf(cursor.getString(3));
                Double disc_total = valueOf(cursor.getString(6));
                Double retail_total = valueOf(cursor.getString(7));
                Double disc = gr? disc_total : (disc_total/retail_total)*retail_amount;
                Double count = cursor.getString(5) == null ? 0 : Double.valueOf(cursor.getString(5));
                String creationdate = cursor.getString(2);

                Items.add(new SaleLogRow(cursor.getString(0),
                        title,
                        creationdate,
                        retail_amount,
                        valueOf(cursor.getString(4)),
                        valueOf(cursor.getString(3)) - valueOf(cursor.getString(4)) - disc,
                        count,
                        disc,
                        nestedList));
            }
        }
    }

    @Override
    public void onItemClick(int pos) {

    }

    public void filter(View view){
        SaleLogFilterDialog customDialog = new SaleLogFilterDialog(Grouped);
        customDialog.show(getSupportFragmentManager(),"Filter List");
    }

    @Override
    public void applyFilter(Boolean grouped) {
        Items = new ArrayList<SaleLogRow>();
        Grouped = grouped;
        displaydata(Grouped);
        adapter = new SaleLogAdapter(this,Items , this);
        recyclerView.setAdapter(adapter);
    }

    ItemTouchHelper.SimpleCallback deleteCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getBindingAdapterPosition();


            new android.app.AlertDialog.Builder(SaleLog.this)
                    .setTitle("هل أنت متأكد؟ لا يمكن التراجع عن الحذف")
                    .setPositiveButton("حذف", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String deleted = DB.deleteSale(Items.get(position).getID() , Grouped);
                            Toast.makeText(SaleLog.this,deleted,Toast.LENGTH_LONG).show();
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
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(SaleLog.this , R.color.Red))
                    .addSwipeLeftActionIcon(R.drawable.icon_delete_white)
                    .addSwipeLeftLabel("حذف")
                    .setSwipeLeftLabelTypeface(ResourcesCompat.getFont(SaleLog.this, R.font.rubik_bold))
                    .setSwipeLeftLabelColor(ContextCompat.getColor(SaleLog.this , R.color.white))
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
            int position = viewHolder.getBindingAdapterPosition();
            Intent intent = new Intent(SaleLog.this, PointOfSale.class);
            intent.putExtra("saleid", Items.get(position).getID());
            startActivityForResult(intent,1);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(SaleLog.this , R.color.BlueText))
                    .addSwipeRightActionIcon(R.drawable.icon_edit)
                    .addSwipeRightCornerRadius(3 ,5)
                    .addSwipeRightLabel("تعديل")
                    .setSwipeRightLabelTypeface(ResourcesCompat.getFont(SaleLog.this, R.font.rubik_bold))
                    .setSwipeRightLabelColor(ContextCompat.getColor(SaleLog.this , R.color.white))
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent refresh = new Intent(this, SaleLog.class);
        startActivity(refresh);
        this.finish();
    }
}