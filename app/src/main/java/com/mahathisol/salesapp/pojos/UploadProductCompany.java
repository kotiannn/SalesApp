package com.mahathisol.salesapp.pojos;

import java.util.List;

/**
 * Created by HP on 08/10/2017.
 */

public class UploadProductCompany {

    private List<Prcomplist> prcomplist;

    public List<Prcomplist> getPrcomplist() {
        return prcomplist;
    }

    public void setPrcomplist(List<Prcomplist> prcomplist) {
        this.prcomplist = prcomplist;
    }

    public static class Prcomplist {
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
