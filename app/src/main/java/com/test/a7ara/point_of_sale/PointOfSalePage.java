package com.test.a7ara.point_of_sale;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class PointOfSalePage {

    private ArrayList<PointOfSaleRow> Items;
    private Double Total, Discount , Profit;
    private Chip Chip;

    public PointOfSalePage(ArrayList<PointOfSaleRow> items, Double total, Double discount , Double profit , Chip chip) {
        Items = items;
        Total = total;
        Discount = discount;
        Profit = profit;
        Chip = chip;
    }

    public ArrayList<PointOfSaleRow> getItems() {
        return Items;
    }

    public Double getDiscount() {
        return Discount;
    }

    public void setItems(ArrayList<PointOfSaleRow> items) {
        Items = items;
    }

    public void setTotal(Double total) {
        Total = total;
    }

    public void setDiscount(Double discount) {
        Discount = discount;
    }

    public void setProfit(Double profit) {
        Profit = profit;
    }

    public Double getTotal() {
        return Total;
    }

    public Double getProfit() {
        return Profit;
    }

    public com.google.android.material.chip.Chip getChip() {
        return Chip;
    }

    public void setChip(com.google.android.material.chip.Chip chip) {
        Chip = chip;
    }
}
