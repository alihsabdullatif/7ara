package com.test.a7ara;

public class InventoryIncrease {
    private Integer LogID;
    private Integer InsertID;
    private Integer InsertRemaining;
    private Integer InsertChange;
    private Integer NewCount;

    public InventoryIncrease(Integer logid , Integer insertID,Integer insertchange , Integer remaining) {
        LogID = logid;
        InsertID = insertID;
        InsertRemaining = remaining;
        InsertChange = insertchange;
    }


    public Integer getLogID() {
        return LogID;
    }

    public void setInsertID(Integer insertID) {
        InsertID = insertID;
    }

    public Integer getInsertID() {
        return InsertID;
    }

    public Integer getInsertChange() {
        return InsertChange;
    }

    public Integer getInsertRemaining() {
        return InsertRemaining;
    }

    public void setInsertChange(Integer insertRemaining) {
        InsertRemaining = insertRemaining;
    }

    public Integer getNewCount() {
        return NewCount;
    }

    public void setNewCount(Integer newCount) {
        NewCount = newCount;
    }
}
