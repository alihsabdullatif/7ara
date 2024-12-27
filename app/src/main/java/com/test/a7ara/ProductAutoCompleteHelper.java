package com.test.a7ara;

import java.util.ArrayList;

public class ProductAutoCompleteHelper {
    private ArrayList<String> Ids;
    private ArrayList<String> Names;
    private ArrayList<String> Codes;
    private ArrayList<String> Inventories;

    public ProductAutoCompleteHelper(ArrayList<String> ids, ArrayList<String> names, ArrayList<String> codes, ArrayList<String> inventories) {
        Ids = ids;
        Names = names;
        Codes = codes;
        Inventories = inventories;
    }
    public ArrayList<String> getIds() {
        return Ids;
    }
    public ArrayList<String> getNames() {
        return Names;
    }
    public ArrayList<String> getCodes() {
        return Codes;
    }

    public ArrayList<String> getInventories() {
        return Inventories;
    }
}
