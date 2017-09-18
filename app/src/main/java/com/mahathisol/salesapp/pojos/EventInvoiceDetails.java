package com.mahathisol.salesapp.pojos;

/**
 * Created by HP on 08/04/2017.
 */

public class EventInvoiceDetails {

    private  InvoiceDetailsList ddata;


    public EventInvoiceDetails(InvoiceDetailsList ddata) {
        this.ddata = ddata;
    }
    public InvoiceDetailsList getDdata() {
        return ddata;
    }
    public void setDdata(InvoiceDetailsList ddata) {
        this.ddata = ddata;
    }
}
