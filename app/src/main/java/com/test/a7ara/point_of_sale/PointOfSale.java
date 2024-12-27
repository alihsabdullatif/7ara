package com.test.a7ara.point_of_sale;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.test.a7ara.BarCodeCaptureAct;
import com.test.a7ara.DatabaseHelper;
import com.test.a7ara.ProductAutoCompleteHelper;
import com.test.a7ara.R;
import com.test.a7ara.databinding.ActivityPointOfSaleBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class PointOfSale extends AppCompatActivity implements PointOfSaleItemClick, PointOfSaleDiscountDialog.DiscountDialogInterface,PointOfSaleCountDialog.CountDialogInterface {

    private ActivityPointOfSaleBinding binding;
    ArrayList<PointOfSalePage> Pages;
    ArrayList<ItemInventory> ItemInvs;
    Integer CurrentPage;
    PointOfSaleListAdapter adapter;
    String data , EditSaleID;
    DatabaseHelper DB;
    AutoCompleteTextView products;
    ProductAutoCompleteHelper autoCompleteHelper;
    Boolean IsEdit;
    NumberFormat fr;
    Chip chip1,chip2,chip3,chip4,chip5,chipadd;
    ArrayList<Chip> visible_chips;
    ArrayList<Chip> hidden_chips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPointOfSaleBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Pages = new ArrayList<>();
        ItemInvs = new ArrayList<>();
        CurrentPage = 0;
        chip1 = findViewById(R.id.chip_p1);
        Pages.add(new PointOfSalePage(new ArrayList<>(),0.0,0.0,0.0,chip1));
        DB = new DatabaseHelper(this);
        //set locale
        Locale.setDefault(new Locale("ar"));
        fr = NumberFormat.getInstance(Locale.ENGLISH);
        fr.setMaximumFractionDigits(0);
        //create tabs:
        createTabs();
        //check if edit
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            EditSaleID = extras.getString("saleid");
        }
        if(EditSaleID != null){
            IsEdit = true;
            binding.tabs.setVisibility(View.GONE);
            Pages.get(CurrentPage).setDiscount(DB.getSaleDiscountFromSaleID(EditSaleID));
            binding.discountTextview.setText(fr.format(Pages.get(CurrentPage).getDiscount()));
            binding.finish.setText("تعديل");
            Cursor cursor = DB.getProductsfromSaleId(EditSaleID);
            if(cursor.getCount()==0){
                Toast.makeText(PointOfSale.this, "غير موجود", Toast.LENGTH_LONG).show();
                return;
            }
            else{
                Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
                while (cursor.moveToNext()) {
                    PointOfSaleRow tobeadded = new PointOfSaleRow(cursor.getString(7),
                            cursor.getString(5),
                            cursor.getString(1),
                            Double.valueOf(cursor.getString(2)),
                            Double.valueOf(cursor.getString(3)),
                            false,
                            Double.valueOf(cursor.getString(6)),
                            cursor.getInt(4),
                            Double.valueOf(cursor.getString(8))-Double.valueOf(cursor.getString(6)),
                            DB.getInsertFromProductID(cursor.getString(7)));
                    Pages.get(CurrentPage).getItems().add(tobeadded);
                    AddtoInvList(cursor.getString(7),
                            Double.valueOf(cursor.getString(8)),
                            Double.valueOf(cursor.getString(6)),
                            tobeadded);
                }
                cursor.close();
            }
            updateSums();
        }else{
            IsEdit = false;
            binding.discountTextview.setText("0");
            binding.totalTextview.setText("0");
            Pages.get(CurrentPage).setTotal(0.0);
            Pages.get(CurrentPage).setDiscount(0.0);
            Pages.get(CurrentPage).setProfit(0.0);
        }

        //fill autocomplete textview with data
        autoCompleteHelper = DB.getAutoCompleteHelper();
        products = binding.manualTextbox;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(PointOfSale.this, android.R.layout.simple_list_item_1, autoCompleteHelper.getNames()==null?new ArrayList<>():autoCompleteHelper.getNames());
        products.setAdapter(arrayAdapter);

        //recyclerview adapter
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PointOfSaleListAdapter(Pages.get(CurrentPage).getItems(),this);
        binding.recyclerview.setAdapter(adapter);
        new ItemTouchHelper(deleteCallback).attachToRecyclerView(binding.recyclerview);

        //prevent going back
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Boolean empty = true;
                for(int i = 0 ; i < Pages.size() ; i++){
                    if(Pages.get(i).getItems().size()>0){
                        empty = false;
                    }
                }
                if(!empty){
                    AlertDialog.Builder exitConfirm = new AlertDialog.Builder(PointOfSale.this);
                    exitConfirm.setMessage("هل أنت متأكد؟ لديك فواتير لم تحفظ بعد");
                    exitConfirm.setCancelable(true)
                            .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finishAndRemoveTask();
                                }
                            })
                            .setNegativeButton("عودة", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = exitConfirm.create();
                    alertDialog.show();
                }
                else{
                    finishAndRemoveTask();
                }
            }
        });

    }



    //tabs managment:
    private void createTabs() {
        chip2 = findViewById(R.id.chip_p2);
        chip3 = findViewById(R.id.chip_p3);
        chip4 = findViewById(R.id.chip_p4);
        chip5 = findViewById(R.id.chip_p5);

        Pages.add(new PointOfSalePage(new ArrayList<>(),0.0,0.0,0.0,chip2));
        Pages.add(new PointOfSalePage(new ArrayList<>(),0.0,0.0,0.0,chip3));
        Pages.add(new PointOfSalePage(new ArrayList<>(),0.0,0.0,0.0,chip4));
        Pages.add(new PointOfSalePage(new ArrayList<>(),0.0,0.0,0.0,chip5));

        chipadd = findViewById(R.id.chip_add);

        visible_chips = new ArrayList<>();
        hidden_chips = new ArrayList<>();
        visible_chips.add(chip1);
        hidden_chips.add(chip2);
        hidden_chips.add(chip3);
        hidden_chips.add(chip4);
        hidden_chips.add(chip5);
        chip1.setChecked(true);


        chip1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setColors(R.drawable.box_tiel_outline,R.drawable.box_tiel_faint,R.drawable.box_tiel,R.color.TielText);
            setPage(0);
        });
        chip2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setColors(R.drawable.box_orange_outline,R.drawable.box_orange_faint,R.drawable.box_orange,R.color.OrangeText);
            setPage(1);
        });
        chip3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setColors(R.drawable.box_blue_outline,R.drawable.box_blue_faint,R.drawable.box_blue,R.color.BlueText);
            setPage(2);
        });
        chip4.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setColors(R.drawable.box_red_outline,R.drawable.box_red_faint,R.drawable.box_red,R.color.CherryText);
            setPage(3);
        });
        chip5.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setColors(R.drawable.box_green_outline,R.drawable.box_green_faint,R.drawable.box_green,R.color.GreenText);
            setPage(4);
        });


        chip1.setOnCloseIconClickListener(v -> {
            closeChip(0);
        });
        chip2.setOnCloseIconClickListener(v -> {
            closeChip(1);
        });
        chip3.setOnCloseIconClickListener(v -> {
            closeChip(2);
        });
        chip4.setOnCloseIconClickListener(v -> {
            closeChip(3);
        });
        chip5.setOnCloseIconClickListener(v -> {
            closeChip(4);
        });


        ///addaddadd
        chipadd.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(hidden_chips.size()>0){
                int toaddpos = getFirstEmptyPos(hidden_chips);
                visible_chips.add(hidden_chips.get(toaddpos));
                visible_chips.get(visible_chips.size()-1).setChecked(true);
                hidden_chips.remove(toaddpos);
            }

            setChipsVisibility();
        });
    }
    private void setPage(int page){
        CurrentPage = page;
        adapter = new PointOfSaleListAdapter(Pages.get(CurrentPage).getItems(),this);
        binding.recyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        updateSums();
    }
    private void closeChip(int position){
        if(visible_chips.size()>1){
            if(Pages.get(position).getItems().size()>0){
                new android.app.AlertDialog.Builder(PointOfSale.this)
                        .setMessage("هل أنت متأكد من حذف الفاتورة؟")
                        .setNegativeButton("حذف", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeChip(position);
                            }
                        })
                        .setPositiveButton("عودة", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }else{
                removeChip(position);
            }
        }
    }
    private void removeChip(int position){
        hidden_chips.add(Pages.get(position).getChip());
        visible_chips.remove(Pages.get(position).getChip());
        if(CurrentPage == position){
            visible_chips.get(0).setChecked(true);
        }
        for(int i = 0 ; i < Pages.get(position).getItems().size() ; i++){
            addinventory(Pages.get(position).getItems().get(i).getID() , Pages.get(position).getItems().get(i).getCount());
        }
        Pages.get(position).setItems(new ArrayList<>());
        Pages.get(position).setDiscount(0.0);
        Pages.get(position).setProfit(0.0);
        Pages.get(position).setTotal(0.0);
        updateSums();
        adapter.notifyDataSetChanged();
        setChipsVisibility();
    }
    public int getFirstEmptyPos(ArrayList<Chip> listofchips){
        int position1 = listofchips.indexOf(chip1);
        int position2 = listofchips.indexOf(chip2);
        int position3 = listofchips.indexOf(chip3);
        int position4 = listofchips.indexOf(chip4);
        int position5 = listofchips.indexOf(chip5);
        if(position1!=-1){return position1;}
        else if(position2!=-1){return position2;}
        else if(position3!=-1){return position3;}
        else if(position4!=-1){return position4;}
        else{return position5;}
    }
    public void setChipsVisibility(){
        for(int i = 0 ; i < visible_chips.size() ; i++){
            visible_chips.get(i).setVisibility(View.VISIBLE);
        }
        for(int i = 0 ; i < hidden_chips.size() ; i++){
            hidden_chips.get(i).setVisibility(View.GONE);
        }
        if(hidden_chips.size()>0) {
            chipadd.setVisibility(View.VISIBLE);
        }else {
            chipadd.setVisibility(View.GONE);
        }
    }
    public void setColors(int outlinebox, int colorfaintbox,int colorbox , int color){
        int[][] states = new int[][] {new int[] {-android.R.attr.state_enabled}, new int[] { android.R.attr.state_focused}, new int[] { android.R.attr.state_hovered}};
        int[] colors = new int[] {this.getResources().getColor(color), this.getResources().getColor(color), this.getResources().getColor(color)};
        ColorStateList myList = new ColorStateList(states, colors);

        binding.floatingBox.setBackground(ContextCompat.getDrawable(getApplicationContext(), colorfaintbox));
        binding.finish.setBackground(ContextCompat.getDrawable(getApplicationContext(), colorbox));
        binding.manualLayout.setHintTextColor(ColorStateList.valueOf(this.getResources().getColor(color)));
        binding.manualLayout.setDefaultHintTextColor(ColorStateList.valueOf(this.getResources().getColor(color)));
        binding.manualLayout.setBoxStrokeColorStateList(myList);
        binding.save.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext() , color)));
        binding.scan.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext() , color)));
        binding.totalLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), colorbox));
        binding.totalTextview.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext() , color)));
        binding.totalTextviewLayout.setBackground(ContextCompat.getDrawable(getApplicationContext() , outlinebox));
    }


    //adding items manually:
    public void additem(View view) {
        data = binding.manualTextbox.getText().toString();
        if(!autoCompleteHelper.getNames().isEmpty() && autoCompleteHelper.getNames().contains(data)){
            getItemFromDB(autoCompleteHelper.getCodes().get(autoCompleteHelper.getNames().indexOf(data)));
        }
        else{
            Toast.makeText(PointOfSale.this, "الرجاء كتابة إسم منتج صحيح", Toast.LENGTH_LONG).show();
        }
        updateSums();
        adapter.notifyDataSetChanged();
        binding.manualTextbox.setText("");
    }

    //adding items with barcode scanner:
    public void scan(View view) {
        ScanOptions op = new ScanOptions();
        op.setPrompt("شغل الفلاش بالضغط على تعلاية الصوت");
        op.setBeepEnabled(true);
        op.setOrientationLocked(true);
        op.setCaptureActivity(BarCodeCaptureAct.class);
        barLauncher.launch(op);
    }
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result->{
        if(result.getContents() != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(PointOfSale.this);
            builder.setTitle("Res");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
            getItemFromDB(result.getContents());
            adapter.notifyDataSetChanged();
        }
    });

    private void getItemFromDB(String code) {
        Cursor cursor = DB.getProductfromCode(code);
        if(cursor.getCount()==0){
            Toast.makeText(PointOfSale.this, "غير موجود", Toast.LENGTH_LONG).show();
        }
        else{
            Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
            if (cursor.moveToFirst()){
                do{
                    Boolean oktoadd = true;
//                    Double wholesale = 0.0;
                    for(int i = 0 ; i < ItemInvs.size() ; i++){
                        if(ItemInvs.get(i).getItemID().equals(cursor.getString(0))){
                            if(ItemInvs.get(i).getRemainingInPOS()<1){
                                oktoadd = false;
                            }
//                            else{
//                                wholesale = ItemInvs.get()
//                            }
                        }
                    }
                    if(oktoadd){
                        PointOfSaleRow tobeadded = new PointOfSaleRow(cursor.getString(0),
                                code,
                                cursor.getString(1),
                                Double.valueOf(cursor.getString(2)),
                                Double.valueOf(cursor.getString(3)),
                                false,
                                1.0,
                                cursor.getInt(4),
                                Double.valueOf(cursor.getInt(5)),
                                DB.getInsertFromProductID(cursor.getString(0)));





                        Pages.get(CurrentPage).getItems().add(tobeadded);
                        AddtoInvList(cursor.getString(0),Double.valueOf(cursor.getInt(5)),1.0,tobeadded);

                        removeinventory2(tobeadded.getID() , 1.0);
                        InventoryWholesalePrices price = getavailableprice(tobeadded.getID());
                        tobeadded.addprice(ItemInvs,1.0);

                    }
                    else{
                        Toast.makeText(PointOfSale.this, "لم يعد لديك من هذا المنتج", Toast.LENGTH_LONG).show();
                    }

                    updateSums();
                }while(cursor.moveToNext());
            }
            cursor.close();
        }
    }

    public void AddtoInvList (String id , Double remaining , Double amount , PointOfSaleRow row){
        boolean isnew = true;
        for(int i = 0 ; i < ItemInvs.size() ; i++){
            if(ItemInvs.get(i).getItemID().equals(id)){
                ItemInvs.get(i).TakeFromInv(amount);
//                row.addprice(ItemInvs.);
                isnew = false;
                refreshremaining();
            }
        }
        if(isnew){
            ItemInvs.add(new ItemInventory(id,remaining-amount,DB.getInsertFromProductID(id)));
        }
    }
    public void refreshremaining(){
        for(int i = 0 ; i < ItemInvs.size() ; i++){
            for (int ii = 0 ; ii < Pages.size() ; ii++){
                for (int iii = 0 ; iii < Pages.get(ii).getItems().size() ; iii++){
                    if(Pages.get(ii).getItems().get(iii).getID().equals(ItemInvs.get(i).getItemID())){
                        Pages.get(ii).getItems().get(iii).setRemaining(ItemInvs.get(i).getRemainingInPOS());
                        Pages.get(ii).getItems().get(iii).setPrices(ItemInvs.get(i).getWholesalePrice());
                    }
                }
            }
        }
    }

    public void addinventory(String itemID , Double amount){
        for(int i = 0 ; i < ItemInvs.size() ; i++){
            if(ItemInvs.get(i).getItemID().equals(itemID)){
                Double oldremaining = ItemInvs.get(i).getRemainingInPOS();
                ItemInvs.get(i).setRemainingInPOS(oldremaining+amount);
            }
        }
        refreshremaining();
    }
    public void addinventory2(String itemID , Double amount){
        for(int i = 0 ; i < ItemInvs.size() ; i++){
            if(ItemInvs.get(i).getItemID().equals(itemID)){
                Double tobeadded = amount;
                for(int ii = ItemInvs.get(i).getWholesalePrice().size() - 1 ; ii >= 0 ; ii--){
                    Double count = ItemInvs.get(i).getWholesalePrice().get(ii).getCount();
                    Double remaining = ItemInvs.get(i).getWholesalePrice().get(ii).getRemainingCount();
                    Double space = count - remaining;
                    if(space > 0){
                        if(tobeadded<=space){
                            ItemInvs.get(i).getWholesalePrice().get(ii).setRemainingCount(remaining+tobeadded);
                            tobeadded = 0.0;
                        }else{
                            ItemInvs.get(i).getWholesalePrice().get(ii).setRemainingCount(count);
                            tobeadded = tobeadded - count;
                        }
                    }
                }
            }
        }
    }
    public void removeinventory(String itemID , Double amount){
        for(int i = 0 ; i < ItemInvs.size() ; i++){
            if(ItemInvs.get(i).getItemID().equals(itemID)){
                Double oldremaining = ItemInvs.get(i).getRemainingInPOS();
                ItemInvs.get(i).setRemainingInPOS(oldremaining-amount);
            }
        }
        refreshremaining();
    }

    public void removeinventory2(String itemID , Double amount){
        for(int i = 0 ; i < ItemInvs.size() ; i++){
            if(ItemInvs.get(i).getItemID().equals(itemID)){
                Double toberemoved = amount;
                for(int ii = 0 ; ii < ItemInvs.get(i).getWholesalePrice().size() ; ii++){
                    if(toberemoved>0){
                        Double count = ItemInvs.get(i).getWholesalePrice().get(ii).getRemainingCount();
//                        Double price = ItemInvs.get(i).getWholesalePrice().get(ii).getPrice();
                        if(toberemoved<=count){
                            ItemInvs.get(i).getWholesalePrice().get(ii).setRemainingCount(count-toberemoved);
                            toberemoved = 0.0;
                        }else{
                            ItemInvs.get(i).getWholesalePrice().get(ii).setRemainingCount(0.0);
                            toberemoved = toberemoved - count;
                        }
                    }
                }
            }
        }
        refreshremaining();
    }

    public void reciept(View view) {
        Intent intent = new Intent(PointOfSale.this, Reciept.class);
        intent.putExtra("Items", Pages.get(CurrentPage).getItems());
        intent.putExtra("DiscountAmount" , Pages.get(CurrentPage).getDiscount());
        startActivity(intent);
    }

    //deleting item:
    PointOfSaleRow deleted_row; //this is for undoing delete
    ItemTouchHelper.SimpleCallback deleteCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getBindingAdapterPosition();
            deleted_row = Pages.get(CurrentPage).getItems().get(position);
            Pages.get(CurrentPage).getItems().remove(position);
            addinventory(deleted_row.getID(),deleted_row.getCount());
            updateSums();
            adapter.notifyDataSetChanged();
            Snackbar.make(binding.recyclerview,deleted_row.getName() + " تم حذفه.",Snackbar.LENGTH_LONG).setAction("تراجع", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pages.get(CurrentPage).getItems().add(position, deleted_row);
                    removeinventory(deleted_row.getID(),deleted_row.getCount());
                    updateSums();
                    adapter.notifyDataSetChanged();
                }
            }).show();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(PointOfSale.this , R.color.white))
                    .addSwipeLeftActionIcon(R.drawable.icon_delete)
                    .addSwipeLeftCornerRadius(3,3)
                    .addSwipeLeftLabel("Remove")
                    .setSwipeLeftLabelColor(ContextCompat.getColor(PointOfSale.this , R.color.Red))
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    //increase count
    @Override
    public void OnIncreaseClicked(int pos) {
        Pages.get(CurrentPage).getItems().get(pos).addtoCount();

        InventoryWholesalePrices price = getavailableprice(Pages.get(CurrentPage).getItems().get(pos).getID());
        Pages.get(CurrentPage).getItems().get(pos).addprice(ItemInvs,1.0);

        removeinventory(Pages.get(CurrentPage).getItems().get(pos).getID() , 1.0);
        removeinventory2(Pages.get(CurrentPage).getItems().get(pos).getID() , 1.0);
        refreshremaining();
        updateSums();
        adapter.notifyDataSetChanged();
    }

    private InventoryWholesalePrices getavailableprice(String id) {
//        InventoryWholesalePrices temp;
        for (int i =0 ; i<ItemInvs.size() ; i++){
            if(ItemInvs.get(i).getItemID().equals(id)){
                for(int ii = 0 ; ii<ItemInvs.get(i).getWholesalePrice().size() ; ii++){
                    if(ItemInvs.get(i).getWholesalePrice().get(ii).getRemainingCount()>0){
//                        if(ItemInvs.get(i).getWholesalePrice().get(ii).getRemainingCount()==1){
//                            ItemInvs.get(i).getWholesalePrice().remove(ii);
//                        }else{
//                            ItemInvs.get(i).getWholesalePrice().get(ii).removeitem();
//                        }
                        return new InventoryWholesalePrices(id,ItemInvs.get(i).getWholesalePrice().get(ii).getPrice(),ItemInvs.get(i).getWholesalePrice().get(ii).getCount(),1.0,ItemInvs.get(i).getWholesalePrice().get(ii).getInventoryInsertionDate());
                    }
                }
//                temp = ItemInvs.get(i).getWholesalePrice().get(0);

            }
        }
        return null;
    }

    //decrease count
    @Override
    public void OnDecreaseClicked(int pos) {
        Pages.get(CurrentPage).getItems().get(pos).removefromCount();

        Pages.get(CurrentPage).getItems().get(pos).removeprice(1.0);

        addinventory(Pages.get(CurrentPage).getItems().get(pos).getID() , 1.0);
        addinventory2(Pages.get(CurrentPage).getItems().get(pos).getID() , 1.0);

        refreshremaining();
        updateSums();
        adapter.notifyDataSetChanged();
    }
    //view item details
    @Override
    public void OnRowClicked(int pos) {
        PointOfSaleDetailsItemDialog customDialog = new PointOfSaleDetailsItemDialog(Pages.get(CurrentPage).getItems().get(pos));
        customDialog.show(getSupportFragmentManager(),"view details");
    }
    //show edit count dialog
    @Override
    public void OnCountClicked(int pos) {
        PointOfSaleCountDialog customDialog = new PointOfSaleCountDialog(Pages.get(CurrentPage).getItems().get(pos).getPrice(),
                Pages.get(CurrentPage).getItems().get(pos).getCount(),
                Pages.get(CurrentPage).getItems().get(pos).getByWeight(),
                Pages.get(CurrentPage).getItems().get(pos).getRemaining(),
                pos);
        customDialog.show(getSupportFragmentManager(),"enter count");
    }
    //set count
    @Override
    public void applyCount(Double count , int position) {
        Double oldcount = Pages.get(CurrentPage).getItems().get(position).getCount();
        Pages.get(CurrentPage).getItems().get(position).setCount(count);
        for(int i = 0 ; i < ItemInvs.size() ; i++){
            if(ItemInvs.get(i).getItemID().equals(Pages.get(CurrentPage).getItems().get(position).getID())){
                if(oldcount > count){
                    ItemInvs.get(i).AddToInv(oldcount - count);

                    Pages.get(CurrentPage).getItems().get(position).removeprice(oldcount - count);

//                    addinventory(Pages.get(CurrentPage).getItems().get(position).getID() , oldcount - count);
                    addinventory2(Pages.get(CurrentPage).getItems().get(position).getID() , oldcount - count);
                }
                else if(oldcount < count){
                    ItemInvs.get(i).TakeFromInv(count - oldcount);

                    InventoryWholesalePrices price = getavailableprice(Pages.get(CurrentPage).getItems().get(position).getID());
                    Pages.get(CurrentPage).getItems().get(position).addprice(ItemInvs,count - oldcount);

//                    removeinventory(Pages.get(CurrentPage).getItems().get(position).getID() , count - oldcount);
                    removeinventory2(Pages.get(CurrentPage).getItems().get(position).getID() , count - oldcount);
                }
                Pages.get(CurrentPage).getItems().get(position).setRemaining(ItemInvs.get(i).getRemainingInPOS());
            }
        }
        refreshremaining();
        updateSums();
        adapter.notifyDataSetChanged();
    }
    //show discount dialog
    public void discount(View view){
        PointOfSaleDiscountDialog customDialog = new PointOfSaleDiscountDialog(Pages.get(CurrentPage).getProfit());
        customDialog.show(getSupportFragmentManager(),"enter discount");
    }
    //apply discount
    @Override
    public void applyDiscount(Double dis) {
        Pages.get(CurrentPage).setDiscount(dis);

        binding.discountTextview.setText(fr.format(Pages.get(CurrentPage).getDiscount()));
        binding.totalTextview.setText(fr.format(Pages.get(CurrentPage).getTotal() - Pages.get(CurrentPage).getDiscount()));
        if(dis > 0){
            binding.totalTitle.setText("المجموع بعد الحسم");
        }
        else{
            binding.totalTitle.setText("المجموع");
        }
    }
    //show total details dialog
    public void ShowTotalDetails(View view) {
        PointOfSaleDetailsDialog customDialog = new PointOfSaleDetailsDialog(Pages.get(CurrentPage).getTotal() ,Pages.get(CurrentPage).getTotal() - Pages.get(CurrentPage).getProfit(), Pages.get(CurrentPage).getProfit(),  (Pages.get(CurrentPage).getProfit() / Pages.get(CurrentPage).getTotal())*100 , Pages.get(CurrentPage).getDiscount());
        customDialog.show(getSupportFragmentManager(),"view details");
    }



    //general data fixes
    public void updateSums(){
        Pages.get(CurrentPage).setTotal(0.0);
        Pages.get(CurrentPage).setProfit(0.0);
        for (int i = 0 ; i < Pages.get(CurrentPage).getItems().size() ; i++) {
            Pages.get(CurrentPage).setTotal(Pages.get(CurrentPage).getTotal() + Pages.get(CurrentPage).getItems().get(i).getSum());
            Pages.get(CurrentPage).setProfit(Pages.get(CurrentPage).getProfit() + Pages.get(CurrentPage).getItems().get(i).getTotalProfit());
//            Pages.get(CurrentPage).getItems().get(i).setPrices(DB.getInsertFromProductID(Pages.get(CurrentPage).getItems().get(i).getID()));
        }
        if(Pages.get(CurrentPage).getDiscount() > Pages.get(CurrentPage).getProfit()){
            Pages.get(CurrentPage).setDiscount(Pages.get(CurrentPage).getProfit());
            binding.discountTextview.setText(fr.format(Pages.get(CurrentPage).getDiscount()));
        }
        binding.totalTextview.setText(fr.format(Pages.get(CurrentPage).getTotal() - Pages.get(CurrentPage).getDiscount()));
        binding.discountTextview.setText(fr.format(Pages.get(CurrentPage).getDiscount()));
    }
    private void reset() {
        Pages.get(CurrentPage).setItems(new ArrayList<>());
        Pages.get(CurrentPage).setTotal(0.0);
        Pages.get(CurrentPage).setDiscount(0.0);
        Pages.get(CurrentPage).setProfit(0.0);
//        adapter = new PointOfSaleListAdapter(Pages.get(CurrentPage).getItems(),this);
//        binding.recyclerview.setAdapter(adapter);
//        binding.totalTextview.setText(fr.format(Pages.get(CurrentPage).getTotal() - Pages.get(CurrentPage).getDiscount()));
//        binding.discountTextview.setText(fr.format(Pages.get(CurrentPage).getDiscount()));
        updateSums();
    }

    //
    public void finish(View view) {
        if(Pages.get(CurrentPage).getItems().size()>0){
            new android.app.AlertDialog.Builder(PointOfSale.this)
                    .setTitle("تأكيد؟")
                    .setPositiveButton("إدخال", (dialog, which) -> {
                        if(IsEdit){
                            long updated = DB.updateSale(Pages.get(CurrentPage).getDiscount(), Pages.get(CurrentPage).getItems(),EditSaleID);
                            if(updated != -1){
                                Toast.makeText(PointOfSale.this,"تم التعديل.",Toast.LENGTH_LONG).show();
                                reset();
                            }else {
                                Toast.makeText(PointOfSale.this, String.valueOf(updated), Toast.LENGTH_LONG).show();
                            }
                        }else{
                            long inserted = DB.insertSale(Pages.get(CurrentPage).getDiscount(), Pages.get(CurrentPage).getItems());
                            if(inserted != -1){
                                Toast.makeText(PointOfSale.this,"تم إلبيع.",Toast.LENGTH_LONG).show();
                                reset();
                            }else {
                                Toast.makeText(PointOfSale.this, String.valueOf(inserted), Toast.LENGTH_LONG).show();
                            }
                            if(visible_chips.size()>1){
                                closeChip(CurrentPage);
                            }
                        }
                    })
                    .setNegativeButton("إلغاء", (dialog, which) -> {

                    })
                    .show();
        }
        else{
            new android.app.AlertDialog.Builder(PointOfSale.this)
                    .setMessage("الرجاء إضافة مواد للفاتورة")
                    .setNegativeButton("موافق", (dialog, which) -> {

                    })
                    .show();
        }

    }







}