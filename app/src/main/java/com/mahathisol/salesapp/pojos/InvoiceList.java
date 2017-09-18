package com.mahathisol.salesapp.pojos;

/**
 * Created by HP on 08/23/2017.
 */

public class InvoiceList {
    private String id;
    private String invoiceNo;
    private String invoiceDate;
    private String customerId;
    private String cuName;
    private String cuMobile;
    private String cuEmailid;
    private String cuAddress1;
    private String cuAddress2;
    private String cuAddress3;
    private String cuCity;
    private String cuStaeid;
    private String cuGSTINNO;
    private String companyId;
    private String cName;
    private String cMobile;
    private String cEmailid;
    private String cAddress1;
    private String cAddress2;
    private String cAddress3;
    private String ccity;
    private String cpincode;
    private String cstateid;
    private String cGSTINNo;
    private String cTaxIncludedAmt;
    private String reportFooter;
    private String showbankdetails;
    private String accHolderName;
    private String accNo;
    private String bankname;
    private String bankbranch;
    private String ifsccode;
    private String inHeader;
    private String paymentmodeid;
    private String amountPaid;
    private String instrNum;
    private String instrDate;
    private String remarks;

    public String getPaymentmodeid() {
        return paymentmodeid;
    }

    public void setPaymentmodeid(String paymentmodeid) {
        this.paymentmodeid = paymentmodeid;
    }

    public String getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getInstrNum() {
        return instrNum;
    }

    public void setInstrNum(String instrNum) {
        this.instrNum = instrNum;
    }

    public String getInstrDate() {
        return instrDate;
    }

    public void setInstrDate(String instrDate) {
        this.instrDate = instrDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void Invoice(String id, String invoiceNo, String invoiceDate, String customerId, String cuName, String cuMobile, String cuEmailid, String cuCity, String cuGSTINNO) {
        this.id = id;
        this.invoiceNo = invoiceNo;
        this.invoiceDate = invoiceDate;
        this.customerId = customerId;
        this.cuName = cuName;
        this.cuMobile = cuMobile;
        this.cuEmailid = cuEmailid;
        this.cuCity = cuCity;
        this.cuGSTINNO = cuGSTINNO;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCuName() {
        return cuName;
    }

    public void setCuName(String cuName) {
        this.cuName = cuName;
    }

    public String getCuMobile() {
        return cuMobile;
    }

    public void setCuMobile(String cuMobile) {
        this.cuMobile = cuMobile;
    }

    public String getCuEmailid() {
        return cuEmailid;
    }

    public void setCuEmailid(String cuEmailid) {
        this.cuEmailid = cuEmailid;
    }

    public String getCuCity() {
        return cuCity;
    }

    public void setCuCity(String cuCity) {
        this.cuCity = cuCity;
    }

    public String getCuGSTINNO() {
        return cuGSTINNO;
    }

    public void setCuGSTINNO(String cuGSTINNO) {
        this.cuGSTINNO = cuGSTINNO;
    }
}
