package com.test.a7ara.inventory_insertion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class InsertListRow {

    private String ID;
    private String ProductId;
    private String ProductName;
    private Double InsertCount;
    private Double InsertRemainingCount;
    private Double InsertWholesalePrice;
    private String CreationDate;
    private String NestedList;

    public InsertListRow(String ID, String productId, String productName, Double insertCount,
                         Double insertRemainingCount, Double insertWholesalePrice, String creationDate,
                         String nested) {
        this.ID = ID;
        ProductId = productId;
        ProductName = productName;
        InsertCount = insertCount;
        InsertRemainingCount = insertRemainingCount;
        InsertWholesalePrice = insertWholesalePrice;
        CreationDate = creationDate;
        NestedList = nested;
    }

    public String getID() {
        return ID;
    }

    public String getProductName() {
        return ProductName;
    }

    public int getInsertCount() {
        return InsertCount.intValue();
    }

    public Double getInsertWholesalePrice() {
        return InsertWholesalePrice;
    }

    public Date getDate(){
        SimpleDateFormat fullformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fullDate = null;
        try {
            fullDate = fullformat.parse(CreationDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return fullDate;
    }

    public String getCreationDateTime() {
        SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd EEE hh:mm a", new Locale("ar"));
        return timeformat.format(getDate());
    }

    public int getInsertRemainingCount() {
        return InsertRemainingCount.intValue();
    }

    public String getNestedList() {
        return NestedList;
    }
}
