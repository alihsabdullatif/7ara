package com.test.a7ara.sale_log;

import static android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SaleLogRow {

    private String ID;
    private String Title;
    private String CreationDate;
    private Double RetailAmount;
    private Double WholesaleAmount;
    private Double Profit;
    private Double Count;
    private Double Discount;
    private ArrayList<SaleLogRowExpandable> NestedList;
    private Boolean isExpandable;

    public SaleLogRow(String id, String title, String creationdate, Double retailamount, Double wholesaleamount, Double profit,Double count, Double discount,ArrayList<SaleLogRowExpandable> nestedlist){
        ID = id;
        Title = title;
        CreationDate = creationdate;
        RetailAmount = retailamount;
        WholesaleAmount = wholesaleamount;
        Profit = profit;
        Count = count;
        Discount = discount;
        NestedList = nestedlist;
        isExpandable = false;
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

    public String getCreationTime() {
        SimpleDateFormat timeformat = new SimpleDateFormat("hh:mm a", new Locale("ar"));
        return timeformat.format(getDate());
    }

    public String getCreationDate_notime() {
        SimpleDateFormat dateformate = new SimpleDateFormat("yyyy-MM-dd EEE", new Locale("ar"));
        return dateformate.format(getDate());
    }

    public String getAgo(){
        long cuMillis = System.currentTimeMillis();
        return (String) DateUtils.getRelativeTimeSpanString(getDate().getTime(), cuMillis,1,FORMAT_ABBREV_RELATIVE);
    }

    public String getID() {
        return ID;
    }

    public String getTitle() {
        return Title;
    }

    public String getCreationDate() {
        return CreationDate;
    }

    public Double getRetailAmount() {
        return RetailAmount;
    }

    public Double getProfit() {
        return Profit;
    }

    public ArrayList<SaleLogRowExpandable> getNestedList() {
        return NestedList;
    }

    public Boolean getExpandable() {
        return isExpandable;
    }

    public void setExpandable(Boolean expandable) {
        isExpandable = expandable;
    }

    public Double getCount() {
        return Count;
    }

    public Double getWholesaleAmount() {
        return WholesaleAmount;
    }

    public void setWholesaleAmount(Double wholesaleAmount) {
        WholesaleAmount = wholesaleAmount;
    }

    public Double getDiscount() {
        return Discount;
    }

    public void setDiscount(Double discount) {
        Discount = discount;
    }
}
