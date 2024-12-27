package com.test.a7ara.inventory_insertion;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.test.a7ara.diffs.DiffListAdapter;
import com.test.a7ara.diffs.DiffListItemClick;
import com.test.a7ara.diffs.DiffListRow;
import com.test.a7ara.diffs.DiffsList;
import com.test.a7ara.diffs.DiffsListInsertDialog;

import java.util.ArrayList;
import java.util.Locale;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class InsertList extends AppCompatActivity implements InsertListItemClick {

    RecyclerView recyclerView;
    ArrayList<InsertListRow> Items;
    DatabaseHelper DB;
    InsertListAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_insert_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Locale.setDefault(new Locale("ar"));

        DB = new DatabaseHelper(this);
        Items = new ArrayList<>();
        recyclerView = findViewById(R.id.insertrv);
        adapter = new InsertListAdapter(this,Items , this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        displaydata();

        new ItemTouchHelper(deleteCallback).attachToRecyclerView(recyclerView);
        new ItemTouchHelper(editCallback).attachToRecyclerView(recyclerView);

        swipeRefreshLayout = findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            finish();
            Intent intent = new Intent(this, InsertList.class);
            startActivity(intent);
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void displaydata() {
        Cursor cursor = DB.getAllInserts();
        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
        if(cursor.getCount()==0){
            Toast.makeText(InsertList.this, "nothing to show", Toast.LENGTH_LONG).show();
            return;
        }
        else{
            while (cursor.moveToNext()){
                Cursor nestedcursor = DB.getInsertLogs(cursor.getString(0));
                String nested = "";
                while (nestedcursor.moveToNext()){
                    nested = nested + " | " + "TYPE:" + nestedcursor.getString(0)
                            + " ID:" + nestedcursor.getString(1)
                            + " COUNT:" + nestedcursor.getString(2);
                }

                Items.add(new InsertListRow(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        Double.valueOf(cursor.getString(3)),
                        Double.valueOf(cursor.getString(4)),
                        Double.valueOf(cursor.getString(5)),
                        cursor.getString(6),
                        nested));
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

            new android.app.AlertDialog.Builder(InsertList.this)
                    .setTitle("هل أنت متأكد؟ سيتم حذف الإدخال, لا يمكن التراجع عن الحذف")
                    .setPositiveButton("حذف", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String deleted = DB.deleteInsertion(Items.get(position).getID());
                            Toast.makeText(InsertList.this,deleted,Toast.LENGTH_LONG).show();
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
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(InsertList.this , R.color.Red))
                    .addSwipeLeftActionIcon(R.drawable.icon_delete_white)
                    .addSwipeLeftLabel("حذف")
                    .setSwipeLeftLabelTypeface(ResourcesCompat.getFont(InsertList.this, R.font.rubik_bold))
                    .setSwipeLeftLabelColor(ContextCompat.getColor(InsertList.this , R.color.white))
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
//            DiffsListInsertDialog customDialog = new InsertListInsertDialog(Items.get(position),position);
//            customDialog.show(getSupportFragmentManager(),"edit diff");
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(InsertList.this , R.color.BlueText))
                    .addSwipeRightActionIcon(R.drawable.icon_edit)
                    .addSwipeRightCornerRadius(3 ,5)
                    .addSwipeRightLabel("تعديل")
                    .setSwipeRightLabelTypeface(ResourcesCompat.getFont(InsertList.this, R.font.rubik_bold))
                    .setSwipeRightLabelColor(ContextCompat.getColor(InsertList.this , R.color.white))
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
}