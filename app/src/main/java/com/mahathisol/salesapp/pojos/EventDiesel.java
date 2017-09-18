package com.mahathisol.salesapp.pojos;

/**
 * Created by Admin on 05-08-2017.
 */

public class EventDiesel {


    private  Dieselli.Diesellist ddata;


    public EventDiesel(Dieselli.Diesellist ddata) {
        this.ddata = ddata;
    }
    public Dieselli.Diesellist getDdata() {
        return ddata;
    }
    public void setDdata(Dieselli.Diesellist ddata) {
        this.ddata = ddata;
    }

}
