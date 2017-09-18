package com.mahathisol.salesapp.pojos;

import java.util.List;

/**
 * Created by HP on 08/16/2017.
 */

public class InvoiceDetails {


    private List<Invlist> invlist;

    public List<Invlist> getInvlist() {
        return invlist;
    }

    public void setInvlist(List<Invlist> invlist) {
        this.invlist = invlist;
    }

    public static class Invlist {
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
        private String prid;

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

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getPHSNcode() {
            return pHSNcode;
        }

        public void setPHSNcode(String pHSNcode) {
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

        public String getCGSTRate() {
            return cGSTRate;
        }

        public void setCGSTRate(String cGSTRate) {
            this.cGSTRate = cGSTRate;
        }

        public String getCGStAmount() {
            return cGStAmount;
        }

        public void setCGStAmount(String cGStAmount) {
            this.cGStAmount = cGStAmount;
        }

        public String getSGSTRate() {
            return sGSTRate;
        }

        public void setSGSTRate(String sGSTRate) {
            this.sGSTRate = sGSTRate;
        }

        public String getSGSTAmount() {
            return sGSTAmount;
        }

        public void setSGSTAmount(String sGSTAmount) {
            this.sGSTAmount = sGSTAmount;
        }

        public String getIGSTRate() {
            return iGSTRate;
        }

        public void setIGSTRate(String iGSTRate) {
            this.iGSTRate = iGSTRate;
        }

        public String getIGSTAmount() {
            return iGSTAmount;
        }

        public void setIGSTAmount(String iGSTAmount) {
            this.iGSTAmount = iGSTAmount;
        }

        public String getActive() {
            return active;
        }

        public void setActive(String active) {
            this.active = active;
        }

        public String getPTypeName() {
            return pTypeName;
        }

        public void setPTypeName(String pTypeName) {
            this.pTypeName = pTypeName;
        }

        public String getPTypeId() {
            return pTypeId;
        }

        public void setPTypeId(String pTypeId) {
            this.pTypeId = pTypeId;
        }

        public String getPVariantName() {
            return pVariantName;
        }

        public void setPVariantName(String pVariantName) {
            this.pVariantName = pVariantName;
        }

        public String getPVarintId() {
            return pVarintId;
        }

        public void setPVarintId(String pVarintId) {
            this.pVarintId = pVarintId;
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

        public String getCTaxIncludedAmt() {
            return cTaxIncludedAmt;
        }

        public void setCTaxIncludedAmt(String cTaxIncludedAmt) {
            this.cTaxIncludedAmt = cTaxIncludedAmt;
        }

        public String getReportFooter() {
            return reportFooter;
        }

        public void setReportFooter(String reportFooter) {
            this.reportFooter = reportFooter;
        }

        public String getShowbankdetails() {
            return showbankdetails;
        }

        public void setShowbankdetails(String showbankdetails) {
            this.showbankdetails = showbankdetails;
        }

        public String getAccHolderName() {
            return accHolderName;
        }

        public void setAccHolderName(String accHolderName) {
            this.accHolderName = accHolderName;
        }

        public String getAccNo() {
            return accNo;
        }

        public void setAccNo(String accNo) {
            this.accNo = accNo;
        }

        public String getBankname() {
            return bankname;
        }

        public void setBankname(String bankname) {
            this.bankname = bankname;
        }

        public String getBankbranch() {
            return bankbranch;
        }

        public void setBankbranch(String bankbranch) {
            this.bankbranch = bankbranch;
        }

        public String getIfsccode() {
            return ifsccode;
        }

        public void setIfsccode(String ifsccode) {
            this.ifsccode = ifsccode;
        }

        public String getInHeader() {
            return inHeader;
        }

        public void setInHeader(String inHeader) {
            this.inHeader = inHeader;
        }

        public String getPrid() {
            return prid;
        }

        public void setPrid(String prid) {
            this.prid = prid;
        }
    }
}
