package com.test.a7ara;

public class InventoryDecrease {
    private Integer InsertID;
    private Integer InsertCount;
    private Integer InsertRemaining;

    public InventoryDecrease(Integer insertID, Integer insertCount, Integer insertRemaining) {
        InsertID = insertID;
        InsertCount = insertCount;
        InsertRemaining = insertRemaining;
    }

    public Integer getInsertID() {
        return InsertID;
    }

    public Integer getInsertRemaining() {
        return InsertRemaining;
    }

    public void setInsertRemaining(Integer insertRemaining) {
        InsertRemaining = insertRemaining;
    }

    public Integer getInsertCount() {
        return InsertCount;
    }

    public void setInsertCount(Integer insertCount) {
        InsertCount = insertCount;
    }
}
