package com.adolfo.flexiologistics.models;

public class Sale {

    private int itemID;
    private String code;
    private String clientName;

    public Sale(int itemID, String code, String clientName) {
        this.itemID = itemID;
        this.code = code;
        this.clientName = clientName;
    }

    public int getItemID() { return itemID; }

    public void setItemID(int itemID) { this.itemID = itemID; }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public String getClientName() { return clientName; }

    public void setClientName(String clientName) { this.clientName = clientName; }
}
