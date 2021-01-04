package com.adolfo.flexiologistics.models;

public class Company {

    private String companyID;
    private String companyName;

    public Company(String companyID, String companyName) {
        this.companyID = companyID;
        this.companyName = companyName;
    }

    public String getCompanyID() { return companyID; }

    public void setCompanyID(String companyID) { this.companyID = companyID; }

    public String getCompanyName() { return companyName; }

    public void setCompanyName(String companyName) { this.companyName = companyName; }

}
