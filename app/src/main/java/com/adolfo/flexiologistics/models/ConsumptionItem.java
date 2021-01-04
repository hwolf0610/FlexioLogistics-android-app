package com.adolfo.flexiologistics.models;

public class ConsumptionItem {

    private String categoryID;
    private String itemID;
    private String atributo;
    private String cantidad;
    private String unidadID;
    private String bodegaID;

    public ConsumptionItem(String categoryID, String itemID, String atributo, String cantidad, String unidadID, String bodegaID) {
        this.categoryID = categoryID;
        this.itemID = itemID;
        this.atributo = atributo;
        this.cantidad = cantidad;
        this.unidadID = unidadID;
        this.bodegaID = bodegaID;
    }

    public String getCategoryID() { return categoryID; }

    public void setCategoryID(String categoryID) { this.categoryID = categoryID; }

    public String getItemID() { return itemID; }

    public void setItemID(String itemID) { this.itemID = itemID; }

    public String getAtributo() { return atributo; }

    public void setAtributo(String atributo) { this.atributo = atributo; }

    public String getCantidad() { return cantidad; }

    public void setCantidad(String cantidad) { this.cantidad = cantidad; }

    public String getUnidadID() { return unidadID; }

    public void setUnidadID(String unidadID) { this.unidadID = unidadID; }

    public String getBodegaID() { return bodegaID; }

    public void setBodegaID(String bodegaID) { this.bodegaID = bodegaID; }
}
