package com.test.a7ara.point_of_sale;

import java.util.ArrayList;

public class ItemInventory {
    private String ItemID;
    private Double RemainingInPOS;
    private ArrayList<InventoryWholesalePrices> WholesalePrice;

    public ItemInventory(String itemID, Double remainingInPOS,ArrayList<InventoryWholesalePrices> wholesale) {
        ItemID = itemID;
        RemainingInPOS = remainingInPOS;
        WholesalePrice = wholesale;
    }

    public ArrayList<InventoryWholesalePrices> getWholesalePrice() {
        return WholesalePrice;
    }



    public String getItemID() {
        return ItemID;
    }

    public Double getRemainingInPOS() {
        return RemainingInPOS;
    }

    public void TakeFromInv(Double amount){
        RemainingInPOS = RemainingInPOS - amount;
    }

    public void AddToInv(Double amount){
        RemainingInPOS = RemainingInPOS + amount;
    }

    public void setRemainingInPOS(Double remainingInPOS) {
        RemainingInPOS = remainingInPOS;
    }
}
