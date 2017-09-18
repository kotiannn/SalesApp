package com.mahathisol.salesapp.pojos;

import java.util.List;

/**
 * Created by HP on 07/31/2017.
 */

public class InsertInvoice {


    private List<Value> value;

    public List<Value> getValue() {
        return value;
    }

    public void setValue(List<Value> value) {
        this.value = value;
    }

    public static class Value {
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
        private String paymentmodeid;
        private String amountPaid;
        private String instrNum;
        private String instrDate;
        private String remarks;

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

        public String getCuAddress1() {
            return cuAddress1;
        }

        public void setCuAddress1(String cuAddress1) {
            this.cuAddress1 = cuAddress1;
        }

        public String getCuAddress2() {
            return cuAddress2;
        }

        public void setCuAddress2(String cuAddress2) {
            this.cuAddress2 = cuAddress2;
        }

        public String getCuAddress3() {
            return cuAddress3;
        }

        public void setCuAddress3(String cuAddress3) {
            this.cuAddress3 = cuAddress3;
        }

        public String getCuCity() {
            return cuCity;
        }

        public void setCuCity(String cuCity) {
            this.cuCity = cuCity;
        }

        public String getCuStaeid() {
            return cuStaeid;
        }

        public void setCuStaeid(String cuStaeid) {
            this.cuStaeid = cuStaeid;
        }

        public String getCuGSTINNO() {
            return cuGSTINNO;
        }

        public void setCuGSTINNO(String cuGSTINNO) {
            this.cuGSTINNO = cuGSTINNO;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public String getCName() {
            return cName;
        }

        public void setCName(String cName) {
            this.cName = cName;
        }

        public String getCMobile() {
            return cMobile;
        }

        public void setCMobile(String cMobile) {
            this.cMobile = cMobile;
        }

        public String getCEmailid() {
            return cEmailid;
        }

        public void setCEmailid(String cEmailid) {
            this.cEmailid = cEmailid;
        }

        public String getCAddress1() {
            return cAddress1;
        }

        public void setCAddress1(String cAddress1) {
            this.cAddress1 = cAddress1;
        }

        public String getCAddress2() {
            return cAddress2;
        }

        public void setCAddress2(String cAddress2) {
            this.cAddress2 = cAddress2;
        }

        public String getCAddress3() {
            return cAddress3;
        }

        public void setCAddress3(String cAddress3) {
            this.cAddress3 = cAddress3;
        }

        public String getCcity() {
            return ccity;
        }

        public void setCcity(String ccity) {
            this.ccity = ccity;
        }

        public String getCpincode() {
            return cpincode;
        }

        public void setCpincode(String cpincode) {
            this.cpincode = cpincode;
        }

        public String getCstateid() {
            return cstateid;
        }

        public void setCstateid(String cstateid) {
            this.cstateid = cstateid;
        }

        public String getCGSTINNo() {
            return cGSTINNo;
        }

        public void setCGSTINNo(String cGSTINNo) {
            this.cGSTINNo = cGSTINNo;
        }

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
    }
}
