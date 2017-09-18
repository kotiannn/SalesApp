package com.mahathisol.salesapp.pojos;

/**
 * Created by Admin on 03-08-2017.
 */

public class CreateDiesel {

    private CreateCustomer.Create create;

    public CreateCustomer.Create getCreate() {
        return create;
    }

    public void setCreate(CreateCustomer.Create create) {
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
