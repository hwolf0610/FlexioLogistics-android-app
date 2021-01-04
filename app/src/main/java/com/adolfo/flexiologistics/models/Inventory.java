package com.adolfo.flexiologistics.models;

public class Inventory {

    private int imgType;
    private String productID;
    private String name;
    private String subname;
    private String providerName;
    private String typeStr;
    private String dateStr;
    private String timeStr;

    public Inventory(int imgType, String productID, String name, String subname, String providerName, String typeStr, String dateStr, String timeStr) {
        this.imgType = imgType;
        this.productID = productID;
        this.name = name;
        this.subname = subname;
        this.providerName = providerName;
        this.typeStr = typeStr;
        this.dateStr = dateStr;
        this.timeStr = timeStr;
    }

    public int getImgType() { return imgType; }

    public void setImgType(int imgType) { this.imgType = imgType; }

    public String getProductID() { return productID; }

    public void setProductID(String productID) { this.productID = productID; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getSubname() { return subname; }

    public void setSubname(String subname) { this.subname = subname; }

    public String getProviderName() { return providerName; }

    public void setProviderName(String providerName) { this.providerName = providerName; }

    public String getTypeStr() { return typeStr; }

    public void setTypeStr(String typeStr) { this.typeStr = typeStr; }

    public String getDateStr() { return dateStr; }

    public void setDateStr(String dateStr) { this.dateStr = dateStr; }

    public String getTimeStr() { return timeStr; }

    public void setTimeStr(String timeStr) { this.timeStr = timeStr; }
}
