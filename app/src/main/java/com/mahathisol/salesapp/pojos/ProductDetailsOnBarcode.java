package com.mahathisol.salesapp.pojos;

/**
 * Created by HP on 4/13/2017.
 */

public class ProductDetailsOnBarcode {

    public void ProductDetails(String id, String productcode, String prCompanyId, String prTypeId, String prVariantId, String cgstRate, String sgstRate, String igstRate, String retailRate, String mrpRate, String barcodeno, String hsncode, String ptypename, String pvaraint, String pcatname) {
        this.id = id;
        this.productcode = productcode;
        this.prCompanyId = prCompanyId;
        this.prTypeId = prTypeId;
        this.prVariantId = prVariantId;
        this.cgstRate = cgstRate;
        this.sgstRate = sgstRate;
        this.igstRate = igstRate;
        this.retailRate = retailRate;
        this.mrpRate = mrpRate;
        this.barcodeno = barcodeno;
        this.hsncode = hsncode;
        this.pvaraint = pvaraint;
        this.ptypename = ptypename;
        this.pcatid = pcatname;
    }

    private String id;
    private String productcode;
    private String prCompanyId;
    private String prTypeId;
    private String prVariantId;
    private String cgstRate;
    private String sgstRate;
    private String igstRate;
    private String retailRate;
    private String mrpRate;
    private String barcodeno;
    private String hsncode;
    private String pvaraint;
    private String ptypename;
    private String pcatid;

    public String getPcatid() {
        return pcatid;
    }

    public void setPcatid(String pcatid) {
        this.pcatid = pcatid;
    }

    public String getPvaraint() {
        return pvaraint;
    }

    public void setPvaraint(String pvaraint) {
        this.pvaraint = pvaraint;
    }

    public String getPtypename() {
        return ptypename;
    }

    public void setPtypename(String ptypename) {
        this.ptypename = ptypename;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductcode() {
        return productcode;
    }

    public void setProductcode(String productcode) {
        this.productcode = productcode;
    }

    public String getPrCompanyId() {
        return prCompanyId;
    }

    public void setPrCompanyId(String prCompanyId) {
        this.prCompanyId = prCompanyId;
    }

    public String getPrTypeId() {
        return prTypeId;
    }

    public void setPrTypeId(String prTypeId) {
        this.prTypeId = prTypeId;
    }

    public String getPrVariantId() {
        return prVariantId;
    }

    public void setPrVariantId(String prVariantId) {
        this.prVariantId = prVariantId;
    }

    public String getCgstRate() {
        return cgstRate;
    }

    public void setCgstRate(String cgstRate) {
        this.cgstRate = cgstRate;
    }

    public String getSgstRate() {
        return sgstRate;
    }

    public void setSgstRate(String sgstRate) {
        this.sgstRate = sgstRate;
    }

    public String getIgstRate() {
        return igstRate;
    }

    public void setIgstRate(String igstRate) {
        this.igstRate = igstRate;
    }

    public String getRetailRate() {
        return retailRate;
    }

    public void setRetailRate(String retailRate) {
        this.retailRate = retailRate;
    }

    public String getMrpRate() {
        return mrpRate;
    }

    public void setMrpRate(String mrpRate) {
        this.mrpRate = mrpRate;
    }

    public String getBarcodeno() {
        return barcodeno;
    }

    public void setBarcodeno(String barcodeno) {
        this.barcodeno = barcodeno;
    }

    public String getHsncode() {
        return hsncode;
    }

    public void setHsncode(String hsncode) {
        this.hsncode = hsncode;
    }
}
