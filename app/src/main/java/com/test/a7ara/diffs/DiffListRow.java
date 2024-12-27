package com.test.a7ara.diffs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DiffListRow {

    private String ID;
    private String ProductId;
    private String ProductName;
    private Double DiffCount;
    private String DiffType;
    private Integer Loss;
    private Double DiffWholesalePrice;
    private String CreationDate;

    public DiffListRow(String ID, String productid,String productName, Double diffCount,String diffType, Double diffWholesalePrice, String creationDate, Integer loss) {
        this.ID = ID;
        ProductId = productid;
        ProductName = productName;
        DiffCount = diffCount;
        DiffType = diffType;
        DiffWholesalePrice = diffWholesalePrice;
        CreationDate = creationDate;
        Loss = loss;
    }

    public String getID() {
        return ID;
    }

    public String getProductId() {
        return ProductId;
    }

    public String getProductName() {
        return ProductName;
    }

    public Double getDiffCount() {
        return DiffCount;
    }

    public String getDiffType() {
        return DiffType;
    }

    public Integer getLoss() {
        return Loss;
    }

    public Double getDiffWholesalePrice() {
        return DiffWholesalePrice;
    }

    public String getCreationDate() {
        return CreationDate;
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
}
