package com.test.a7ara.product_list;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProductListRow {

    private String ID;
    private String Name;
    private String CreationDate;
    private Double Sales;
    private Double Diffs;
    private Double Inserts;
    private Boolean ByWeight;

    public ProductListRow(String id, String name, String category, Boolean byweight, String creationdate, String code, Double sales , Double diffs , Double inserts){
        ID = id;
        Name = name;
        ByWeight = byweight;
        CreationDate = creationdate;
        Sales = sales;
        Diffs = diffs;
        Inserts = inserts;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return Name;
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


    public Double getSales() {
        return Sales;
    }

    public Double getDiffs() {
        return Diffs;
    }

    public Double getInserts() {
        return Inserts;
    }

    public Double getInventory() {
        return Inserts - Sales - Diffs;
    }

    public Boolean getByWeight() {
        return ByWeight;
    }
}
