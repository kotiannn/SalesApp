package com.mahathisol.salesapp.pojos;

/**
 * Created by HP on 09/01/2017.
 */

public class InvoiceDetailsPrint {

    private String id;
    private String invoiceId;
    private String productId;
    private String pHSNcode;
    private String productcode;
    private String quantity;
    private String rate;
    private String amount;
    private String cGSTRate;
    private String cGStAmount;
    private String sGSTRate;
    private String sGSTAmount;
    private String iGSTRate;
    private String iGSTAmount;
    private String active;
    private String pTypeName;
    private String pTypeId;
    private String pVariantName;
    private String pVarintId;
    private String taxableRate;
    private String taxableAmount;
    public void setAll(String id, String invoiceId, String productId, String pHSNcode, String productcode, String quantity, String rate, String amount, String cGSTRate, String cGStAmount, String sGSTRate, String sGSTAmount, String iGSTRate, String iGSTAmount, String active, String pTypeName, String pTypeId, String pVariantName, String pVarintId,String tr,String ta) {
        this.id = id;
        this.invoiceId = invoiceId;
        this.productId = productId;
        this.pHSNcode = pHSNcode;
        this.productcode = productcode;
        this.quantity = quantity;
        this.rate = rate;
        this.amount = amount;
        this.cGSTRate = cGSTRate;
        this.cGStAmount = cGStAmount;
        this.sGSTRate = sGSTRate;
        this.sGSTAmount = sGSTAmount;
        this.iGSTRate = iGSTRate;
        this.iGSTAmount = iGSTAmount;
        this.active = active;
        this.pTypeName = pTypeName;
        this.pTypeId = pTypeId;
        this.pVariantName = pVariantName;
        this.pVarintId = pVarintId;
        this.taxableRate = tr;
        this.taxableAmount = ta;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getTaxableRate() {
        return taxableRate;
    }

    public void setTaxableRate(String taxableRate) {
        this.taxableRate = taxableRate;
    }

    public String getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(String taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getpHSNcode() {
        return pHSNcode;
    }

    public void setpHSNcode(String pHSNcode) {
        this.pHSNcode = pHSNcode;
    }

    public String getProductcode() {
        return productcode;
    }

    public void setProductcode(String productcode) {
        this.productcode = productcode;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getcGSTRate() {
        return cGSTRate;
    }

    public void setcGSTRate(String cGSTRate) {
        this.cGSTRate = cGSTRate;
    }

    public String getcGStAmount() {
        return cGStAmount;
    }

    public void setcGStAmount(String cGStAmount) {
        this.cGStAmount = cGStAmount;
    }

    public String getsGSTRate() {
        return sGSTRate;
    }

    public void setsGSTRate(String sGSTRate) {
        this.sGSTRate = sGSTRate;
    }

    public String getsGSTAmount() {
        return sGSTAmount;
    }

    public void setsGSTAmount(String sGSTAmount) {
        this.sGSTAmount = sGSTAmount;
    }

    public String getiGSTRate() {
        return iGSTRate;
    }

    public void setiGSTRate(String iGSTRate) {
        this.iGSTRate = iGSTRate;
    }

    public String getiGSTAmount() {
        return iGSTAmount;
    }

    public void setiGSTAmount(String iGSTAmount) {
        this.iGSTAmount = iGSTAmount;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getpTypeName() {
        return pTypeName;
    }

    public void setpTypeName(String pTypeName) {
        this.pTypeName = pTypeName;
    }

    public String getpTypeId() {
        return pTypeId;
    }

    public void setpTypeId(String pTypeId) {
        this.pTypeId = pTypeId;
    }

    public String getpVariantName() {
        return pVariantName;
    }

    public void setpVariantName(String pVariantName) {
        this.pVariantName = pVariantName;
    }

    public String getpVarintId() {
        return pVarintId;
    }

    public void setpVarintId(String pVarintId) {
        this.pVarintId = pVarintId;
    }
}
