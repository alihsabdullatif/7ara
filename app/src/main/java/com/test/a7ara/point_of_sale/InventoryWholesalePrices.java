package com.test.a7ara.point_of_sale;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InventoryWholesalePrices {
    private String InsertionID;
    private Double Price;
    private Double TotalCount;
    private Double RemainingCount;
    private String InventoryInsertionDate;

    public InventoryWholesalePrices(String insertion_id,Double price, Double count,Double remaining, String inventoryInsertionDate) {
        InsertionID = insertion_id;
        Price = price;
        TotalCount = count;
        RemainingCount = remaining;
        InventoryInsertionDate = inventoryInsertionDate;
    }

    public String getInsertionID() {
        return InsertionID;
    }

    public Double getPrice() {
        return Price;
    }

    public Double getCount() {
        return TotalCount;
    }

    public void setTotalCount(Double totalCount) {
        TotalCount = totalCount;
    }

    public void additem(Double a){
        RemainingCount=RemainingCount+a;
    }

    public void removeitem(Double a){
        RemainingCount=RemainingCount-a;
    }

    public String getInventoryInsertionDate() {
        return InventoryInsertionDate;
    }

    public Double getRemainingCount() {
        return RemainingCount;
    }

    public Date getDate(){
        SimpleDateFormat fullformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fullDate = null;
        try {
            if(InventoryInsertionDate==null){
                InventoryInsertionDate = "0001-01-01 00:00:00";
            }
            fullDate = fullformat.parse(InventoryInsertionDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return fullDate;
    }

    public String getDateTime() {
        SimpleDateFormat timeformat = new SimpleDateFormat("yy/MM/dd", new Locale("ar"));
        return timeformat.format(getDate());
    }

    public void setRemainingCount(Double remainingcount) {
        RemainingCount = remainingcount;
    }
}
