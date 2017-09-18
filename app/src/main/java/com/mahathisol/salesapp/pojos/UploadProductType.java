package com.mahathisol.salesapp.pojos;

import java.util.List;

/**
 * Created by HP on 08/10/2017.
 */

public class UploadProductType {

    private List<Prtypelist> prtypelist;

    public List<Prtypelist> getPrtypelist() {
        return prtypelist;
    }

    public void setPrtypelist(List<Prtypelist> prtypelist) {
        this.prtypelist = prtypelist;
    }

    public static class Prtypelist {
        private String id;
        private String description;
        private String prodcategoryid;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getProdcategoryid() {
            return prodcategoryid;
        }

        public void setProdcategoryid(String prodcategoryid) {
            this.prodcategoryid = prodcategoryid;
        }
    }
}
