package com.adolfo.flexiologistics.models;


public class DeliveryItem {

    private String itemID;
    private String serverName;
    private int count;
    private float packed;
    private int warehouseID, lineItemID, unidadID, id;
    private String itemBarcode;

    public DeliveryItem(String itemID, String serverName, int count, float packed, int warehouseID, int lineItemID, int unidadID, int id, String itemBarcode) {
        this.itemID = itemID;
        this.serverName = serverName;
        this.count = count;
        this.packed = packed;
        this.warehouseID = warehouseID;
        this.lineItemID = lineItemID;
        this.unidadID = unidadID;
        this.id = id;
        this.itemBarcode = itemBarcode;
    }

    public String getItemID() { return itemID; }

    public void setItemID(String itemID) { this.itemID = itemID; }

    public String getServerName() { return serverName; }

    public void setServerName(String serverName) { this.serverName = serverName; }

    public int getCount() { return count; }

    public void setCount(int count) { this.count = count; }

    public float getPacked() { return packed; }

    public void setPacked(float packed) { this.packed = packed; }

    public int getWarehouseID() { return warehouseID; }

    public void setWarehouseID(int warehouseID) { this.warehouseID = warehouseID; }

    public int getLineItemID() { return lineItemID; }

    public void setLineItemID(int lineItemID) { this.lineItemID = lineItemID; }

    public int getUnidadID() { return unidadID; }

    public void setUnidadID(int unidadID) { this.unidadID = unidadID; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getItemBarcode() { return itemBarcode; }

    public void setItemBarcode(String itemBarcode) { this.itemBarcode = itemBarcode; }
}
