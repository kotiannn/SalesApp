package com.mahathisol.salesapp.pojos;

import java.util.List;

/**
 * Created by HP on 08/10/2017.
 */

public class UploadProduct {


    private List<Prlist> prlist;

    public List<Prlist> getPrlist() {
        return prlist;
    }

    public void setPrlist(List<Prlist> prlist) {
        this.prlist = prlist;
    }

    public static class Prlist {
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
}
