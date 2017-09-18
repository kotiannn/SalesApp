package com.mahathisol.salesapp.pojos;

/**
 * Created by Admin on 31-08-2017.
 */

public class ProductList {
    private String prtype;
    private String prvariant;
    private String inwardQty;
    private String closingStock;
    private String openingStock;
    private String returnquantity;
    private String soldquantity;

    public String getPrtype() {
        return prtype;
    }

    public void setPrtype(String prtype) {
        this.prtype = prtype;
    }

    public String getPrvariant() {
        return prvariant;
    }

    public void setPrvariant(String prvariant) {
        this.prvariant = prvariant;
    }

    public String getInwardQty() {
        return inwardQty;
    }

    public void setInwardQty(String inwardQty) {
        this.inwardQty = inwardQty;
    }

    public String getClosingStock() {
        return closingStock;
    }

    public void setClosingStock(String closingStock) {
        this.closingStock = closingStock;
    }

    public String getOpeningStock() {
        return openingStock;
    }

    public void setOpeningStock(String openingStock) {
        this.openingStock = openingStock;
    }

    public String getreturnquantity() {
        return returnquantity;
    }

    public void setreturnquantity(String openingStock) {
        this.returnquantity = openingStock;
    }

    public String getsoldquantity() {
        return soldquantity;
    }

    public void setsoldquantity(String soldquantity) {
        this.soldquantity = soldquantity;
    }

    public void setall(String prtype, String prvariant, String inwardQty, String closingStock, String openingStock, String returnqty, String soldqty) {
        this.prtype = prtype;
        this.prvariant = prvariant;
        this.inwardQty = inwardQty;
        this.closingStock = closingStock;
        this.openingStock = openingStock;
        this.returnquantity = returnqty;
        this.soldquantity = soldqty;
    }
}
