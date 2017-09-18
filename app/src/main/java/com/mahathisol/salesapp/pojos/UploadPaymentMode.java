package com.mahathisol.salesapp.pojos;

import java.util.List;

/**
 * Created by HP on 09/02/2017.
 */

public class UploadPaymentMode {


    private List<Paymentmodeli> paymentmodeli;

    public List<Paymentmodeli> getPaymentmodeli() {
        return paymentmodeli;
    }

    public void setPaymentmodeli(List<Paymentmodeli> paymentmodeli) {
        this.paymentmodeli = paymentmodeli;
    }

    public static class Paymentmodeli {
        private String id;
        private String description;
        private String active;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getActive() {
            return active;
        }

        public void setActive(String active) {
            this.active = active;
        }
    }
}
