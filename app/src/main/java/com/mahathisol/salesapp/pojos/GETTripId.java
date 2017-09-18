package com.mahathisol.salesapp.pojos;

/**
 * Created by HP on 08/05/2017.
 */

public class GETTripId {

    private String id;
    private String totalValue;
    private String status;
    private String routeid;
    private String code;
    private String invoiceCtr;
    private String tripno;

    public String getTripno() {
        return tripno;
    }

    public void setTripno(String tripno) {
        this.tripno = tripno;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(String totalValue) {
        this.totalValue = totalValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRouteid() {
        return routeid;
    }

    public void setRouteid(String routeid) {
        this.routeid = routeid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInvoiceCtr() {
        return invoiceCtr;
    }

    public void setInvoiceCtr(String invoiceCtr) {
        this.invoiceCtr = invoiceCtr;
    }
}
