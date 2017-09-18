package com.mahathisol.salesapp.pojos;

import java.util.List;

/**
 * Created by HP on 08/18/2017.
 */

public class UploadTripProduct {

    private List<TripProductlist> tripProductlist;

    public List<TripProductlist> getTripProductlist() {
        return tripProductlist;
    }

    public void setTripProductlist(List<TripProductlist> tripProductlist) {
        this.tripProductlist = tripProductlist;
    }

    public static class TripProductlist {
        private String id;
        private String tripId;
        private String productId;
        private String openingStock;
        private String inwardQty;
        private String closingStock;

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
