package com.mahathisol.salesapp.pojos;

import java.util.List;

/**
 * Created by Admin on 02-08-2017.
 */

public class Vehicleli {

    private List<Vehiclelist> vehiclelist;

    public List<Vehiclelist> getVehiclelist() {
        return vehiclelist;
    }

    public void setVehiclelist(List<Vehiclelist> vehiclelist) {
        this.vehiclelist = vehiclelist;
    }

    public static class Vehiclelist {
        private String id;
        private String regno;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRegno() {
            return regno;
        }

        public void setRegno(String regno) {
            this.regno = regno;
        }
    }
}
