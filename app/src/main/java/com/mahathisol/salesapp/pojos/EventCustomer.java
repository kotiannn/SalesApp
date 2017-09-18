package com.mahathisol.salesapp.pojos;

/**
 * Created by HP on 08/04/2017.
 */

public class EventCustomer {

    private  CustomerList ddata;


    public EventCustomer(CustomerList ddata) {
        this.ddata = ddata;
    }
    public CustomerList getDdata() {
        return ddata;
    }
    public void setDdata(CustomerList ddata) {
        this.ddata = ddata;
    }
}
