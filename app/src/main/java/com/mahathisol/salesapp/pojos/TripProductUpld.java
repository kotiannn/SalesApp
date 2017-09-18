package com.mahathisol.salesapp.pojos;

import java.util.List;

/**
 * Created by HP on 08/10/2017.
 */

public class TripProductUpld {

    private List<Tripproductlist> tripproductlist;

    public List<Tripproductlist> getTripproductlist() {
        return tripproductlist;
    }

    public void setTripproductlist(List<Tripproductlist> tripproductlist) {
        this.tripproductlist = tripproductlist;
    }

    public static class Tripproductlist {
        private String id;
        private String tripId;
        private String productId;
        private String openingStock;
        private String inwardQty;
        private String closingStock;
        private String loaddate;
        private String returnquantity;
        private String soldquantity;

        public String getSoldquantity() {
            return soldquantity;
        }

        public void setSoldquantity(String soldquantity) {
            this.soldquantity = soldquantity;
        }

        public String getLoaddate() {
            return loaddate;
        }

        public void setLoaddate(String loaddate) {
            this.loaddate = loaddate;
        }

        public String getReturnquantity() {
            return returnquantity;
        }

        public void setReturnquantity(String returnquantity) {
            this.returnquantity = returnquantity;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTripId() {
            return tripId;
        }

        public void setTripId(String tripId) {
            this.tripId = tripId;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getOpeningStock() {
            return openingStock;
        }

        public void setOpeningStock(String openingStock) {
            this.openingStock = openingStock;
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
    }
}
