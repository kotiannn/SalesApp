package com.mahathisol.salesapp.pojos;

import java.util.List;

/**
 * Created by Admin on 04-08-2017.
 */

public class Dieselli {

    private List<Diesellist> diesellist;

    public List<Diesellist> getDiesellist() {
        return diesellist;
    }

    public void setDiesellist(List<Diesellist> diesellist) {
        this.diesellist = diesellist;
    }

    public static class Diesellist {
        private String id;
        private String vehicleid;
        private String vehicleregno;
        private String filldate;

        public String getFilldate1() {
            return filldate1;
        }

        public void setFilldate1(String filldate1) {
            this.filldate1 = filldate1;
        }

        private String filldate1;
        private String litre;
        private String rate;
        private String amount;
        private String bunk;
        private String remarks;
        private String kmreading;
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getVehicleid() {
            return vehicleid;
        }

        public void setVehicleid(String vehicleid) {
            this.vehicleid = vehicleid;
        }

        public String getVehicleregno() {
            return vehicleregno;
        }

        public void setVehicleregno(String vehicleregno) {
            this.vehicleregno = vehicleregno;
        }

        public String getFilldate() {
            return filldate;
        }

        public void setFilldate(String filldate) {
            this.filldate = filldate;
        }

        public String getLitre() {
            return litre;
        }

        public void setLitre(String litre) {
            this.litre = litre;
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

        public String getBunk() {
            return bunk;
        }

        public void setBunk(String bunk) {
            this.bunk = bunk;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getKmreading() {
            return kmreading;
        }

        public void setKmreading(String kmreading) {
            this.kmreading = kmreading;
        }
    }
}
