package com.mahathisol.salesapp.pojos;

import java.util.List;

/**
 * Created by HP on 08/10/2017.
 */

public class UploadVaraint {


    private List<Variantlist> variantlist;

    public List<Variantlist> getVariantlist() {
        return variantlist;
    }

    public void setVariantlist(List<Variantlist> variantlist) {
        this.variantlist = variantlist;
    }

    public static class Variantlist {
        private String id;
        private String description;

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
    }
}
