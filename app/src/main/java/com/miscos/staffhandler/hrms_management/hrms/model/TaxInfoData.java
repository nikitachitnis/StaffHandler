package com.miscos.staffhandler.hrms_management.hrms.model;

public class TaxInfoData {
    String taxName;
    String taxValueOne;
    String taxValueTwo;

    public TaxInfoData(String taxName, String taxValueOne, String taxValueTwo) {
        this.taxName = taxName;
        this.taxValueOne = taxValueOne;
        this.taxValueTwo = taxValueTwo;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public String getTaxValueOne() {
        return taxValueOne;
    }

    public void setTaxValueOne(String taxValueOne) {
        this.taxValueOne = taxValueOne;
    }

    public String getTaxValueTwo() {
        return taxValueTwo;
    }

    public void setTaxValueTwo(String taxValueTwo) {
        this.taxValueTwo = taxValueTwo;
    }
}
