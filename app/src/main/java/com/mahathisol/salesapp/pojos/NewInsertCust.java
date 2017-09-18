package com.mahathisol.salesapp.pojos;

import java.util.List;

/**
 * Created by HP on 08/08/2017.
 */

public class NewInsertCust {


    private List<Value> value;

    public List<Value> getValue() {
        return value;
    }

    public void setValue(List<Value> value) {
        this.value = value;
    }

    public static class Value {
        private String id;
        private String name;
        private String mobile;
        private String emailid;
        private String address1;
        private String address2;
        private String address3;
        private String city;
        private String pincode;
        private String stateid;
        private String gstinno;
        private String active;
        private String retailOrMRP;

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

        public String getActive() {
            return active;
        }

        public void setActive(String active) {
            this.active = active;
        }

        public String getRetailOrMRP() {
            return retailOrMRP;
        }

        public void setRetailOrMRP(String retailOrMRP) {
            this.retailOrMRP = retailOrMRP;
        }
    }
}
