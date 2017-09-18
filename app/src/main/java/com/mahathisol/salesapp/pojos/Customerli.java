package com.mahathisol.salesapp.pojos;

import java.util.List;

/**
 * Created by Admin on 09-08-2017.
 */

public class Customerli
{

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
    }
}
