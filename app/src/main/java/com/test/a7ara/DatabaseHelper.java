package com.test.a7ara;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.test.a7ara.point_of_sale.InventoryWholesalePrices;
import com.test.a7ara.point_of_sale.PointOfSaleRow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "shop.db";
    public static final String TABLE_NAME_PRODUCTS = "PRODUCTS";
    public static final String TABLE_NAME_QRS = "QR_CODES";
    public static final String TABLE_NAME_INSRTS = "INSERTIONS_LOG";
    public static final String TABLE_NAME_SALES = "SALES_LOG";
    public static final String TABLE_NAME_SALE_ITEM = "SALES_LOG_ITEM";
    private static final String TABLE_NAME_DIFFS = "DIFFS";
    private static final String TABLE_NAME_INSERTION_RELATION = "TABLE_NAME_INSERTION_RELATION";
    //shared constants
    public static final String ID = "ID";
    public static final String CREATION_DATE = "CREATION_DATE";
    public static final String MODIFICATION_DATE = "MODIFICATION_DATE";
    public static final String NAME = "NAME";
    public static final String PRODUCT_ID = "PRODUCT_ID";
    public static final String CODE = "CODE";
    //product specific
    public static final String CATEGORY = "CATEGORY";
    public static final String BY_WEIGHT = "BY_WEIGHT";
    //insertion specific
    public static final String PIECES = "PIECES";
    public static final String PAYED_INSERTION_PRICE = "PAYED_INSERTION_PRICE";
    public static final String PAYED_PIECE_PRICE = "PAYED_PIECE_PRICE";
    public static final String RETAIL_PIECE_PRICE = "RETAIL_PIECE_PRICE";
    public static final String REMAINING_PIECES = "REMAINING_PIECES";
    //sales specific
    public static final String DISCOUNT = "DISCOUNT";
    public static final String SALE_TOTAL_WHOLESALE = "SALE_TOTAL_WHOLESALE";
    public static final String SALE_TOTAL_RETAIL = "SALE_TOTAL_RETAIL";
    //sales item specific
    public static final String SALE_ID = "SALE_ID";
    public static final String ITEM_COUNT = "COUNT";
    public static final String ITEM_WHOLESALE_PRICE = "ITEM_WHOLESALE_PRICE";
    public static final String ITEM_RETAIL_PRICE = "ITEM_RETAIL_PRICE";
    public static final String ITEM_WHOLESALE_PRICE_TOTAL = "ITEM_WHOLESALE_PRICE_TOTAL";
    public static final String ITEM_RETAIL_PRICE_TOTAL = "ITEM_RETAIL_PRICE_TOTAL";
    //diffs specific
    private static final String DIFF_COUNT = "DIFF_COUNT";
    private static final String DIFF_TYPE = "DIFF_TYPE";
    private static final String DIFF_PIECE_PRICE_RETAIL = "DIFF_PIECE_PRICE_RETAIL";
    private static final String DIFF_PIECE_PRICE_WHOLESALE = "DIFF_PIECE_PRICE_WHOLESALE";
    private static final String DIFF_TOTAL_PRICE_RETAIL = "DIFF_TOTAL_PRICE_RETAIL";
    private static final String DIFF_TOTAL_PRICE_WHOLESALE = "DIFF_TOTAL_PRICE_WHOLESALE";
    private static final String LOSS = "LOSS";
    ////relational
    private static final String RELATION_ID = "RELATION_ID";
    private static final String INSERTION_ID = "INSERTION_ID";
    private static final String RELATION_TO = "RELATION_TO";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME_PRODUCTS +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT ," +
                NAME + " TEXT, " + CODE + " TEXT ,  " + CATEGORY + " TEXT , "  + BY_WEIGHT + " INTEGER , " + CREATION_DATE + " DATETIME , " + MODIFICATION_DATE + " DATETIME)");
        db.execSQL("create table " + TABLE_NAME_QRS +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT ," +
                PRODUCT_ID + " TEXT, " + CODE + " TEXT , " + CREATION_DATE + " DATETIME)");
        db.execSQL("create table " + TABLE_NAME_INSRTS +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT ," +
                PRODUCT_ID + " TEXT, " + PIECES + " TEXT, " + PAYED_INSERTION_PRICE + " TEXT, " +
                PAYED_PIECE_PRICE + " TEXT, " +  RETAIL_PIECE_PRICE + " TEXT, " + REMAINING_PIECES + " TEXT, " +
                 MODIFICATION_DATE + " DATETIME , " + CREATION_DATE + " DATETIME)");
        db.execSQL("create table " + TABLE_NAME_SALES +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT ," +
                DISCOUNT + " TEXT ," + SALE_TOTAL_WHOLESALE + " TEXT ," + SALE_TOTAL_RETAIL + " TEXT ," + MODIFICATION_DATE + " DATETIME , " +
                CREATION_DATE + " DATETIME)");
        db.execSQL("create table " + TABLE_NAME_SALE_ITEM +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT ," +
                SALE_ID + " TEXT, " + PRODUCT_ID + " TEXT, " + CODE + " TEXT , " + ITEM_COUNT + " TEXT , " + ITEM_WHOLESALE_PRICE +
                " TEXT , " + ITEM_RETAIL_PRICE + " TEXT , " + ITEM_WHOLESALE_PRICE_TOTAL + " TEXT , " + ITEM_RETAIL_PRICE_TOTAL +
                " TEXT , " + MODIFICATION_DATE + " DATETIME ," + CREATION_DATE + " DATETIME)");
        db.execSQL("create table " + TABLE_NAME_DIFFS +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT ," +
                PRODUCT_ID + " TEXT, " + DIFF_COUNT + " TEXT , " + DIFF_TYPE + " TEXT , " +
                DIFF_PIECE_PRICE_RETAIL + " TEXT, " + DIFF_PIECE_PRICE_WHOLESALE + " TEXT , " +
                DIFF_TOTAL_PRICE_RETAIL + " TEXT, " + DIFF_TOTAL_PRICE_WHOLESALE + " TEXT , " +
                LOSS + " TEXT , " +
                MODIFICATION_DATE + " DATETIME ," + CREATION_DATE + " DATETIME)");
        //RELATIONAL TABLES
        db.execSQL("create table " + TABLE_NAME_INSERTION_RELATION +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT ," +
                RELATION_ID + " TEXT, " + INSERTION_ID + " TEXT , " + PIECES + " TEXT , " +
                RELATION_TO + " TEXT , " + MODIFICATION_DATE + " DATETIME , " + CREATION_DATE + " DATETIME)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_QRS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_INSRTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SALES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SALE_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DIFFS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_INSERTION_RELATION);
        onCreate(db);
    }

    //********************
    //Product:
    public long insertProduct (String name ,String code,  String category, Integer by_weight){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAME , name);
        cv.put(CODE , code);
        cv.put(CATEGORY , category);
        cv.put(BY_WEIGHT , by_weight);
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(Calendar.getInstance().getTime());
        cv.put(CREATION_DATE , currentDateandTime);
        return db.insert(TABLE_NAME_PRODUCTS,null,cv);
    }
    public String deleteProduct(String pr_id){
        SQLiteDatabase db = this.getWritableDatabase();


        db.execSQL("DELETE from " + TABLE_NAME_INSRTS + " where " + PRODUCT_ID + " = " + pr_id);
        db.execSQL("DELETE from " + TABLE_NAME_SALE_ITEM + " where " + PRODUCT_ID + " = " + pr_id);
        db.execSQL("DELETE from " + TABLE_NAME_PRODUCTS + " where " + ID + " = " + pr_id);

        //delets sales without any items
        db.execSQL("DELETE from " + TABLE_NAME_SALES + " where " + ID + " NOT IN " +
                "(SELECT "+SALE_ID+" from "+TABLE_NAME_SALE_ITEM+")");

        return "Deleted Product";
    }
    public Cursor getAllProducts(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select ID, NAME, CATEGORY , CREATION_DATE , " +
                "(select "+TABLE_NAME_QRS+"."+CODE+" from "+TABLE_NAME_QRS+" where "+TABLE_NAME_QRS+"."+PRODUCT_ID+" = "+TABLE_NAME_PRODUCTS+"."+ID+" order by "+TABLE_NAME_QRS+"."+CREATION_DATE+" DESC limit 1)," +
                "(select SUM("+TABLE_NAME_INSRTS+"."+PIECES+") from "+TABLE_NAME_INSRTS+" where "+TABLE_NAME_INSRTS+"."+PRODUCT_ID+" = "+TABLE_NAME_PRODUCTS+"."+ID+") ," +
                "(select SUM("+TABLE_NAME_SALE_ITEM+"."+ITEM_COUNT+") from "+TABLE_NAME_SALE_ITEM+" where "+TABLE_NAME_SALE_ITEM+"."+PRODUCT_ID+" = "+TABLE_NAME_PRODUCTS+"."+ID+"), " +
                "(select SUM("+TABLE_NAME_DIFFS+"."+DIFF_COUNT+") from "+TABLE_NAME_DIFFS+" where "+TABLE_NAME_DIFFS+"."+PRODUCT_ID+" = "+TABLE_NAME_PRODUCTS+"."+ID+"), " +
                BY_WEIGHT + " " +
                "from "+TABLE_NAME_PRODUCTS,null);
    }
    public String getProductNameFromCode(String code){
        SQLiteDatabase db = getReadableDatabase();
        String i = "nothing found";
        String query = "SELECT (select products.NAME from "+TABLE_NAME_PRODUCTS+" where products.id = qr_codes.PRODUCT_ID) FROM "+TABLE_NAME_QRS+" where CODE LIKE '%"+code+"%'";
        Cursor cursor = db.rawQuery(query,  null);

        if (cursor != null) {
            cursor.moveToFirst();
            i = cursor.getString(0);
        }
        cursor.close();
        db.close();

        return i;
    }
    public ProductAutoCompleteHelper getAutoCompleteHelper(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String > ids = new ArrayList<String>();
        ArrayList<String > names = new ArrayList<String>();
        ArrayList<String > codes = new ArrayList<String>();
        ArrayList<String > invs = new ArrayList<String>();
        Cursor cursor = db.rawQuery("select "+ID+", "+NAME+", "+CODE+
                ",(select SUM("+TABLE_NAME_INSRTS+"."+REMAINING_PIECES+") from "+TABLE_NAME_INSRTS+" where "+TABLE_NAME_INSRTS+"."+PRODUCT_ID+" = "+TABLE_NAME_PRODUCTS+"."+ID+ ")" +
                " from "+TABLE_NAME_PRODUCTS + "",null);
        if(cursor.getCount()==0){
            return null;
        }
        else{
            while (cursor.moveToNext()){
                ids.add(cursor.getString(0));
                names.add(cursor.getString(1));
                codes.add(cursor.getString(2));
                invs.add(cursor.getString(3)==null?"0":cursor.getString(3));
            }
            return new ProductAutoCompleteHelper(ids,names,codes,invs);
        }
    }
    public Cursor getProductsfromSaleId(String saleid){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select "+TABLE_NAME_SALE_ITEM + "."+ ID + " , " +
                "(select "+TABLE_NAME_PRODUCTS+"."+NAME+" from "+TABLE_NAME_PRODUCTS+" where "+TABLE_NAME_PRODUCTS+"."+ID+" = "+TABLE_NAME_SALE_ITEM+"."+PRODUCT_ID+") ," +
                ITEM_RETAIL_PRICE + "," + ITEM_WHOLESALE_PRICE + "," +
                "(select "+TABLE_NAME_PRODUCTS+"."+BY_WEIGHT+" from "+TABLE_NAME_PRODUCTS+" where "+TABLE_NAME_PRODUCTS+"."+ID+" = "+TABLE_NAME_SALE_ITEM+"."+PRODUCT_ID+") ," +
                CODE + "," + ITEM_COUNT + ", " + PRODUCT_ID + "," +
                "(select SUM("+TABLE_NAME_INSRTS+"."+REMAINING_PIECES+") FROM "+TABLE_NAME_INSRTS+" where "+TABLE_NAME_INSRTS+"."+PRODUCT_ID+" = " + TABLE_NAME_SALE_ITEM+"."+PRODUCT_ID + ")" +
                " from "+TABLE_NAME_SALE_ITEM +" where "+SALE_ID+" LIKE '"+saleid+"'",null);
    }
    public String getProductIDFromCode(String code){
        SQLiteDatabase db = getReadableDatabase();
        String i = "nothing found";
        String query = "SELECT PRODUCT_ID FROM "+TABLE_NAME_QRS+" where CODE LIKE '%"+code+"%'";
        Cursor cursor = db.rawQuery(query,  null);
        if (cursor != null) {
            cursor.moveToFirst();
            i = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return i;
    }
    public Cursor getProductfromID(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select " + NAME + ", " + CODE + " , "+CATEGORY+" , " +
                "(select "+TABLE_NAME_INSRTS+"."+PAYED_PIECE_PRICE+" from "+TABLE_NAME_INSRTS+" where "+TABLE_NAME_INSRTS+"."+PRODUCT_ID+" = "+TABLE_NAME_PRODUCTS+"."+ID+" ORDER BY "+TABLE_NAME_INSRTS+"."+CREATION_DATE+" DESC LIMIT 1), " +
                "(select "+TABLE_NAME_INSRTS+"."+RETAIL_PIECE_PRICE+" from "+TABLE_NAME_INSRTS+" where "+TABLE_NAME_INSRTS+"."+PRODUCT_ID+" = "+TABLE_NAME_PRODUCTS+"."+ID+" ORDER BY "+TABLE_NAME_INSRTS+"."+CREATION_DATE+" DESC LIMIT 1), " +
                "(select SUM("+TABLE_NAME_SALE_ITEM+"."+ITEM_WHOLESALE_PRICE_TOTAL+") from "+TABLE_NAME_SALE_ITEM+" where "+TABLE_NAME_SALE_ITEM+"."+PRODUCT_ID+" = "+TABLE_NAME_PRODUCTS+"."+ID+"), " +
                "(select SUM("+TABLE_NAME_SALE_ITEM+"."+ITEM_RETAIL_PRICE_TOTAL+") from "+TABLE_NAME_SALE_ITEM+" where "+TABLE_NAME_SALE_ITEM+"."+PRODUCT_ID+" = "+TABLE_NAME_PRODUCTS+"."+ID+"), " +
                "(select SUM("+TABLE_NAME_INSRTS+"."+PIECES+") from "+TABLE_NAME_INSRTS+" where "+TABLE_NAME_INSRTS+"."+PRODUCT_ID+" = "+TABLE_NAME_PRODUCTS+"."+ID+"), " +
                "(select SUM("+TABLE_NAME_INSRTS+"."+PAYED_INSERTION_PRICE+") from "+TABLE_NAME_INSRTS+" where "+TABLE_NAME_INSRTS+"."+PRODUCT_ID+" = "+TABLE_NAME_PRODUCTS+"."+ID+"), " +
                "(select SUM("+TABLE_NAME_SALE_ITEM+"."+ITEM_COUNT+") from "+TABLE_NAME_SALE_ITEM+" where "+TABLE_NAME_SALE_ITEM+"."+PRODUCT_ID+" = "+TABLE_NAME_PRODUCTS+"."+ID+"), " +
                "(select SUM("+TABLE_NAME_DIFFS+"."+DIFF_COUNT+") from "+TABLE_NAME_DIFFS+" where "+TABLE_NAME_DIFFS+"."+PRODUCT_ID+" = "+TABLE_NAME_PRODUCTS+"."+ID+"), " +
                "(select SUM("+TABLE_NAME_DIFFS+"."+DIFF_TOTAL_PRICE_WHOLESALE+") from "+TABLE_NAME_DIFFS+" where "+TABLE_NAME_DIFFS+"."+PRODUCT_ID+" = "+TABLE_NAME_PRODUCTS+"."+ID+") " +
                "from "+TABLE_NAME_PRODUCTS +" where "+ID+" LIKE '%"+id+"%'",null);
    }

    public Cursor getProductfromCode(String code){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select "+ID+","+NAME+" , " +
                "(select "+TABLE_NAME_INSRTS+"."+RETAIL_PIECE_PRICE+" from "+TABLE_NAME_INSRTS+" where "+TABLE_NAME_INSRTS+"."+PRODUCT_ID+" = "+TABLE_NAME_PRODUCTS+"."+ID+" ORDER BY "+TABLE_NAME_INSRTS+"."+CREATION_DATE+" DESC LIMIT 1) ," +
                "(select "+TABLE_NAME_INSRTS+"."+PAYED_PIECE_PRICE+" from "+TABLE_NAME_INSRTS+" where "+TABLE_NAME_INSRTS+"."+PRODUCT_ID+" = "+TABLE_NAME_PRODUCTS+"."+ID+" ORDER BY "+TABLE_NAME_INSRTS+"."+CREATION_DATE+" DESC LIMIT 1) ," +
                BY_WEIGHT +", " +
                "(select SUM("+TABLE_NAME_INSRTS+"."+REMAINING_PIECES+") from "+TABLE_NAME_INSRTS+" where "+TABLE_NAME_INSRTS+"."+PRODUCT_ID+" = "+TABLE_NAME_PRODUCTS+"."+ID+") " +
                " from "+TABLE_NAME_PRODUCTS +" where "+CODE+" LIKE '"+code+"'",null);
    }
    private Double getLastWholesalePriceByProductID(String id) {
        SQLiteDatabase db = getReadableDatabase();
        Double i = 0.0;
        String query = "select "+TABLE_NAME_INSRTS+"."+PAYED_PIECE_PRICE+" from "+TABLE_NAME_INSRTS+" where "+TABLE_NAME_INSRTS+"."+PRODUCT_ID+" = "+id+" ORDER BY "+TABLE_NAME_INSRTS+"."+CREATION_DATE+" DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query,  null);
        if (cursor != null) {
            cursor.moveToFirst();
            i = cursor.getString(0) == "" ? 0.0 : Double.valueOf(cursor.getString(0));
        }
        cursor.close();
//        db.close();
        return i;
    }
    private Double getLastRetailPriceByProductID(String id) {
        SQLiteDatabase db = getReadableDatabase();
        Double i = 0.0;
        String query = "select "+TABLE_NAME_INSRTS+"."+RETAIL_PIECE_PRICE+" from "+TABLE_NAME_INSRTS+" where "+TABLE_NAME_INSRTS+"."+PRODUCT_ID+" = "+id+" ORDER BY "+TABLE_NAME_INSRTS+"."+CREATION_DATE+" DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query,  null);
        if (cursor != null) {
            cursor.moveToFirst();
            i = cursor.getString(0) == "" ? 0.0 : Double.valueOf(cursor.getString(0));
        }
        cursor.close();
//        db.close();
        return i;
    }



    //**************************************************************************************************************************************
    //Codes:
    public boolean insertCode (long product_id ,  String code){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PRODUCT_ID , product_id);
        cv.put(CODE , code);
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(Calendar.getInstance().getTime());
        cv.put(CREATION_DATE , currentDateandTime);
        long result = db.insert(TABLE_NAME_QRS,null,cv);
        return result != -1;
    }
    //checks for already inserted QR codes
    public boolean Code_added (String value){
        String query = "SELECT * FROM " + TABLE_NAME_QRS + " WHERE " + CODE + " = ?";
        String[] whereArgs = {value};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, whereArgs);
        int count = cursor.getCount();
        cursor.close();
        return count >= 1;
    }



    //**************************************************************************************************************************************
    //Insertions:
    public long insertInsertion (String prodID ,String pieces,  String wholesale_price,String retail_price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PRODUCT_ID , prodID);
        cv.put(PIECES , pieces);
        cv.put(REMAINING_PIECES , pieces);
        cv.put(PAYED_INSERTION_PRICE , wholesale_price);
        double wholesale_peice_price = 0;
        if(wholesale_price != null && pieces != null){
            wholesale_peice_price = Double.parseDouble(wholesale_price) / Double.parseDouble(pieces);
        }
        cv.put(PAYED_PIECE_PRICE , String.valueOf(wholesale_peice_price));
        cv.put(RETAIL_PIECE_PRICE , retail_price);
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(Calendar.getInstance().getTime());
        cv.put(CREATION_DATE , currentDateandTime);
        return db.insertOrThrow(TABLE_NAME_INSRTS,null,cv);
    }

    public String deleteInsertion(String insertion_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE from " + TABLE_NAME_INSRTS + " where " + ID + " = " + insertion_id);

        ///////*************THERE SHOULD BE LOGS DELETION HERE + CHANGE OR REMOVAL OF RELATED SALES/DIFFS
        return "Deleted Insertion";
    }

    public ArrayList<InventoryWholesalePrices> getInsertFromProductID(String product_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select "+ID+", "+ PAYED_PIECE_PRICE + ","+ PIECES+" , "+ REMAINING_PIECES+" ,  " +CREATION_DATE + " " +
                "from "+TABLE_NAME_INSRTS +" where "+PRODUCT_ID+" = "+product_id,null);
        ArrayList<InventoryWholesalePrices> PricesList = new ArrayList<>();
        while (cursor.moveToNext()) {
            PricesList.add(new InventoryWholesalePrices(cursor.getString(0),
                    Double.valueOf(cursor.getString(1)),
                    Double.valueOf(cursor.getString(2)),
                    Double.valueOf(cursor.getString(3)),
                    cursor.getString(4)));
        }
        return PricesList;
    }

    public Cursor getAllInserts() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select "+ID+", "+PRODUCT_ID+", "+
                "(select "+TABLE_NAME_PRODUCTS+"."+NAME+" from "+TABLE_NAME_PRODUCTS+" where "+TABLE_NAME_PRODUCTS+"."+ID+" = "+TABLE_NAME_INSRTS+"."+PRODUCT_ID+"), " +
                PIECES+" , "+ REMAINING_PIECES+" , "+ PAYED_INSERTION_PRICE + ", " +CREATION_DATE + " " +
                "from "+TABLE_NAME_INSRTS,null);
    }

    public Cursor getInsertLogs(String insertion_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select "+RELATION_TO+", "+RELATION_ID+", "+ PIECES+
                " from "+TABLE_NAME_INSERTION_RELATION + " where " + INSERTION_ID + " = " + insertion_id + " ORDER BY "+CREATION_DATE+" ASC",null);
    }

    public String DecreaseInventory(String product_id, Integer amount,String type, String relid){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<InventoryDecrease> decrease = new ArrayList<>();
        Cursor temp = db.rawQuery("select " + ID +","+ REMAINING_PIECES +
                " from "+TABLE_NAME_INSRTS + " where " + PRODUCT_ID + " = " + product_id + " ORDER BY "+CREATION_DATE+" ASC",null);
        while (temp.moveToNext()) {
            decrease.add(new InventoryDecrease(Integer.valueOf(temp.getString(0)),
                    null,
                    Integer.valueOf(temp.getString(1))));
        }
        int startingID = -1;
        int tobetakenout = amount;
        for(int i = 0 ; i < decrease.size() ; i++){
            if(decrease.get(i).getInsertRemaining()>0){
                if(startingID == -1){
                    startingID = decrease.get(i).getInsertID();
                }
            }
        }
        boolean isediting = false;
        ArrayList<InventoryDecrease> AffectedRows = new ArrayList<>();
        for(int i = 0 ; i < decrease.size() ; i++){
            if(tobetakenout >0){
                int remaining = decrease.get(i).getInsertRemaining();
                if(decrease.get(i).getInsertID()==startingID || isediting){
                    isediting = true;
                    if(remaining>= tobetakenout){
                        decrease.get(i).setInsertRemaining(remaining- tobetakenout);
                        decrease.get(i).setInsertCount(tobetakenout);
                        AffectedRows.add(decrease.get(i));
                        tobetakenout = 0;
                        isediting = false;
                    }
                    else{
                        decrease.get(i).setInsertRemaining(0);
                        decrease.get(i).setInsertCount(remaining);
                        AffectedRows.add(decrease.get(i));
                        tobetakenout = tobetakenout - remaining;
                    }
                }
            }
        }
        long res = 0;
        for(int i = 0 ; i < AffectedRows.size() ; i++) {
            ContentValues cv = new ContentValues();
            cv.put(REMAINING_PIECES , AffectedRows.get(i).getInsertRemaining());
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss", Locale.getDefault());
            String currentDateandTime = sdf.format(Calendar.getInstance().getTime());
            cv.put(MODIFICATION_DATE , currentDateandTime);
            res = db.update(TABLE_NAME_INSRTS,cv,"ID = ?",new String[]{String.valueOf(AffectedRows.get(i).getInsertID())});

            ContentValues relation_cv = new ContentValues();
            relation_cv.put(RELATION_ID, relid);
            relation_cv.put(RELATION_TO, type);
            relation_cv.put(INSERTION_ID, AffectedRows.get(i).getInsertID());
            relation_cv.put(PIECES, AffectedRows.get(i).getInsertCount());
            relation_cv.put(CREATION_DATE, currentDateandTime);
            db.insertOrThrow(TABLE_NAME_INSERTION_RELATION,null,relation_cv);
        }
        if(tobetakenout >0){
            return "(LIMIT EXCEEDED) " + res;
        }
        return String.valueOf(res);
    }
    public String IncreaseInventory(Integer amount, String type, String relid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<InventoryIncrease> increase = new ArrayList<>();
        Cursor temp = db.rawQuery("select " + ID + "," + INSERTION_ID +"," + PIECES + ", " +
                "(select "+TABLE_NAME_INSRTS+"."+REMAINING_PIECES+" from "+TABLE_NAME_INSRTS+" where "+TABLE_NAME_INSRTS+"."+ID+" = "+INSERTION_ID+")" +
                " from "+TABLE_NAME_INSERTION_RELATION + " where " + RELATION_ID + " = " + relid + " AND " + RELATION_TO + " LIKE '%"+type+"%'" +
                " ORDER BY (select "+TABLE_NAME_INSRTS+"."+CREATION_DATE+" from "+TABLE_NAME_INSRTS+" where "+TABLE_NAME_INSRTS+"."+ID+" = "+INSERTION_ID+") DESC",null);
        while (temp.moveToNext()) {
            increase.add(new InventoryIncrease(Integer.valueOf(temp.getString(0)),
                    Integer.valueOf(temp.getString(1)),
                    Integer.valueOf(temp.getString(2)),
                    Integer.valueOf(temp.getString(3))));
        }
        long res = 0;
        int tobereturned = amount;

        for(int i = 0 ; i < increase.size() ; i++){
            ContentValues cv = new ContentValues();
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss", Locale.getDefault());
            String currentDateandTime = sdf.format(Calendar.getInstance().getTime());
            cv.put(MODIFICATION_DATE , currentDateandTime);
            int oldcount = increase.get(i).getInsertChange();

            if(tobereturned != -1){
                if(tobereturned < oldcount){
                    cv.put(REMAINING_PIECES , increase.get(i).getInsertRemaining() + tobereturned);
                    ContentValues relation_cv = new ContentValues();
                    relation_cv.put(PIECES, oldcount - tobereturned);
                    tobereturned = 0;
                    relation_cv.put(MODIFICATION_DATE, currentDateandTime);
                    db.update(TABLE_NAME_INSERTION_RELATION,relation_cv,"ID = ?",new String[]{String.valueOf(increase.get(i).getLogID())});
                }else{
                    cv.put(REMAINING_PIECES , increase.get(i).getInsertRemaining() + oldcount);
                    db.execSQL("DELETE from " + TABLE_NAME_INSERTION_RELATION + " where " + ID + " = " + increase.get(i).getLogID());
                    tobereturned = tobereturned - oldcount;
                }
            }else{
                cv.put(REMAINING_PIECES , increase.get(i).getInsertRemaining() + oldcount);
                db.execSQL("DELETE from " + TABLE_NAME_INSERTION_RELATION + " where " + ID + " = " + increase.get(i).getLogID());
            }
            res = db.update(TABLE_NAME_INSRTS,cv,"ID = ?",new String[]{String.valueOf(increase.get(i).getInsertID())});
        }

        return String.valueOf(res);
    }

    //**************************************************************************************************************************************
    //Sales:
    public long insertSale (Double discount , ArrayList<PointOfSaleRow> items){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DISCOUNT , discount);
        Double total_wholesale = 0.0;
        Double total_retail = 0.0;
        for (int i = 0 ; i < items.size() ; i++) {
            total_wholesale = total_wholesale + items.get(i).getWholesaleSum();
            total_retail = total_retail + items.get(i).getSum();
        }
        cv.put(SALE_TOTAL_WHOLESALE , total_wholesale);
        cv.put(SALE_TOTAL_RETAIL , total_retail);
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(Calendar.getInstance().getTime());
        cv.put(CREATION_DATE , currentDateandTime);
        long res = db.insert(TABLE_NAME_SALES,null,cv);
        for (int i = 0 ; i < items.size() ; i++) {
            insertSaleItem(String.valueOf(res),items.get(i).getID(),items.get(i).getCode(),items.get(i).getCount(),items.get(i).getWholesalePrice(),items.get(i).getPrice(),items.get(i).getWholesaleSum(),items.get(i).getSum());
        }
        return res;
    }
    public String deleteSale(String id, Boolean grouped){
        SQLiteDatabase db = this.getWritableDatabase();
        if(grouped){
            deleteSaleitems(id);
            db.execSQL("DELETE from " + TABLE_NAME_SALES + " where " + ID + " = " + id);
            return "Deleted Sale";
        }else{
            db.execSQL("DELETE from " + TABLE_NAME_SALE_ITEM + " where " + ID + " = " + id);
            return "Deleted Item";
        }
    }
    public long updateSale (Double discount , ArrayList<PointOfSaleRow> items, String saleid){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DISCOUNT , discount);
        Double total_wholesale = 0.0;
        Double total_retail = 0.0;
        for (int i = 0 ; i < items.size() ; i++) {
            total_wholesale = total_wholesale + items.get(i).getWholesaleSum();
            total_retail = total_retail + items.get(i).getSum();
        }
        cv.put(SALE_TOTAL_WHOLESALE , total_wholesale);
        cv.put(SALE_TOTAL_RETAIL , total_retail);
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(Calendar.getInstance().getTime());
        cv.put(MODIFICATION_DATE , currentDateandTime);
        long res = db.update(TABLE_NAME_SALES,cv,"ID = ?",new String[]{saleid});
        deleteSaleitems(saleid);
        for (int i = 0 ; i < items.size() ; i++) {
            insertSaleItem(saleid,items.get(i).getID(),items.get(i).getCode(),items.get(i).getCount(),items.get(i).getWholesalePrice(),items.get(i).getPrice(),items.get(i).getWholesaleSum(),items.get(i).getSum());
        }
        return res;
    }
    public Cursor getAllSales(boolean grouped){
        SQLiteDatabase db = this.getWritableDatabase();
        if(grouped){
            return db.rawQuery("select "+ID+", " +
                    "(select COUNT("+TABLE_NAME_SALE_ITEM+"."+PRODUCT_ID+") from "+TABLE_NAME_SALE_ITEM + " where "+TABLE_NAME_SALE_ITEM+"."+SALE_ID+" = "+TABLE_NAME_SALES+"."+ID+") , " +
                    CREATION_DATE+" , "+ SALE_TOTAL_RETAIL + " , " + SALE_TOTAL_WHOLESALE + ", " +
                    "(select SUM("+TABLE_NAME_SALE_ITEM+"."+ITEM_COUNT+") from "+TABLE_NAME_SALE_ITEM + " where "+TABLE_NAME_SALE_ITEM+"."+SALE_ID+" = "+TABLE_NAME_SALES+"."+ID+"),  " +
                    DISCOUNT + ", " + SALE_TOTAL_RETAIL +
                    " from "+TABLE_NAME_SALES,null);
        }
        else{
            return db.rawQuery("select "+SALE_ID+", " +
                    "(select "+TABLE_NAME_PRODUCTS+"."+NAME+" from "+TABLE_NAME_PRODUCTS+" where "+TABLE_NAME_PRODUCTS+"."+ID+" = "+PRODUCT_ID+"), " +
                    CREATION_DATE+" , "+ITEM_RETAIL_PRICE_TOTAL+" , "+ITEM_WHOLESALE_PRICE_TOTAL+", " + ITEM_COUNT +
                    ", (select "+TABLE_NAME_SALES+"."+DISCOUNT+" from "+TABLE_NAME_SALES+" where "+TABLE_NAME_SALES+"."+ID+" = "+SALE_ID+") " +
                    ", (select "+TABLE_NAME_SALES+"."+SALE_TOTAL_RETAIL+" from "+TABLE_NAME_SALES+" where "+TABLE_NAME_SALES+"."+ID+" = "+SALE_ID+") " +
                    " from "+TABLE_NAME_SALE_ITEM,null);
        }
    }
    public Double getSaleDiscountFromSaleID(String id){
        SQLiteDatabase db = getReadableDatabase();
        Double i = 0.0;
        String query = "SELECT "+DISCOUNT+" FROM "+TABLE_NAME_SALES+" where "+ID+" LIKE '"+id+"'";
        Cursor cursor = db.rawQuery(query,  null);
        if (cursor != null) {
            cursor.moveToFirst();
            i = cursor.getString(0) == "" ? 0.0 : Double.valueOf(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return i;
    }

    //**************************************************************************************************************************************
    //Sale Items:
    public long insertSaleItem (String sale_id,String product_id,String code,Double count,Double wholesale,Double retail,Double wholesale_total,Double retail_total){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SALE_ID , sale_id);
        cv.put(PRODUCT_ID , product_id);
        cv.put(CODE , code);
        cv.put(ITEM_COUNT , count);
        cv.put(ITEM_WHOLESALE_PRICE , wholesale);
        cv.put(ITEM_WHOLESALE_PRICE_TOTAL , wholesale_total);
        cv.put(ITEM_RETAIL_PRICE , retail);
        cv.put(ITEM_RETAIL_PRICE_TOTAL , retail_total);
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(Calendar.getInstance().getTime());
        cv.put(CREATION_DATE , currentDateandTime);

        long res = db.insert(TABLE_NAME_SALE_ITEM,null,cv);
        DecreaseInventory(product_id,count.intValue(),"SALEITEM",String.valueOf(res));
        return res;
    }
    public String deleteSaleitems(String saleid){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor temp = getSaleItemsIDsFromSaleID(saleid);
        if(temp.getCount()!=0){
            while (temp.moveToNext()) {
                db.execSQL("DELETE from " + TABLE_NAME_SALE_ITEM + " where " + ID + " = " + temp.getString(0));
                IncreaseInventory(temp.getString(1)==null?0:(Double.valueOf(temp.getString(1))).intValue(),"SALEITEM",temp.getString(0));
            }
        }
        return "Deleted Items";
    }
    public Cursor getSaleItemsFromSaleID (String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select " +
                "(select "+TABLE_NAME_PRODUCTS+"."+NAME+" from "+TABLE_NAME_PRODUCTS+" where "+TABLE_NAME_PRODUCTS+"."+ID+" = "+PRODUCT_ID+") , "
                + ITEM_RETAIL_PRICE + " , " + ITEM_WHOLESALE_PRICE + " , " + ITEM_COUNT  +
                " from "+TABLE_NAME_SALE_ITEM + " where " + SALE_ID + " = " + id,null);
    }
    public Cursor getSaleItemsIDsFromSaleID (String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select " + ID + "," +  ITEM_COUNT +
                " from "+TABLE_NAME_SALE_ITEM + " where " + SALE_ID + " = " + id,null);
    }


    //**************************************************************************************************************************************
    //Diffs:
    public long insertDiff (String product_id ,Double count,  String type, Integer loss){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PRODUCT_ID , product_id);
        cv.put(DIFF_COUNT , count);
        cv.put(DIFF_TYPE , type);
        cv.put(DIFF_PIECE_PRICE_WHOLESALE , getLastWholesalePriceByProductID(product_id));
        cv.put(DIFF_PIECE_PRICE_RETAIL , getLastRetailPriceByProductID(product_id));
        cv.put(DIFF_TOTAL_PRICE_WHOLESALE , count* Double.valueOf(getLastWholesalePriceByProductID(product_id)));
        cv.put(DIFF_TOTAL_PRICE_RETAIL , count*Double.valueOf(getLastRetailPriceByProductID(product_id)));
        cv.put(LOSS,loss);
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(Calendar.getInstance().getTime());
        cv.put(CREATION_DATE , currentDateandTime);
        long res = db.insert(TABLE_NAME_DIFFS,null,cv);
        DecreaseInventory(product_id,count.intValue(),"DIFF" , String.valueOf(res));
        return res;
    }
    public long updateDiff (Double count, String type, String diffid, Integer loss){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Cursor tepmcursor = db.rawQuery("select "+TABLE_NAME_DIFFS+"."+DIFF_PIECE_PRICE_WHOLESALE+
                ","+ TABLE_NAME_DIFFS+"."+DIFF_PIECE_PRICE_RETAIL+
                ","+TABLE_NAME_DIFFS+"."+DIFF_COUNT+
                ","+TABLE_NAME_DIFFS+"."+PRODUCT_ID+
                " from "+TABLE_NAME_DIFFS+" where "+TABLE_NAME_DIFFS+"."+ID+" = "+diffid,  null);
        Double oldwholesale = 0.0, oldretail = 0.0 , oldcount = 0.0;
        String productid = "";
        if (tepmcursor != null) {
            tepmcursor.moveToFirst();
            oldwholesale = tepmcursor.getString(0) == "" ? 0.0 : Double.valueOf(tepmcursor.getString(0));
            oldretail = tepmcursor.getString(1) == "" ? 0.0 : Double.valueOf(tepmcursor.getString(1));
            oldcount= tepmcursor.getString(2) == "" ? 0.0 : Double.valueOf(tepmcursor.getString(2));
            productid = tepmcursor.getString(3);
        }
        cv.put(DIFF_TOTAL_PRICE_WHOLESALE , count*oldwholesale);
        cv.put(DIFF_TOTAL_PRICE_RETAIL , count*oldretail);
        cv.put(DIFF_COUNT , count);
        cv.put(DIFF_TYPE , type);
        cv.put(LOSS,loss);
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(Calendar.getInstance().getTime());
        cv.put(MODIFICATION_DATE , currentDateandTime);
        if(loss==1) {
            if(count > oldcount){
                DecreaseInventory(productid , (int) (count - oldcount) , "DIFF" , diffid);
            }
            else if(count < oldcount){
                IncreaseInventory((int) (oldcount - count) , "DIFF" , diffid);
            }
        }else{
//            if(count > oldcount){
//                IncreaseInventory((int) (count - oldcount) , "DIFF" , diffid);
//            }
//            else if(count < oldcount){
//                DecreaseInventory(productid , (int) (oldcount - count) , "DIFF" , diffid);
//            }
        }
        long res = db.update(TABLE_NAME_DIFFS,cv,"ID = ?",new String[]{diffid});
        return res;
    }
    public String deleteDiff(String diffid , Integer loss){
        SQLiteDatabase db = this.getWritableDatabase();
        if(loss==1){
            IncreaseInventory(-1,"DIFF",diffid);
        }

        db.execSQL("DELETE from " + TABLE_NAME_DIFFS + " where " + ID + " = " + diffid);
        return "Deleted Diff";
    }
    public Cursor getAllDiffs(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select "+ID+", "+PRODUCT_ID+", "+
                "(select "+TABLE_NAME_PRODUCTS+"."+NAME+" from "+TABLE_NAME_PRODUCTS+" where "+TABLE_NAME_PRODUCTS+"."+ID+" = "+TABLE_NAME_DIFFS+"."+PRODUCT_ID+"), " +
                DIFF_COUNT+", "+DIFF_TYPE+" , "+ DIFF_TOTAL_PRICE_WHOLESALE + ", " +CREATION_DATE + ", " +LOSS + " " +
                "from "+TABLE_NAME_DIFFS,null);
    }



}
