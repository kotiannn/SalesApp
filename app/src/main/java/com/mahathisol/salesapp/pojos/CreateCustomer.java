package com.mahathisol.salesapp.pojos;

/**
 * Created by HP on 07/31/2017.
 */

public class CreateCustomer {


    private Create create;

    public Create getCreate() {
        return create;
    }

    public void setCreate(Create create) {
        this.create = create;
    }

    public static class Create {
        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
