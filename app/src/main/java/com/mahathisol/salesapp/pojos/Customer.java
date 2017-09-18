package com.mahathisol.salesapp.pojos;

import java.util.List;

/**
 * Created by HP on 07/31/2017.
 */

public class Customer {

    private List<Customerlist> customerlist;

    public List<Customerlist> getCustomerlist() {
        return customerlist;
    }

    public void setCustomerlist(List<Customerlist> customerlist) {
        this.customerlist = customerlist;
    }

    public static class Customerlist {
        private String id;
        private String name;
        private String mobile;
        private String emailid;
        private String address1;
        private String address2;
        private String address3;
        private String pincode;
        private String city;
        private String stateid;
        private String gstinno;
        private String staten;
        private String active;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getStateid() {
            return stateid;
        }

        public void setStateid(String stateid) {
            this.stateid = stateid;
        }

        public String getGstinno() {
            return gstinno;
        }

        public void setGstinno(String gstinno) {
            this.gstinno = gstinno;
        }

        public String getStaten() {
            return staten;
        }

        public void setStaten(String staten) {
            this.staten = staten;
        }

        public String getActive() {
            return active;
        }

        public void setActive(String active) {
            this.active = active;
        }
    }
}
