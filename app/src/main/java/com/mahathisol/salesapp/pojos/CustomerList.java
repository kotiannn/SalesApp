package com.mahathisol.salesapp.pojos;

/**
 * Created by HP on 4/13/2017.
 */

public class CustomerList {

    public int id,stateid;
    public String name, mobile,emailid,address1,address2,address3,city,pincode,gstinno,active,status,retailmrp;


    public void setAll(int id, String name, String mobile, String emailid, String address1, String address2, String address3, String city, String pincode,int stateid, String gstinno, String active,String retailmrp, String status) {
        this.id = id;
        this.stateid = stateid;
        this.name = name;
        this.mobile = mobile;
        this.emailid = emailid;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.city = city;
        this.pincode = pincode;
        this.gstinno = gstinno;
        this.active = active;
        this.retailmrp = retailmrp;
        this.status = status;

    }

    public String getRetailmrp() {
        return retailmrp;
    }

    public void setRetailmrp(String retailmrp) {
        this.retailmrp = retailmrp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStateid() {
        return stateid;
    }

    public void setStateid(int stateid) {
        this.stateid = stateid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getGstinno() {
        return gstinno;
    }

    public void setGstinno(String gstinno) {
        this.gstinno = gstinno;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
