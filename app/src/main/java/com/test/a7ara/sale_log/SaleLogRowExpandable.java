package com.test.a7ara.sale_log;

import static java.lang.String.format;

public class SaleLogRowExpandable {
    private String Name;
    private Double ItemRetailPrice;
    private Double ItemWholesalePrice;
    private Double Count;
    private Double TotalRetailPrice;
    private Double TotalWholesalePrice;

    public SaleLogRowExpandable(String name, Double itemretailprice, Double itemwholesaleprice, Double count) {
        Name = name;
        ItemRetailPrice = itemretailprice;
        ItemWholesalePrice = itemwholesaleprice;
        Count = count;
        TotalRetailPrice = ItemRetailPrice * Count;
        TotalWholesalePrice = ItemWholesalePrice * Count;
    }

    public String getName() {
        return Name;
    }

    public Double getItemRetailPrice() {
        return ItemRetailPrice;
    }

    public Double getItemWholesalePrice() {
        return ItemWholesalePrice;
    }

    public Double getCount() {
        return Count;
    }

    public Double getTotalRetailPrice() {
        return TotalRetailPrice;
    }

    public Double getTotalWholesalePrice() {
        return TotalWholesalePrice;
    }

    public Double getTotalProfit(){
        return TotalRetailPrice - TotalWholesalePrice;
    }
}
