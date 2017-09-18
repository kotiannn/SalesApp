package com.mahathisol.salesapp.pojos;

import java.util.List;

/**
 * Created by HP on 08/10/2017.
 */

public class UploadProductCategory {

    private List<Prcat> prcat;

    public List<Prcat> getPrcat() {
        return prcat;
    }

    public void setPrcat(List<Prcat> prcat) {
        this.prcat = prcat;
    }

    public static class Prcat {
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
