package com.test.a7ara.diffs;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.test.a7ara.DatabaseHelper;
import com.test.a7ara.R;
import com.test.a7ara.point_of_sale.PointOfSaleDiscountDialog;
import com.test.a7ara.product_list.ProductList;
import com.test.a7ara.product_list.ProductListAdapter;
import com.test.a7ara.product_list.ProductListItemClick;
import com.test.a7ara.product_list.ProductListRow;
import com.test.a7ara.sale_log.SaleLog;

import java.util.ArrayList;
import java.util.Locale;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class DiffsList extends AppCompatActivity implements DiffListItemClick, DiffsListInsertDialog.DiffInsertDialogInterface{

    RecyclerView recyclerView;
    ArrayList<DiffListRow> Items;
    DatabaseHelper DB;
    DiffListAdapter adapter;
    FloatingActionButton addDiff;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_diffs_list);

        Locale.setDefault(new Locale("ar"));

        DB = new DatabaseHelper(this);
        Items = new ArrayList<>();
        recyclerView = findViewById(R.id.diffsrv);
        adapter = new DiffListAdapter(this,Items , this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        addDiff = findViewById(R.id.add_button);
        addDiff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiffsListInsertDialog customDialog = new DiffsListInsertDialog(null,null);
                customDialog.show(getSupportFragmentManager(),"add diff");
            }
        });

        displaydata();

        new ItemTouchHelper(deleteCallback).attachToRecyclerView(recyclerView);
        new ItemTouchHelper(editCallback).attachToRecyclerView(recyclerView);

        swipeRefreshLayout = findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            finish();
            Intent intent = new Intent(this, DiffsList.class);
            startActivity(intent);
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void displaydata() {
        Cursor cursor = DB.getAllDiffs();
        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
        if(cursor.getCount()==0){
            Toast.makeText(DiffsList.this, "nothing to show", Toast.LENGTH_LONG).show();
            return;
        }
        else{
            while (cursor.moveToNext()){
                Items.add(new DiffListRow(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2)==null?"":cursor.getString(2),
                        Double.valueOf(cursor.getString(3)),
                        cursor.getString(4),
                        Double.valueOf(cursor.getString(5)),
                        cursor.getString(6),
                        Integer.valueOf(cursor.getString(7))));
            }
        }
    }

    @Override
    public void onItemClick(int pos) {

    }

    ItemTouchHelper.SimpleCallback deleteCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getBindingAdapterPosition();
            adapter.notifyDataSetChanged();

            new android.app.AlertDialog.Builder(DiffsList.this)
                    .setTitle("هل أنت متأكد؟ سيتم حذف الفرق, لا يمكن التراجع عن الحذف")
                    .setPositiveButton("حذف", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String deleted = DB.deleteDiff(Items.get(position).getID(),Items.get(position).getLoss());
                            Toast.makeText(DiffsList.this,deleted,Toast.LENGTH_LONG).show();
                            Items.remove(position);
                        }
                    })
                    .setNegativeButton("عودة", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(DiffsList.this , R.color.Red))
                    .addSwipeLeftActionIcon(R.drawable.icon_delete_white)
                    .addSwipeLeftLabel("حذف")
                    .setSwipeLeftLabelTypeface(ResourcesCompat.getFont(DiffsList.this, R.font.rubik_bold))
                    .setSwipeLeftLabelColor(ContextCompat.getColor(DiffsList.this , R.color.white))
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
            DiffsListInsertDialog customDialog = new DiffsListInsertDialog(Items.get(position),position);
            customDialog.show(getSupportFragmentManager(),"edit diff");
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(DiffsList.this , R.color.BlueText))
                    .addSwipeRightActionIcon(R.drawable.icon_edit)
                    .addSwipeRightCornerRadius(3 ,5)
                    .addSwipeRightLabel("تعديل")
                    .setSwipeRightLabelTypeface(ResourcesCompat.getFont(DiffsList.this, R.font.rubik_bold))
                    .setSwipeRightLabelColor(ContextCompat.getColor(DiffsList.this , R.color.white))
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    @Override
    public void InsertDiff(String productID , Double diffCount , String diffType , String diffID, Integer loss) {
        if(diffID!=null){
            long updated = DB.updateDiff(diffCount,diffType,diffID,loss);
            Toast.makeText(DiffsList.this,String.valueOf(updated),Toast.LENGTH_LONG).show();
        }
        else{
            long inserted = DB.insertDiff(productID,diffCount,diffType,loss);
            Toast.makeText(DiffsList.this,String.valueOf(inserted),Toast.LENGTH_LONG).show();
        }
        Intent refresh = new Intent(this, DiffsList.class);
        startActivity(refresh);
        this.finish();
    }

}