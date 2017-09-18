package com.mahathisol.salesapp.pojos;

/**
 * Created by HP on 09/02/2017.
 */

public class InvDetailsGroupByHSN {

    private String pHSNCode;
    private String tax;
    private String cGSTRate;
    private String cGSTAmount;
    private String sGSTRate;
    private String sGSTAmount;
    private String total;

    public void setAll(String pHSNCode, String tax, String cGSTRate, String cGSTAmount, String sGSTRate, String sGSTAmount, String total) {
        this.pHSNCode = pHSNCode;
        this.tax = tax;
        this.cGSTRate = cGSTRate;
        this.cGSTAmount = cGSTAmount;
        this.sGSTRate = sGSTRate;
        this.sGSTAmount = sGSTAmount;
        this.total = total;
    }

    public String getpHSNCode() {
        return pHSNCode;
    }

    public void setpHSNCode(String pHSNCode) {
        this.pHSNCode = pHSNCode;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getcGSTRate() {
        return cGSTRate;
    }

    public void setcGSTRate(String cGSTRate) {
        this.cGSTRate = cGSTRate;
    }

    public String getcGSTAmount() {
        return cGSTAmount;
    }

    public void setcGSTAmount(String cGSTAmount) {
        this.cGSTAmount = cGSTAmount;
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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
